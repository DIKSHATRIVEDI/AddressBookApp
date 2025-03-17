package com.example.addressbookapp.service;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import com.example.addressbookapp.util.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressBookServiceTest {

    @Mock
    private AddressBookRepository repository;

    @Mock
    private JwtToken tokenUtil;

    @InjectMocks
    private AddressBookService service;

    private AddressBookDTO addressBookDTO;
    private AddressBook addressBook;

    @Autowired
    String VALID_TOKEN = "valid-token";
    @Autowired
    Long USER_ID = 1L;
    @Autowired
    Long ENTRY_ID = 101L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressBookDTO = new AddressBookDTO("John Doe", "john@example.com", "1234567890");
        addressBook = new AddressBook(ENTRY_ID, "John Doe", "john@example.com", "1234567890");
    }

    @Test
    void testCreateAddressBookEntry_WhenAuthenticated() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.save(any(AddressBook.class))).thenReturn(addressBook);

        // Act
        AddressBook created = service.createAddressBookEntry(addressBookDTO, VALID_TOKEN);

        // Assert
        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        assertEquals("john@example.com", created.getEmail());
        assertEquals("1234567890", created.getPhoneNumber());
    }

    @Test
    void testCreateAddressBookEntry_WhenNotAuthenticated_ShouldThrowException() {
        // Mock token validation failure
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.createAddressBookEntry(addressBookDTO, VALID_TOKEN));
        assertEquals("Unauthorized! Please log in first.", exception.getMessage());
    }

    @Test
    void testGetAllEntries_WhenAuthenticated() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.findAll()).thenReturn(Arrays.asList(addressBook));

        // Act
        List<AddressBook> entries = service.getAllEntries(VALID_TOKEN);

        // Assert
        assertEquals(1, entries.size());
        assertEquals("John Doe", entries.get(0).getName());
    }

    @Test
    void testGetEntryById_WhenExists() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.of(addressBook));

        // Act
        AddressBook entry = service.getEntryById(ENTRY_ID, VALID_TOKEN);

        // Assert
        assertNotNull(entry);
        assertEquals("John Doe", entry.getName());
    }

    @Test
    void testGetEntryById_WhenNotExists() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        // Act
        AddressBook entry = service.getEntryById(ENTRY_ID, VALID_TOKEN);

        // Assert
        assertNull(entry);
    }

    @Test
    void testUpdateEntry_WhenExists() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.of(addressBook));
        when(repository.save(any(AddressBook.class))).thenReturn(addressBook);

        // Act
        AddressBook updated = service.updateEntry(ENTRY_ID, addressBookDTO, VALID_TOKEN);

        // Assert
        assertNotNull(updated);
        assertEquals("John Doe", updated.getName());
    }

    @Test
    void testUpdateEntry_WhenNotExists() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.findById(ENTRY_ID)).thenReturn(Optional.empty());

        // Act
        AddressBook updated = service.updateEntry(ENTRY_ID, addressBookDTO, VALID_TOKEN);

        // Assert
        assertNull(updated);
    }

    @Test
    void testDeleteEntry_WhenExists() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.existsById(ENTRY_ID)).thenReturn(true);

        // Act
        service.deleteEntry(ENTRY_ID, VALID_TOKEN);

        // Verify that deleteById was called once
        verify(repository, times(1)).deleteById(ENTRY_ID);
    }

    @Test
    void testDeleteEntry_WhenNotExists_ShouldThrowException() {
        // Mock token validation
        when(tokenUtil.getCurrentUserId(VALID_TOKEN)).thenReturn(USER_ID);
        when(tokenUtil.isUserLoggedIn(USER_ID, VALID_TOKEN)).thenReturn(true);
        when(repository.existsById(ENTRY_ID)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.deleteEntry(ENTRY_ID, VALID_TOKEN));
        assertEquals("Entry with ID " + ENTRY_ID + " not found", exception.getMessage());
    }
}

