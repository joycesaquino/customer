package com.customer.mock;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.entity.Customer;
import com.customer.enums.CustomerStatus;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for creating mock Customer objects for testing.
 */
public class CustomerMock {
    
    /**
     * Creates a mock Customer entity with default values.
     * 
     * @return a mock Customer entity
     */
    public static Customer createMockCustomer() {
        OffsetDateTime now = OffsetDateTime.now();
        
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("123-456-7890");
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        customer.setStatus(CustomerStatus.ACTIVE);
        
        return customer;
    }
    
    /**
     * Creates a mock CustomerDTO with default values.
     * 
     * @return a mock CustomerDTO
     */
    public static CustomerDTO createMockCustomerDTO() {
        OffsetDateTime now = OffsetDateTime.now();
        
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPhone("123-456-7890");
        customerDTO.setCreatedAt(now);
        customerDTO.setUpdatedAt(now);
        customerDTO.setStatus(CustomerStatus.ACTIVE);
        
        return customerDTO;
    }
    
    /**
     * Creates a mock CustomerCreateDTO with default values.
     * 
     * @return a mock CustomerCreateDTO
     */
    public static CustomerCreateDTO createMockCustomerCreateDTO() {
        CustomerCreateDTO customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setFirstName("Jane");
        customerCreateDTO.setLastName("Smith");
        customerCreateDTO.setEmail("jane.smith@example.com");
        customerCreateDTO.setPhone("987-654-3210");
        customerCreateDTO.setStatus(CustomerStatus.ACTIVE);
        
        return customerCreateDTO;
    }
    
    /**
     * Creates a mock CustomerUpdateDTO with default values.
     * 
     * @return a mock CustomerUpdateDTO
     */
    public static CustomerUpdateDTO createMockCustomerUpdateDTO() {
        CustomerUpdateDTO customerUpdateDTO = new CustomerUpdateDTO();
        customerUpdateDTO.setFirstName("Updated");
        customerUpdateDTO.setLastName("Name");
        customerUpdateDTO.setEmail("updated.name@example.com");
        customerUpdateDTO.setPhone("555-555-5555");
        customerUpdateDTO.setStatus(CustomerStatus.INACTIVE);
        
        return customerUpdateDTO;
    }
    
    /**
     * Creates a list of mock Customer entities.
     * 
     * @param count the number of mock customers to create
     * @return a list of mock Customer entities
     */
    public static List<Customer> createMockCustomerList(int count) {
        Customer[] customers = new Customer[count];
        for (int i = 0; i < count; i++) {
            customers[i] = createMockCustomer();
        }
        return Arrays.asList(customers);
    }
    
    /**
     * Creates a list of mock CustomerDTO objects.
     * 
     * @param count the number of mock customer DTOs to create
     * @return a list of mock CustomerDTO objects
     */
    public static List<CustomerDTO> createMockCustomerDTOList(int count) {
        CustomerDTO[] customerDTOs = new CustomerDTO[count];
        for (int i = 0; i < count; i++) {
            customerDTOs[i] = createMockCustomerDTO();
        }
        return Arrays.asList(customerDTOs);
    }
}