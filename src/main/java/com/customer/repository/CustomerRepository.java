package com.customer.repository;

import com.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Customer entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find a customer by email address.
     * 
     * @param email the email address to search for
     * @return an Optional containing the customer if found
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Check if a customer exists with the given email.
     * 
     * @param email the email address to check
     * @return true if a customer exists with the email, false otherwise
     */
    boolean existsByEmail(String email);
}