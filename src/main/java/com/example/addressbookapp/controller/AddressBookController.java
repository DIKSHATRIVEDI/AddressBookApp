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
    public AddressBook createAddress(@RequestBody AddressBookDTO dto, @RequestParam String token) {
        return addressBookService.createAddressBookEntry(dto, token);
    }

    @GetMapping("/all")
    public List<AddressBook> getAllEntries(@RequestParam String token) {
        return addressBookService.getAllEntries(token);
    }

    @GetMapping("/{id}")
    public AddressBook getEntryById(@PathVariable Long id, @RequestParam String token) {
        return addressBookService.getEntryById(id, token);
    }

    @PutMapping("/update/{id}")
    public AddressBook updateEntry(@PathVariable Long id, @RequestBody AddressBookDTO dto, @RequestParam String token) {
        return addressBookService.updateEntry(id, dto, token);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEntry(@PathVariable Long id, @RequestParam String token) {
        addressBookService.deleteEntry(id, token);
    }
}
