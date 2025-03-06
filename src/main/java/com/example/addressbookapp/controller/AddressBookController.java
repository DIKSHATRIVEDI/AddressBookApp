package com.example.addressbookapp.controller;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressbookapp")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @PostMapping("/create")
    public ResponseEntity<AddressBook> createEntry(@RequestBody AddressBookDTO dto) {
        AddressBook addressBook = addressBookService.createAddressBookEntry(dto);
        return ResponseEntity.ok(addressBook);
    }
}
