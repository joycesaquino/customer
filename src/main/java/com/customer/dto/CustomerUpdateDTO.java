package com.customer.dto;

import com.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) for updating an existing Customer.
 * Contains only the fields that can be updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerStatus status;
}