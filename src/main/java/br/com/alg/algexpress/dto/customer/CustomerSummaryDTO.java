package br.com.alg.algexpress.dto.customer;

import br.com.alg.algexpress.domain.customer.Customer;

import java.time.LocalDateTime;

public record CustomerSummaryDTO(
    Long id,
    String name,
    String email,
    String phone,
    String cpf,
    Integer loyaltyPoints,
    Customer.CustomerStatus status,
    LocalDateTime registrationDate
) {
    
    public static CustomerSummaryDTO fromEntity(Customer customer) {
        return new CustomerSummaryDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getCpf(),
            customer.getLoyaltyPoints(),
            customer.getStatus(),
            customer.getRegistrationDate()
        );
    }
}