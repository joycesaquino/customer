package com.customer.service;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.entity.Customer;
import com.customer.mapper.CustomerMapper;
import com.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing customer operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Get all customers.
     *
     * @return list of all customers as DTOs
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDTOList(customers);
    }

    /**
     * Get a customer by ID.
     *
     * @param id the customer ID
     * @return an Optional containing the customer DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDTO);
    }

    /**
     * Get a customer by email.
     *
     * @param email the customer email
     * @return an Optional containing the customer DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customerMapper::toDTO);
    }

    /**
     * Create a new customer.
     *
     * @param customerCreateDTO the customer data to create
     * @return the created customer as DTO
     */
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO customerCreateDTO) {
        Customer customer = customerMapper.toEntity(customerCreateDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }

    /**
     * Update an existing customer.
     *
     * @param id the customer ID
     * @param customerUpdateDTO the customer data to update
     * @return the updated customer as DTO
     */
    @Transactional
    public Optional<CustomerDTO> updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    Customer updatedCustomer = customerMapper.updateCustomerFromDTO(customerUpdateDTO, existingCustomer);
                    return customerMapper.toDTO(customerRepository.save(updatedCustomer));
                });
    }

    /**
     * Delete a customer by ID.
     *
     * @param id the customer ID to delete
     */
    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * Check if a customer exists with the given email.
     *
     * @param email the email to check
     * @return true if a customer exists with the email, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}
