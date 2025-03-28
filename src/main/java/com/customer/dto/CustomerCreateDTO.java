package com.customer.dto;

import com.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) for creating a new Customer.
 * Contains only the fields needed for customer creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
}