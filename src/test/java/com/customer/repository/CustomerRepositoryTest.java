package com.customer.repository;

import com.customer.entity.Customer;
import com.customer.enums.CustomerStatus;
import com.customer.mock.CustomerMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("Should return customer when findByEmail is called with existing email")
    void findByEmail_shouldReturnCustomer_whenEmailExists() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        // Remove ID to let the database generate it
        customer.setId(null);
        
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> found = customerRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
        assertEquals("john.doe@example.com", found.get().getEmail());
        assertEquals("123-456-7890", found.get().getPhone());
        assertEquals(CustomerStatus.ACTIVE, found.get().getStatus());
    }

    @Test
    @DisplayName("Should return empty when findByEmail is called with non-existing email")
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        // When
        Optional<Customer> found = customerRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should return true when existsByEmail is called with existing email")
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        // Given
        Customer customer = CustomerMock.createMockCustomer();
        // Use a different email to avoid conflicts with other tests
        customer.setEmail("jane.smith@example.com");
        // Remove ID to let the database generate it
        customer.setId(null);
        
        entityManager.persist(customer);
        entityManager.flush();

        // When
        boolean exists = customerRepository.existsByEmail("jane.smith@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when existsByEmail is called with non-existing email")
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        // When
        boolean exists = customerRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }
}