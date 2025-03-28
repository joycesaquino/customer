package com.customer.mapper;

import com.customer.dto.CustomerCreateDTO;
import com.customer.dto.CustomerDTO;
import com.customer.dto.CustomerUpdateDTO;
import com.customer.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Customer entity and DTOs.
 * Uses Java's Function interface for mapping operations.
 */
@Component
public class CustomerMapper {

    /**
     * Function to convert Customer entity to CustomerDTO.
     */
    private final Function<Customer, CustomerDTO> customerToDtoMapper = customer -> {
        if (customer == null) {
            return null;
        }

        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        dto.setStatus(customer.getStatus());
        return dto;
    };

    /**
     * Function to convert CustomerCreateDTO to Customer entity.
     */
    private final Function<CustomerCreateDTO, Customer> createDtoToEntityMapper = dto -> {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setStatus(dto.getStatus());
        return customer;
    };

    /**
     * Convert Customer entity to CustomerDTO.
     *
     * @param customer the customer entity
     * @return the customer DTO
     */
    public CustomerDTO toDTO(Customer customer) {
        return customerToDtoMapper.apply(customer);
    }

    /**
     * Convert a list of Customer entities to a list of CustomerDTOs.
     *
     * @param customers the list of customer entities
     * @return the list of customer DTOs
     */
    public List<CustomerDTO> toDTOList(List<Customer> customers) {
        if (customers == null) {
            return null;
        }
        return customers.stream()
                .map(customerToDtoMapper)
                .collect(Collectors.toList());
    }

    /**
     * Convert CustomerCreateDTO to Customer entity.
     *
     * @param customerCreateDTO the customer create DTO
     * @return the customer entity
     */
    public Customer toEntity(CustomerCreateDTO customerCreateDTO) {
        return createDtoToEntityMapper.apply(customerCreateDTO);
    }

    /**
     * Update Customer entity from CustomerUpdateDTO.
     *
     * @param customerUpdateDTO the customer update DTO
     * @param customer the customer entity to update
     * @return the updated customer entity
     */
    public Customer updateCustomerFromDTO(CustomerUpdateDTO customerUpdateDTO, Customer customer) {
        if (customerUpdateDTO == null || customer == null) {
            return customer;
        }

        customer.setFirstName(customerUpdateDTO.getFirstName());
        customer.setLastName(customerUpdateDTO.getLastName());
        customer.setEmail(customerUpdateDTO.getEmail());
        customer.setPhone(customerUpdateDTO.getPhone());
        customer.setStatus(customerUpdateDTO.getStatus());
        return customer;
    }
}
