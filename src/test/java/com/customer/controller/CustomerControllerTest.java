package com.customer.controller;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.mock.CustomerMock;
import com.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(CustomerControllerTest.TestConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    static class TestConfig {
        @Bean
        @Primary
        public CustomerService customerService() {
            return org.mockito.Mockito.mock(CustomerService.class);
        }
    }

    @Test
    @DisplayName("Should return all customers when getAllCustomers is called")
    void getAllCustomers_shouldReturnAllCustomers() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customerDTO, customerDTO));

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[0].phone", is("123-456-7890")))
                .andExpect(jsonPath("$[0].status", is("ACTIVE")));
    }

    @Test
    @DisplayName("Should return customer when getCustomerById is called with existing ID")
    void getCustomerById_shouldReturnCustomer_whenIdExists() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customerDTO));

        // When & Then
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("123-456-7890")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("Should return not found when getCustomerById is called with non-existing ID")
    void getCustomerById_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Given
        when(customerService.getCustomerById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return customer when getCustomerByEmail is called with existing email")
    void getCustomerByEmail_shouldReturnCustomer_whenEmailExists() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        String email = "john.doe@example.com";
        when(customerService.getCustomerByEmail(email)).thenReturn(Optional.of(customerDTO));

        // When & Then
        mockMvc.perform(get("/api/customers/by-email")
                .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("123-456-7890")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("Should return not found when getCustomerByEmail is called with non-existing email")
    void getCustomerByEmail_shouldReturnNotFound_whenEmailDoesNotExist() throws Exception {
        // Given
        String email = "nonexistent@example.com";
        when(customerService.getCustomerByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/customers/by-email")
                .param("email", email))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create and return customer when createCustomer is called with valid data")
    void createCustomer_shouldCreateAndReturnCustomer() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        CustomerCreateDTO customerCreateDTO = CustomerMock.createMockCustomerCreateDTO();
        
        when(customerService.existsByEmail(customerCreateDTO.getEmail())).thenReturn(false);
        when(customerService.createCustomer(any(CustomerCreateDTO.class))).thenReturn(customerDTO);

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("123-456-7890")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("Should return conflict when createCustomer is called with existing email")
    void createCustomer_shouldReturnConflict_whenEmailExists() throws Exception {
        // Given
        CustomerCreateDTO customerCreateDTO = CustomerMock.createMockCustomerCreateDTO();
        when(customerService.existsByEmail(customerCreateDTO.getEmail())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerCreateDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update and return customer when updateCustomer is called with existing ID")
    void updateCustomer_shouldUpdateAndReturnCustomer_whenIdExists() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();
        Long id = 1L;
        
        when(customerService.updateCustomer(eq(id), any(CustomerUpdateDTO.class))).thenReturn(Optional.of(customerDTO));

        // When & Then
        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("123-456-7890")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("Should return not found when updateCustomer is called with non-existing ID")
    void updateCustomer_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Given
        CustomerUpdateDTO customerUpdateDTO = CustomerMock.createMockCustomerUpdateDTO();
        Long id = 999L;
        when(customerService.updateCustomer(eq(id), any(CustomerUpdateDTO.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerUpdateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return no content when deleteCustomer is called with existing ID")
    void deleteCustomer_shouldReturnNoContent_whenIdExists() throws Exception {
        // Given
        CustomerDTO customerDTO = CustomerMock.createMockCustomerDTO();
        Long id = 1L;
        when(customerService.getCustomerById(id)).thenReturn(Optional.of(customerDTO));

        // When & Then
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return not found when deleteCustomer is called with non-existing ID")
    void deleteCustomer_shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
        // Given
        Long id = 999L;
        when(customerService.getCustomerById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/customers/999"))
                .andExpect(status().isNotFound());
    }
}