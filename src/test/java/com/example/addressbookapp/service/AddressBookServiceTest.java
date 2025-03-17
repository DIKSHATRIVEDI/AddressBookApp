package com.example.addressbookapp.service;

import com.example.addressbookapp.dto.AddressBookDTO;
import com.example.addressbookapp.model.AddressBook;
import com.example.addressbookapp.repository.AddressBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressBookServiceTest {

    @InjectMocks
    private AddressBookService addressBookService;

    @Mock
    private AddressBookRepository addressBookRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(authentication.getPrincipal()).thenReturn(1L);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateAddressBookEntry() {
        // Arrange
        AddressBookDTO dto = new AddressBookDTO("John Doe", "john@example.com", "1234567890");
        AddressBook addressBook = new AddressBook(null, "John Doe", "john@example.com", "1234567890");

        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(addressBook);

        // Act
        AddressBook result = addressBookService.createAddressBookEntry(dto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(rabbitTemplate).convertAndSend("app.exchange", "contact.add", "john@example.com");
        verify(addressBookRepository).save(any(AddressBook.class));
    }

    @Test
    void testGetAllEntries() {
        // Arrange
        AddressBook address1 = new AddressBook(1L, "John Doe", "john@example.com", "1234567890");
        AddressBook address2 = new AddressBook(2L, "Jane Doe", "jane@example.com", "0987654321");

        when(addressBookRepository.findAll()).thenReturn(Arrays.asList(address1, address2));

        // Act
        List<AddressBook> result = addressBookService.getAllEntries();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        assertTrue(result.size() > 0); // ✅ Using assertTrue
        verify(addressBookRepository).findAll();
    }

    @Test
    void testGetEntryById_Found() {
        // Arrange
        AddressBook address = new AddressBook(1L, "John Doe", "john@example.com", "1234567890");
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(address));

        // Act
        AddressBook result = addressBookService.getEntryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(addressBookRepository).findById(1L);
    }

    @Test
    void testGetEntryById_NotFound() {
        // Arrange
        when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        AddressBook result = addressBookService.getEntryById(1L);

        // Assert
        assertNull(result);
        verify(addressBookRepository).findById(1L);
    }

    @Test
    void testUpdateEntry_Success() {
        // Arrange
        AddressBookDTO dto = new AddressBookDTO("John Smith", "johnsmith@example.com", "1112223333");
        AddressBook existing = new AddressBook(1L, "John Doe", "john@example.com", "1234567890");

        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(existing);

        // Act
        AddressBook result = addressBookService.updateEntry(1L, dto);

        // Assert
        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("johnsmith@example.com", result.getEmail());
        assertEquals("1112223333", result.getPhoneNumber());
        assertTrue(result.getName().equals("John Smith")); // ✅ Using assertTrue
        verify(addressBookRepository).save(any(AddressBook.class));
    }

    @Test
    void testUpdateEntry_NotFound() {
        // Arrange
        AddressBookDTO dto = new AddressBookDTO("John Smith", "johnsmith@example.com", "1112223333");

        when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            addressBookService.updateEntry(1L, dto);
        });

        assertEquals("Entry with ID 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteEntry_Success() {
        // Arrange
        when(addressBookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressBookRepository).deleteById(1L);

        // Act
        addressBookService.deleteEntry(1L);

        // Assert
        assertTrue(addressBookRepository.existsById(1L)); // ✅ Using assertTrue
        verify(addressBookRepository).deleteById(1L);
    }

    @Test
    void testDeleteEntry_NotFound() {
        // Arrange
        when(addressBookRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            addressBookService.deleteEntry(1L);
        });

        assertEquals("Entry with ID 1 not found", exception.getMessage());
        assertFalse(addressBookRepository.existsById(1L)); // ✅ Using assertFalse
        verify(addressBookRepository, atLeastOnce()).existsById(1L);
    }

}
