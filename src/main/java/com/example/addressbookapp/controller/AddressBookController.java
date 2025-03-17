package com.example.addressbookapp.controller;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping("/create")
    public AddressBook createAddress(@RequestBody AddressBookDTO dto) {
        return addressBookService.createAddressBookEntry(dto);
    }

    @GetMapping("/all")
    public List<AddressBook> getAllEntries() {
        return addressBookService.getAllEntries();
    }

    @GetMapping("/{id}")
    public AddressBook getEntryById(@PathVariable Long id) {
        return addressBookService.getEntryById(id);
    }

    @PutMapping("/update/{id}")
    public AddressBook updateEntry(@PathVariable Long id, @RequestBody AddressBookDTO dto) {
        return addressBookService.updateEntry(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEntry(@PathVariable Long id) {
        addressBookService.deleteEntry(id);
    }
}
