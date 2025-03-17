package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import com.example.addressbookapp.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream
=======
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
    public AddressBook createAddressBookEntry(AddressBookDTO dto, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
=======
    @CacheEvict(value = "addressBookCache", allEntries = true) // Clear cache on new entry
    public AddressBook createAddressBookEntry(AddressBookDTO dto) {
        Long userId = getCurrentUserId();
>>>>>>> Stashed changes
        AddressBook addressBook = new AddressBook(null, dto.getName(), dto.getEmail(), dto.getPhoneNumber());

        // Publish contact added event
        rabbitTemplate.convertAndSend("app.exchange", "contact.add", dto.getEmail());

        return addressBookRepository.save(addressBook);
    }

    @Override
<<<<<<< Updated upstream
    public List<AddressBook> getAllEntries(String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
=======
    @Cacheable(value = "addressBookCache") // Cache results
    public List<AddressBook> getAllEntries() {
        getCurrentUserId(); // Ensure user is authenticated
>>>>>>> Stashed changes
        return addressBookRepository.findAll();
    }

    @Override
<<<<<<< Updated upstream
    public AddressBook getEntryById(Long id, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
=======
    @Cacheable(value = "addressBookCache", key = "#id") // Cache specific entry by ID
    public AddressBook getEntryById(Long id) {
        getCurrentUserId(); // Ensure user is authenticated
>>>>>>> Stashed changes
        return addressBookRepository.findById(id).orElse(null);
    }

    @Override
<<<<<<< Updated upstream
    public AddressBook updateEntry(Long id, AddressBookDTO dto, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
=======
    @Caching(evict = {
            @CacheEvict(value = "addressBookCache", allEntries = true),
            @CacheEvict(value = "addressBookCache", key = "#id")
    })
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        getCurrentUserId(); // Ensure user is authenticated
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public void deleteEntry(Long id, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
=======
    @CacheEvict(value = "addressBookCache", key = "#id") // Remove from cache on delete
    public void deleteEntry(Long id) {
        getCurrentUserId(); // Ensure user is authenticated
>>>>>>> Stashed changes
        if (addressBookRepository.existsById(id)) {
            addressBookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Entry with ID " + id + " not found");
        }
    }
}
