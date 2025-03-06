package com.example.addressbookapp.Interface;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import java.util.List;

public interface IAddressBookService {
    AddressBook createAddressBookEntry(AddressBookDTO dto);
    List<AddressBook> getAllEntries();
    AddressBook getEntryById(Long id);
    AddressBook updateEntry(Long id, AddressBookDTO dto);
    void deleteEntry(Long id);
}
