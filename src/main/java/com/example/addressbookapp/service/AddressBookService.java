package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAddressBookService;
import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    AddressBookRepository addressBookRepository;

    @Override
    public AddressBook createAddressBookEntry(AddressBookDTO dto) {
        try {
            AddressBook addressBook = new AddressBook(null, dto.getName(), dto.getEmail(), dto.getPhoneNumber());
            return addressBookRepository.save(addressBook);
        } catch (Exception e) {
            System.err.println("Error creating address book entry: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<AddressBook> getAllEntries() {
        try {
            return addressBookRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error fetching all address book entries: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public AddressBook getEntryById(Long id) {
        try {
            Optional<AddressBook> entry = addressBookRepository.findById(id);
            if (entry.isPresent()) {
                return entry.get();
            } else {
                System.err.println("Address Book entry with ID " + id + " not found");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching entry by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        try {
            Optional<AddressBook> entry = addressBookRepository.findById(id);
            if (entry.isPresent()) {
                AddressBook addressBook = entry.get();
                addressBook.setName(dto.getName());
                addressBook.setEmail(dto.getEmail());
                addressBook.setPhoneNumber(dto.getPhoneNumber());
                return addressBookRepository.save(addressBook);
            } else {
                System.err.println("Address Book entry with ID " + id + " not found");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error updating address book entry: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteEntry(Long id) {
        try {
            if (addressBookRepository.existsById(id)) {
                addressBookRepository.deleteById(id);
            } else {
                System.err.println("Entry with ID " + id + " not found");
            }
        } catch (Exception e) {
            System.err.println("Error deleting address book entry: " + e.getMessage());
        }
    }
}
