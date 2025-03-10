package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import com.example.addressbookapp.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private JwtToken tokenUtil;

    private void checkAuthentication(Long userId, String token) {
        if (userId == null || !tokenUtil.isUserLoggedIn(userId, token)) {
            throw new RuntimeException("Unauthorized! Please log in first.");
        }
    }

    @Override
    public AddressBook createAddressBookEntry(AddressBookDTO dto, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
        AddressBook addressBook = new AddressBook(null, dto.getName(), dto.getEmail(), dto.getPhoneNumber());
        return addressBookRepository.save(addressBook);
    }

    @Override
    public List<AddressBook> getAllEntries(String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
        return addressBookRepository.findAll();
    }

    @Override
    public AddressBook getEntryById(Long id, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
        return addressBookRepository.findById(id).orElse(null);
    }

    @Override
    public AddressBook updateEntry(Long id, AddressBookDTO dto, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
        Optional<AddressBook> entry = addressBookRepository.findById(id);
        if (entry.isPresent()) {
            AddressBook addressBook = entry.get();
            addressBook.setName(dto.getName());
            addressBook.setEmail(dto.getEmail());
            addressBook.setPhoneNumber(dto.getPhoneNumber());
            return addressBookRepository.save(addressBook);
        }
        return null;
    }

    @Override
    public void deleteEntry(Long id, String token) {
        Long userId = tokenUtil.getCurrentUserId(token);
        checkAuthentication(userId, token);
        if (addressBookRepository.existsById(id)) {
            addressBookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Entry with ID " + id + " not found");
        }
    }
}
