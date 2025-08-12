package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.valueObjects.Address;
import br.com.alg.algexpress.dto.customer.AddressDTO;
import br.com.alg.algexpress.infra.repository.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Customer> findActiveCustomers() {
        return customerRepository.findByStatus(Customer.CustomerStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByNameContaining(String name) {
        return customerRepository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public List<Customer> findByLoyaltyPointsGreaterThan(Integer points) {
        return customerRepository.findByLoyaltyPointsGreaterThan(points);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return customerRepository.findCustomersCreatedBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersWithMostOrders() {
        return customerRepository.findCustomersWithMostOrders();
    }

    @Transactional(readOnly = true)
    public List<Customer> findCustomersWithHighestSpending() {
        return customerRepository.findCustomersWithHighestSpending();
    }

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setRegistrationDate(LocalDateTime.now());
            customer.setStatus(Customer.CustomerStatus.ACTIVE);
            customer.setLoyaltyPoints(0);
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer activateCustomer(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setStatus(Customer.CustomerStatus.ACTIVE);
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public Customer deactivateCustomer(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setStatus(Customer.CustomerStatus.INACTIVE);
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public Customer blockCustomer(Long customerId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setStatus(Customer.CustomerStatus.BLOCKED);
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public Customer addLoyaltyPoints(Long customerId, Integer points) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
            return customerRepository.save(customer);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    public Customer redeemLoyaltyPoints(Long customerId, Integer points) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getLoyaltyPoints() >= points) {
                customer.setLoyaltyPoints(customer.getLoyaltyPoints() - points);
                return customerRepository.save(customer);
            } else {
                throw new RuntimeException("Insufficient loyalty points. Customer has: " + 
                    customer.getLoyaltyPoints() + ", trying to redeem: " + points);
            }
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }

    @Transactional(readOnly = true)
    public Integer calculateLoyaltyPointsForOrder(BigDecimal orderTotal) {
        // 1 point for every 10 reais spent
        return orderTotal.divide(new BigDecimal("10")).intValue();
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateLoyaltyDiscount(Integer points) {
        // 1 point = 0.10 reais discount
        return new BigDecimal(points).multiply(new BigDecimal("0.10"));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(Customer.CustomerStatus status) {
        return customerRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long getTotalActiveCustomers() {
        return customerRepository.countByStatus(Customer.CustomerStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAverageLoyaltyPoints() {
        return customerRepository.findAverageLoyaltyPoints();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getLoyaltyPointsDistribution() {
        return customerRepository.findLoyaltyPointsDistribution();
    }

    @Transactional(readOnly = true)
    public List<Customer> findTopCustomersByLoyaltyPoints(int limit) {
        List<Customer> customers = customerRepository.findByLoyaltyPointsGreaterThan(0);
        return customers.stream()
                .sorted((c1, c2) -> c2.getLoyaltyPoints().compareTo(c1.getLoyaltyPoints()))
                .limit(limit)
                .toList();
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
    
    // ===== ADDRESS MANAGEMENT METHODS =====
    
    @Transactional(readOnly = true)
    public Optional<Address> findAddressById(Long addressId) {
        List<Customer> customers = customerRepository.findAll();
        
        for (Customer customer : customers) {
            if (customer.getAddresses() != null) {
                for (Address address : customer.getAddresses()) {
                    if (address.getId().equals(addressId)) {
                        return Optional.of(address);
                    }
                }
            }
        }
        
        return Optional.empty();
    }
    
    public Address addAddressToCustomer(Long customerId, AddressDTO addressDTO) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            Address address = new Address();
            address.setCustomer(customer);
            address.setStreet(addressDTO.street());
            address.setNumber(addressDTO.number());
            address.setComplement(addressDTO.complement());
            address.setNeighborhood(addressDTO.neighborhood());
            address.setCity(addressDTO.city());
            address.setState(addressDTO.state());
            address.setZipCode(addressDTO.zipCode());
            address.setType(addressDTO.type());
            address.setReferencePoints(addressDTO.referencePoints());
            address.setType(addressDTO.type() != null ? addressDTO.type() : Address.AddressType.RESIDENTIAL);
            
            // Initialize addresses list if null
            if (customer.getAddresses() == null) {
                customer.setAddresses(new ArrayList<>());
            }
            
            customer.getAddresses().add(address);
            Customer savedCustomer = customerRepository.save(customer);
            
            // Return the newly added address (last one in the list)
            List<Address> addresses = savedCustomer.getAddresses();
            return addresses.get(addresses.size() - 1);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }
    
    public Address updateCustomerAddress(Long customerId, Long addressId, AddressDTO addressDTO) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            if (customer.getAddresses() != null) {
                for (Address address : customer.getAddresses()) {
                    if (address.getId().equals(addressId)) {
                        address.setStreet(addressDTO.street());
                        address.setNumber(addressDTO.number());
                        address.setComplement(addressDTO.complement());
                        address.setNeighborhood(addressDTO.neighborhood());
                        address.setCity(addressDTO.city());
                        address.setState(addressDTO.state());
                        address.setZipCode(addressDTO.zipCode());
                        address.setType(addressDTO.type());
                        address.setReferencePoints(addressDTO.referencePoints());
            address.setType(addressDTO.type() != null ? addressDTO.type() : Address.AddressType.RESIDENTIAL);
                        
                        customerRepository.save(customer);
                        return address;
                    }
                }
            }
            throw new RuntimeException("Address not found with id: " + addressId + " for customer: " + customerId);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }
    
    public void removeAddressFromCustomer(Long customerId, Long addressId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            if (customer.getAddresses() != null) {
                boolean removed = customer.getAddresses().removeIf(address -> address.getId().equals(addressId));
                if (removed) {
                    customerRepository.save(customer);
                    return;
                }
            }
            throw new RuntimeException("Address not found with id: " + addressId + " for customer: " + customerId);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }
    
    public void setPrimaryAddress(Long customerId, Long addressId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            if (customer.getAddresses() != null) {
                boolean found = false;
                for (Address address : customer.getAddresses()) {
                    if (address.getId().equals(addressId)) {
                        // Set this address as primary and others as non-primary
                        address.setPrimary(true);
                        found = true;
                    } else {
                        address.setPrimary(false);
                    }
                }
                
                if (found) {
                    customerRepository.save(customer);
                    return;
                }
            }
            throw new RuntimeException("Address not found with id: " + addressId + " for customer: " + customerId);
        }
        throw new RuntimeException("Customer not found with id: " + customerId);
    }
}