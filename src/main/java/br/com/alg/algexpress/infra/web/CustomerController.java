package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.valueObjects.Address;
import br.com.alg.algexpress.dto.customer.CustomerDTO;
import br.com.alg.algexpress.dto.customer.CustomerSummaryDTO;
import br.com.alg.algexpress.dto.customer.AddressDTO;
import br.com.alg.algexpress.infra.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes e seus endereços")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Listar clientes ativos", 
               description = "Retorna lista resumida de todos os clientes com status ativo (sem endereços)")
    @ApiResponse(responseCode = "200", description = "Lista resumida de clientes ativos")
    public ResponseEntity<List<CustomerSummaryDTO>> getAllCustomers() {
        List<Customer> customers = customerService.findActiveCustomers();
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", 
               description = "Retorna os detalhes de um cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID do cliente", required = true) 
            @PathVariable Long id) {
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(CustomerDTO.fromEntity(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        return customerService.findByEmail(email)
                .map(customer -> ResponseEntity.ok(CustomerDTO.fromEntity(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        return customerService.findByPhone(phone)
                .map(customer -> ResponseEntity.ok(CustomerDTO.fromEntity(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        List<Customer> customers = customerService.findByStatus(status);
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar clientes por nome", 
               description = "Busca clientes que contenham o nome especificado")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrados")
    public ResponseEntity<List<CustomerSummaryDTO>> searchCustomersByName(
            @Parameter(description = "Nome ou parte do nome do cliente", required = true) 
            @RequestParam String name) {
        List<Customer> customers = customerService.findByNameContaining(name);
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/loyalty-points")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomersByLoyaltyPoints(@RequestParam Integer minPoints) {
        List<Customer> customers = customerService.findByLoyaltyPointsGreaterThan(minPoints);
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-loyalty")
    public ResponseEntity<List<CustomerSummaryDTO>> getTopCustomersByLoyalty(@RequestParam(defaultValue = "10") int limit) {
        List<Customer> customers = customerService.findTopCustomersByLoyaltyPoints(limit);
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomersCreatedBetween(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<Customer> customers = customerService.findCustomersCreatedBetween(startDate, endDate);
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/most-orders")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomersWithMostOrders() {
        List<Customer> customers = customerService.findCustomersWithMostOrders();
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/highest-spending")
    public ResponseEntity<List<CustomerSummaryDTO>> getCustomersWithHighestSpending() {
        List<Customer> customers = customerService.findCustomersWithHighestSpending();
        List<CustomerSummaryDTO> response = customers.stream()
                .map(CustomerSummaryDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar novo cliente", 
               description = "Cadastra um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
        @ApiResponse(responseCode = "409", description = "Email ou telefone já existem"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CustomerDTO> createCustomer(
            @Parameter(description = "Dados do cliente", required = true) 
            @Valid @RequestBody CustomerDTO request) {
        if (customerService.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        if (customerService.existsByPhone(request.phone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setCpf(request.cpf());

        Customer savedCustomer = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerDTO.fromEntity(savedCustomer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO request) {
        return customerService.findById(id)
                .map(customer -> {
                    if (request.name() != null) {
                        customer.setName(request.name());
                    }
                    if (request.email() != null && !customer.getEmail().equals(request.email())) {
                        if (customerService.existsByEmail(request.email())) {
                            throw new RuntimeException("Email already exists");
                        }
                        customer.setEmail(request.email());
                    }
                    if (request.phone() != null && !customer.getPhone().equals(request.phone())) {
                        if (customerService.existsByPhone(request.phone())) {
                            throw new RuntimeException("Phone already exists");
                        }
                        customer.setPhone(request.phone());
                    }
                    if (request.cpf() != null) {
                        customer.setCpf(request.cpf());
                    }

                    Customer updatedCustomer = customerService.updateCustomer(customer);
                    return ResponseEntity.ok(CustomerDTO.fromEntity(updatedCustomer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerDTO> activateCustomer(@PathVariable Long id) {
        try {
            Customer customer = customerService.activateCustomer(id);
            return ResponseEntity.ok(CustomerDTO.fromEntity(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerDTO> deactivateCustomer(@PathVariable Long id) {
        try {
            Customer customer = customerService.deactivateCustomer(id);
            return ResponseEntity.ok(CustomerDTO.fromEntity(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<CustomerDTO> blockCustomer(@PathVariable Long id) {
        try {
            Customer customer = customerService.blockCustomer(id);
            return ResponseEntity.ok(CustomerDTO.fromEntity(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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

    @PatchMapping("/{id}/loyalty-points/redeem")
    public ResponseEntity<CustomerDTO> redeemLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        try {
            Customer customer = customerService.redeemLoyaltyPoints(id, points);
            return ResponseEntity.ok(CustomerDTO.fromEntity(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/loyalty-points/calculate-for-order")
    public ResponseEntity<Integer> calculateLoyaltyPointsForOrder(@RequestParam BigDecimal orderTotal) {
        Integer points = customerService.calculateLoyaltyPointsForOrder(orderTotal);
        return ResponseEntity.ok(points);
    }

    @GetMapping("/loyalty-points/calculate-discount")
    public ResponseEntity<BigDecimal> calculateLoyaltyDiscount(@RequestParam Integer points) {
        BigDecimal discount = customerService.calculateLoyaltyDiscount(points);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/statistics/total-active")
    public ResponseEntity<Long> getTotalActiveCustomers() {
        Long total = customerService.getTotalActiveCustomers();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/statistics/count-by-status")
    public ResponseEntity<Long> getCountByStatus(@RequestParam Customer.CustomerStatus status) {
        Long count = customerService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/average-loyalty-points")
    public ResponseEntity<BigDecimal> getAverageLoyaltyPoints() {
        return customerService.getAverageLoyaltyPoints()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(BigDecimal.ZERO));
    }

    @GetMapping("/statistics/loyalty-distribution")
    public ResponseEntity<List<Object[]>> getLoyaltyPointsDistribution() {
        List<Object[]> distribution = customerService.getLoyaltyPointsDistribution();
        return ResponseEntity.ok(distribution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (customerService.findById(id).isPresent()) {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // ===== GESTÃO DE ENDEREÇOS (seguindo DDD - Address é Value Object de Customer) =====
    
    @GetMapping("/{customerId}/enderecos")
    @Operation(summary = "Listar endereços do cliente", 
               description = "Retorna todos os endereços de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços do cliente"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<AddressDTO>> getCustomerAddresses(
            @Parameter(description = "ID do cliente", required = true) 
            @PathVariable Long customerId) {
        return customerService.findById(customerId)
                .map(customer -> {
                    List<AddressDTO> addresses = customer.getAddresses().stream()
                            .map(AddressDTO::fromEntity)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(addresses);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/{customerId}/enderecos")
    @Operation(summary = "Adicionar endereço ao cliente", 
               description = "Cadastra um novo endereço para o cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados do endereço inválidos")
    })
    public ResponseEntity<AddressDTO> addAddressToCustomer(
            @Parameter(description = "ID do cliente", required = true) 
            @PathVariable Long customerId, 
            @Parameter(description = "Dados do endereço", required = true) 
            @Valid @RequestBody AddressDTO addressDTO) {
        try {
            Address address = customerService.addAddressToCustomer(customerId, addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(AddressDTO.fromEntity(address));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{customerId}/enderecos/{addressId}")
    public ResponseEntity<AddressDTO> updateCustomerAddress(
            @PathVariable Long customerId, 
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDTO addressDTO) {
        try {
            Address address = customerService.updateCustomerAddress(customerId, addressId, addressDTO);
            return ResponseEntity.ok(AddressDTO.fromEntity(address));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{customerId}/enderecos/{addressId}")
    public ResponseEntity<Void> removeAddressFromCustomer(
            @PathVariable Long customerId, 
            @PathVariable Long addressId) {
        try {
            customerService.removeAddressFromCustomer(customerId, addressId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{customerId}/enderecos/{addressId}/set-primary")
    public ResponseEntity<Void> setPrimaryAddress(
            @PathVariable Long customerId, 
            @PathVariable Long addressId) {
        try {
            customerService.setPrimaryAddress(customerId, addressId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}