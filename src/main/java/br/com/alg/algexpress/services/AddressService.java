package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.address.Address;
import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.repository.address.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Address> findByCustomer(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<Address> findByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Address> findByType(Address.AddressType type) {
        return addressRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Address> findByCity(String city) {
        return addressRepository.findByCity(city);
    }

    @Transactional(readOnly = true)
    public List<Address> findByNeighborhood(String neighborhood) {
        return addressRepository.findByNeighborhood(neighborhood);
    }

    @Transactional(readOnly = true)
    public List<Address> findByZipCode(String zipCode) {
        return addressRepository.findByZipCode(zipCode);
    }

    @Transactional(readOnly = true)
    public Optional<Address> findPrimaryAddressByCustomer(Long customerId) {
        return addressRepository.findPrimaryAddressByCustomer(customerId);
    }

    @Transactional(readOnly = true)
    public List<Address> findAddressesInDeliveryZone(String deliveryZone) {
        return addressRepository.findAddressesInDeliveryZone(deliveryZone);
    }

    @Transactional(readOnly = true)
    public List<Address> findMostUsedAddresses() {
        return addressRepository.findMostUsedAddresses();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDeliveryStatisticsByNeighborhood() {
        return addressRepository.findDeliveryStatisticsByNeighborhood();
    }

    public Address save(Address address) {
        // If this is being set as primary, make sure no other address for this customer is primary
        if (address.getIsPrimary() != null && address.getIsPrimary()) {
            setPrimaryAddress(address.getCustomer().getId(), address.getId());
        }
        return addressRepository.save(address);
    }

    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address setPrimaryAddress(Long customerId, Long addressId) {
        // First, remove primary flag from all addresses of this customer
        List<Address> customerAddresses = addressRepository.findByCustomerId(customerId);
        for (Address addr : customerAddresses) {
            addr.setIsPrimary(false);
            addressRepository.save(addr);
        }

        // Then set the specified address as primary
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            if (!address.getCustomer().getId().equals(customerId)) {
                throw new RuntimeException("Address does not belong to the specified customer");
            }
            address.setIsPrimary(true);
            return addressRepository.save(address);
        }
        throw new RuntimeException("Address not found with id: " + addressId);
    }

    public Address updateDeliveryZone(Long addressId, String deliveryZone) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            address.setDeliveryZone(deliveryZone);
            return addressRepository.save(address);
        }
        throw new RuntimeException("Address not found with id: " + addressId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateDeliveryFee(Long addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            
            // Basic delivery fee calculation based on delivery zone
            return switch (address.getDeliveryZone() != null ? address.getDeliveryZone() : "ZONE_1") {
                case "ZONE_1" -> new BigDecimal("5.00");  // Close neighborhood
                case "ZONE_2" -> new BigDecimal("8.00");  // Medium distance
                case "ZONE_3" -> new BigDecimal("12.00"); // Far neighborhood
                default -> new BigDecimal("15.00");       // Very far or special
            };
        }
        throw new RuntimeException("Address not found with id: " + addressId);
    }

    @Transactional(readOnly = true)
    public boolean isAddressInDeliveryArea(Long addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            // Check if the address is in a valid delivery zone
            return address.getDeliveryZone() != null && 
                   !address.getDeliveryZone().equals("NO_DELIVERY");
        }
        return false;
    }

    @Transactional(readOnly = true)
    public String formatFullAddress(Long addressId) {
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            StringBuilder fullAddress = new StringBuilder();
            
            fullAddress.append(address.getStreet());
            if (address.getNumber() != null && !address.getNumber().isEmpty()) {
                fullAddress.append(", ").append(address.getNumber());
            }
            if (address.getComplement() != null && !address.getComplement().isEmpty()) {
                fullAddress.append(" - ").append(address.getComplement());
            }
            fullAddress.append(", ").append(address.getNeighborhood());
            fullAddress.append(", ").append(address.getCity());
            if (address.getState() != null && !address.getState().isEmpty()) {
                fullAddress.append(" - ").append(address.getState());
            }
            fullAddress.append(", CEP: ").append(address.getZipCode());
            
            return fullAddress.toString();
        }
        throw new RuntimeException("Address not found with id: " + addressId);
    }

    @Transactional(readOnly = true)
    public Long countByCustomerId(Long customerId) {
        return addressRepository.countByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Long countByDeliveryZone(String deliveryZone) {
        return addressRepository.countByDeliveryZone(deliveryZone);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getAddressStatisticsByZone() {
        return addressRepository.findAddressStatisticsByZone();
    }

    public void deleteAddress(Long addressId) {
        // Check if it's a primary address before deleting
        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isPresent()) {
            Address address = addressOpt.get();
            if (address.getIsPrimary() != null && address.getIsPrimary()) {
                // If deleting primary address, check if customer has other addresses
                List<Address> customerAddresses = addressRepository.findByCustomerId(address.getCustomer().getId());
                if (customerAddresses.size() > 1) {
                    // Set another address as primary
                    Address newPrimary = customerAddresses.stream()
                            .filter(addr -> !addr.getId().equals(addressId))
                            .findFirst()
                            .orElse(null);
                    if (newPrimary != null) {
                        newPrimary.setIsPrimary(true);
                        addressRepository.save(newPrimary);
                    }
                }
            }
        }
        addressRepository.deleteById(addressId);
    }
}