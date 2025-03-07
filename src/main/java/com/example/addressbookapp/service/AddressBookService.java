package com.example.addressbookapp.service;

import com.example.addressbookapp.Exception.AddressBookException;
import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
        return addressBookRepository.findById(id)
                .orElseThrow(() -> new AddressBookException("Employee with ID " + id + " not found"));
    }

    @Override
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new AddressBookException("Address Book entry with ID " + id + " not found"));
        addressBook.setName(dto.getName());
        addressBook.setEmail(dto.getEmail());
        addressBook.setPhoneNumber(dto.getPhoneNumber());

        return addressBookRepository.save(addressBook);
    }

    @Override
    public void deleteEntry(Long id) {
        if (!addressBookRepository.existsById(id)) {
            throw new AddressBookException("Entry with ID " + id + " not found");
        }
        addressBookRepository.deleteById(id);
    }
}