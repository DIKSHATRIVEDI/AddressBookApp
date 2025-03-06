package com.example.addressbookapp.Interface;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;

public interface IAddressBookService {
    AddressBook createAddressBookEntry(AddressBookDTO dto);
}
