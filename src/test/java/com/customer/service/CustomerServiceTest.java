package com.customer.service;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.entity.Customer;
import com.customer.mapper.CustomerMapper;
import com.customer.mock.CustomerMock;
import com.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Should return all customers when getAllCustomers is called")
    void getAllCustomers_shouldReturnAllCustomers() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        List<Customer> customers = Arrays.asList(customer, customer);
        List<CustomerDTO> expectedDTOs = Arrays.asList(customerDTO, customerDTO);
        
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDTOList(customers)).thenReturn(expectedDTOs);

        // When
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDTOs, result);
        
        verify(customerRepository).findAll();
        verify(customerMapper).toDTOList(customers);
    }

    @Test
    @DisplayName("Should return customer when getCustomerById is called with existing ID")
    void getCustomerById_shouldReturnCustomer_whenIdExists() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        // When
        Optional<CustomerDTO> result = customerService.getCustomerById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerDTO, result.get());
        
        verify(customerRepository).findById(1L);
        verify(customerMapper).toDTO(customer);
    }

    @Test
    @DisplayName("Should return empty when getCustomerById is called with non-existing ID")
    void getCustomerById_shouldReturnEmpty_whenIdDoesNotExist() {
        // Given
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDTO> result = customerService.getCustomerById(999L);

        // Then
        assertFalse(result.isPresent());
        
        verify(customerRepository).findById(999L);
        verify(customerMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should return customer when getCustomerByEmail is called with existing email")
    void getCustomerByEmail_shouldReturnCustomer_whenEmailExists() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        String email = "john.doe@example.com";
        
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        // When
        Optional<CustomerDTO> result = customerService.getCustomerByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerDTO, result.get());
        
        verify(customerRepository).findByEmail(email);
        verify(customerMapper).toDTO(customer);
    }

    @Test
    @DisplayName("Should return empty when getCustomerByEmail is called with non-existing email")
    void getCustomerByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        // Given
        String email = "nonexistent@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDTO> result = customerService.getCustomerByEmail(email);

        // Then
        assertFalse(result.isPresent());
        
        verify(customerRepository).findByEmail(email);
        verify(customerMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should create and return customer when createCustomer is called")
    void createCustomer_shouldCreateAndReturnCustomer() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        CustomerCreateDTO customerCreateDTO = CustomerMock.createMockCustomerCreateDTO();
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Jane");
        newCustomer.setLastName("Smith");
        
        when(customerMapper.toEntity(customerCreateDTO)).thenReturn(newCustomer);
        when(customerRepository.save(newCustomer)).thenReturn(customer);
        when(customerMapper.toDTO(customer)).thenReturn(customerDTO);

        // When
        CustomerDTO result = customerService.createCustomer(customerCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerDTO, result);
        
        verify(customerMapper).toEntity(customerCreateDTO);
        verify(customerRepository).save(newCustomer);
        verify(customerMapper).toDTO(customer);
    }

    @Test
    @DisplayName("Should update and return customer when updateCustomer is called with existing ID")
    void updateCustomer_shouldUpdateAndReturnCustomer_whenIdExists() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();
        Long id = 1L;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setFirstName("Updated");
        
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.updateCustomerFromDTO(customerUpdateDTO, customer)).thenReturn(updatedCustomer);
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);
        when(customerMapper.toDTO(updatedCustomer)).thenReturn(customerDTO);

        // When
        Optional<CustomerDTO> result = customerService.updateCustomer(id, customerUpdateDTO);

        // Then
        assertTrue(result.isPresent());
        assertEquals(customerDTO, result.get());
        
        verify(customerRepository).findById(id);
        verify(customerMapper).updateCustomerFromDTO(customerUpdateDTO, customer);
        verify(customerRepository).save(updatedCustomer);
        verify(customerMapper).toDTO(updatedCustomer);
    }

    @Test
    @DisplayName("Should return empty when updateCustomer is called with non-existing ID")
    void updateCustomer_shouldReturnEmpty_whenIdDoesNotExist() {
        // Given
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();
        Long id = 999L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDTO> result = customerService.updateCustomer(id, customerUpdateDTO);

        // Then
        assertFalse(result.isPresent());
        
        verify(customerRepository).findById(id);
        verify(customerMapper, never()).updateCustomerFromDTO(any(), any());
        verify(customerRepository, never()).save(any());
        verify(customerMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should delete customer when deleteCustomer is called")
    void deleteCustomer_shouldDeleteCustomer() {
        // Given
        Long id = 1L;

        // When
        customerService.deleteCustomer(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should return true when existsByEmail is called with existing email")
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        // Given
        String email = "john.doe@example.com";
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = customerService.existsByEmail(email);

        // Then
        assertTrue(result);
        
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("Should return false when existsByEmail is called with non-existing email")
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        // Given
        String email = "nonexistent@example.com";
        when(customerRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = customerService.existsByEmail(email);

        // Then
        assertFalse(result);
        
        verify(customerRepository).existsByEmail(email);
    }
}