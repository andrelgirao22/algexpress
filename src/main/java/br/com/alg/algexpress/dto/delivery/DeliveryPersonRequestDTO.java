package br.com.alg.algexpress.dto.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeliveryPersonRequestDTO {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone format is invalid")
    @Size(max = 15, message = "Phone must not exceed 15 characters")
    private String phone;
    
    @Size(max = 20, message = "License plate must not exceed 20 characters")
    private String vehicleLicensePlate;
    
    @Size(max = 50, message = "Vehicle model must not exceed 50 characters")
    private String vehicleModel;
}