package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import com.example.addressbookapp.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    @CacheEvict(value = "addressBookCache", allEntries = true) // Clear cache on new entry
    public AddressBook createAddressBookEntry(AddressBookDTO dto) {
        Long userId = getCurrentUserId();
        AddressBook addressBook = new AddressBook(null, dto.getName(), dto.getEmail(), dto.getPhoneNumber());

        // Publish contact added event
        rabbitTemplate.convertAndSend("app.exchange", "contact.add", dto.getEmail());

        return addressBookRepository.save(addressBook);
    }

    @Override
    @Cacheable(value = "addressBookCache") // Cache results
    public List<AddressBook> getAllEntries() {
        getCurrentUserId(); // Ensure user is authenticated
        return addressBookRepository.findAll();
    }

    @Override
    @Cacheable(value = "addressBookCache", key = "#id") // Cache specific entry by ID
    public AddressBook getEntryById(Long id) {
        getCurrentUserId(); // Ensure user is authenticated
        return addressBookRepository.findById(id).orElse(null);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "addressBookCache", allEntries = true),
            @CacheEvict(value = "addressBookCache", key = "#id")
    })
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        getCurrentUserId(); // Ensure user is authenticated
        Optional<AddressBook> entry = addressBookRepository.findById(id);
        if (entry.isPresent()) {
            AddressBook addressBook = entry.get();
            addressBook.setName(dto.getName());
            addressBook.setEmail(dto.getEmail());
            addressBook.setPhoneNumber(dto.getPhoneNumber());
            return addressBookRepository.save(addressBook);
        }
        throw new RuntimeException("Entry with ID " + id + " not found");
    }

    @Override
    @CacheEvict(value = "addressBookCache", key = "#id") // Remove from cache on delete
    public void deleteEntry(Long id) {
        getCurrentUserId(); // Ensure user is authenticated
        if (addressBookRepository.existsById(id)) {
            addressBookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Entry with ID " + id + " not found");
        }
    }
}
