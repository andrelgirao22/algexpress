package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.dto.customer.CustomerDTO;
import br.com.alg.algexpress.infra.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
public class CustomerControllerSimple {

    private final CustomerService customerService;

    public CustomerControllerSimple(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Customer> customers = customerService.findActiveCustomers();
        return ResponseEntity.ok(customers.stream().map(CustomerDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(CustomerDTO.fromEntity(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO dto) {
        if (customerService.existsByEmail(dto.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        Customer customer = dto.toEntityForCreation();
        Customer saved = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDTO.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto) {
        return customerService.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    existing.setEmail(dto.email());
                    existing.setPhone(dto.phone());
                    existing.setCpf(dto.cpf());
                    Customer updated = customerService.updateCustomer(existing);
                    return ResponseEntity.ok(CustomerDTO.fromEntity(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/loyalty-points/add")
    public ResponseEntity<CustomerDTO> addLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        try {
            Customer customer = customerService.addLoyaltyPoints(id, points);
            return ResponseEntity.ok(CustomerDTO.fromEntity(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (customerService.findById(id).isPresent()) {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}