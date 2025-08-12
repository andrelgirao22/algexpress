package br.com.alg.algexpress.dto.customer;

import br.com.alg.algexpress.domain.customer.Customer;
// AddressDTO agora está no mesmo pacote
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CustomerDTO(
    Long id,
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone format is invalid")
    @Size(max = 15, message = "Phone must not exceed 15 characters")
    String phone,
    
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF format should be xxx.xxx.xxx-xx")
    @Size(max = 14, message = "CPF must not exceed 14 characters")
    String cpf,
    
    Integer loyaltyPoints,
    Customer.CustomerStatus status,
    LocalDateTime registrationDate,
    List<AddressDTO> addresses
) {
    
    public static CustomerDTO fromEntity(Customer customer) {
        List<AddressDTO> addressDTOs = null;
        if (customer.getAddresses() != null) {
            addressDTOs = customer.getAddresses().stream()
                    .map(AddressDTO::fromEntity)
                    .collect(Collectors.toList());
        }
        
        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getCpf(),
            customer.getLoyaltyPoints(),
            customer.getStatus(),
            customer.getRegistrationDate(),
            addressDTOs
        );
    }
    
    public static CustomerDTO fromEntityWithoutAddresses(Customer customer) {
        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getEmail(),
            customer.getPhone(),
            customer.getCpf(),
            customer.getLoyaltyPoints(),
            customer.getStatus(),
            customer.getRegistrationDate(),
            null  // addresses não carregados
        );
    }
    
    public Customer toEntity() {
        Customer customer = new Customer();
        customer.setId(this.id);
        customer.setName(this.name);
        customer.setEmail(this.email);
        customer.setPhone(this.phone);
        customer.setCpf(this.cpf);
        customer.setLoyaltyPoints(this.loyaltyPoints);
        customer.setStatus(this.status);
        customer.setRegistrationDate(this.registrationDate);
        return customer;
    }
    
    public Customer toEntityForCreation() {
        Customer customer = new Customer();
        customer.setName(this.name);
        customer.setEmail(this.email);
        customer.setPhone(this.phone);
        customer.setCpf(this.cpf);
        return customer;
    }
}