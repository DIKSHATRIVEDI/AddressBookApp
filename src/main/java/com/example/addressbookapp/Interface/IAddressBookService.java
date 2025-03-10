package com.example.addressbookapp.Interface;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import java.util.List;

public interface IAddressBookService {
    AddressBook createAddressBookEntry(AddressBookDTO dto, String token);
    List<AddressBook> getAllEntries(String token);
    AddressBook getEntryById(Long id, String token);
    AddressBook updateEntry(Long id, AddressBookDTO dto, String token);
    void deleteEntry(Long id, String token);
}
