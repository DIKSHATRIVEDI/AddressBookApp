package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    AddressBookRepository addressBookRepository;

    @Override
    public AddressBook createAddressBookEntry(AddressBookDTO dto) {
        AddressBook addressBook = new AddressBook(null, dto.getName(), dto.getEmail(), dto.getPhoneNumber());
        return addressBookRepository.save(addressBook);
    }

    @Override
    public List<AddressBook> getAllEntries() {
        return addressBookRepository.findAll();
    }

    @Override
    public AddressBook getEntryById(Long id) {
        return addressBookRepository.findById(id).orElse(null);
    }

    @Override
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        Optional<AddressBook> existingEntry = addressBookRepository.findById(id);

        if (existingEntry.isPresent()) {
            AddressBook updatedEntry = existingEntry.get();
            updatedEntry.setName(dto.getName());
            updatedEntry.setEmail(dto.getEmail());
            updatedEntry.setPhoneNumber(dto.getPhoneNumber());
            return addressBookRepository.save(updatedEntry);
        }
        return null;
    }

    @Override
    public void deleteEntry(Long id) {
        addressBookRepository.deleteById(id);
    }
}