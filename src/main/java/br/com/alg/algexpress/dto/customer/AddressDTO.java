package br.com.alg.algexpress.dto.customer;

import br.com.alg.algexpress.domain.valueObjects.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AddressDTO(
    Long id,
    
    Long customerId,
    
    @NotBlank(message = "Street is required")
    @Size(max = 200, message = "Street must not exceed 200 characters")
    String street,
    
    @NotBlank(message = "Number is required")
    @Size(max = 10, message = "Number must not exceed 10 characters")
    String number,
    
    @Size(max = 100, message = "Complement must not exceed 100 characters")
    String complement,
    
    @NotBlank(message = "Neighborhood is required")
    @Size(max = 100, message = "Neighborhood must not exceed 100 characters")
    String neighborhood,
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,
    
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "State must be exactly 2 characters")
    String state,
    
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "ZIP code format is invalid")
    String zipCode,
    
    @Size(max = 200, message = "Reference points must not exceed 200 characters")
    String referencePoints,
    
    Address.AddressType type,
    
    BigDecimal deliveryFee,

    Boolean primary
) {
    
    public static AddressDTO fromEntity(Address address) {
        return new AddressDTO(
            address.getId(),
            address.getCustomer() != null ? address.getCustomer().getId() : null,
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getReferencePoints(),
            address.getType(),
            address.getDeliveryFee(),
            address.getPrimary()
        );
    }
    
    public Address toEntity() {
        Address address = new Address();
        address.setId(this.id);
        address.setStreet(this.street);
        address.setNumber(this.number);
        address.setComplement(this.complement);
        address.setNeighborhood(this.neighborhood);
        address.setCity(this.city);
        address.setState(this.state);
        address.setZipCode(this.zipCode);
        address.setReferencePoints(this.referencePoints);
        address.setType(this.type);
        address.setDeliveryFee(this.deliveryFee);
        address.setPrimary(this.primary != null ? this.primary : false);
        return address;
    }
}