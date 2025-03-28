package com.customer.mapper;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.entity.Customer;
import com.customer.mock.CustomerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    @DisplayName("Should map Customer to CustomerDTO")
    void toDTO_shouldMapCustomerToDTO() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();

        // When
        CustomerDTO dto = customerMapper.toDTO(customer);

        // Then
        assertNotNull(dto);
        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getFirstName(), dto.getFirstName());
        assertEquals(customer.getLastName(), dto.getLastName());
        assertEquals(customer.getEmail(), dto.getEmail());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getCreatedAt(), dto.getCreatedAt());
        assertEquals(customer.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(customer.getStatus(), dto.getStatus());
    }

    @Test
    @DisplayName("Should return null when toDTO is called with null Customer")
    void toDTO_shouldReturnNullWhenCustomerIsNull() {
        // When
        CustomerDTO dto = customerMapper.toDTO(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should map Customer list to CustomerDTO list")
    void toDTOList_shouldMapCustomerListToDTOList() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        List<Customer> customers = Arrays.asList(customer, customer);

        // When
        List<CustomerDTO> dtos = customerMapper.toDTOList(customers);

        // Then
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        
        CustomerDTO firstDto = dtos.get(0);
        assertEquals(customer.getId(), firstDto.getId());
        assertEquals(customer.getFirstName(), firstDto.getFirstName());
        assertEquals(customer.getLastName(), firstDto.getLastName());
        assertEquals(customer.getEmail(), firstDto.getEmail());
        assertEquals(customer.getPhone(), firstDto.getPhone());
        assertEquals(customer.getCreatedAt(), firstDto.getCreatedAt());
        assertEquals(customer.getUpdatedAt(), firstDto.getUpdatedAt());
        assertEquals(customer.getStatus(), firstDto.getStatus());
    }

    @Test
    @DisplayName("Should return null when toDTOList is called with null Customer list")
    void toDTOList_shouldReturnNullWhenCustomerListIsNull() {
        // When
        List<CustomerDTO> dtos = customerMapper.toDTOList(null);

        // Then
        assertNull(dtos);
    }

    @Test
    @DisplayName("Should map CustomerCreateDTO to Customer entity")
    void toEntity_shouldMapCreateDTOToEntity() {
        // Given
        CustomerCreateDTO customerCreateDTO = CustomerMock.createMockCustomerCreateDTO();

        // When
        Customer entity = customerMapper.toEntity(customerCreateDTO);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals(customerCreateDTO.getFirstName(), entity.getFirstName());
        assertEquals(customerCreateDTO.getLastName(), entity.getLastName());
        assertEquals(customerCreateDTO.getEmail(), entity.getEmail());
        assertEquals(customerCreateDTO.getPhone(), entity.getPhone());
        assertEquals(customerCreateDTO.getStatus(), entity.getStatus());
    }

    @Test
    @DisplayName("Should return null when toEntity is called with null CustomerCreateDTO")
    void toEntity_shouldReturnNullWhenCreateDTOIsNull() {
        // When
        Customer entity = customerMapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should update Customer from CustomerUpdateDTO")
    void updateCustomerFromDTO_shouldUpdateCustomerFromDTO() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();

        // When
        Customer updatedCustomer = customerMapper.updateCustomerFromDTO(customerUpdateDTO, customer);

        // Then
        assertNotNull(updatedCustomer);
        assertEquals(customer.getId(), updatedCustomer.getId());
        assertEquals(customerUpdateDTO.getFirstName(), updatedCustomer.getFirstName());
        assertEquals(customerUpdateDTO.getLastName(), updatedCustomer.getLastName());
        assertEquals(customerUpdateDTO.getEmail(), updatedCustomer.getEmail());
        assertEquals(customerUpdateDTO.getPhone(), updatedCustomer.getPhone());
        assertEquals(customerUpdateDTO.getStatus(), updatedCustomer.getStatus());
        assertEquals(customer.getCreatedAt(), updatedCustomer.getCreatedAt());
        assertEquals(customer.getUpdatedAt(), updatedCustomer.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return original Customer when updateCustomerFromDTO is called with null DTO")
    void updateCustomerFromDTO_shouldReturnOriginalCustomerWhenDTOIsNull() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();

        // When
        Customer updatedCustomer = customerMapper.updateCustomerFromDTO(null, customer);

        // Then
        assertSame(customer, updatedCustomer);
    }

    @Test
    @DisplayName("Should return null when updateCustomerFromDTO is called with null Customer")
    void updateCustomerFromDTO_shouldReturnNullWhenCustomerIsNull() {
        // Given
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();

        // When
        Customer updatedCustomer = customerMapper.updateCustomerFromDTO(customerUpdateDTO, null);

        // Then
        assertNull(updatedCustomer);
    }
}