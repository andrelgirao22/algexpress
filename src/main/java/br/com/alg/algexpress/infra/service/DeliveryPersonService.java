package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import br.com.alg.algexpress.infra.repository.delivery.DeliveryPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryPerson> findById(Long id) {
        return deliveryPersonRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findAll() {
        return deliveryPersonRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findByStatus(DeliveryPerson.DeliveryPersonStatus status) {
        return deliveryPersonRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findAvailableDeliveryPersons() {
        return deliveryPersonRepository.findByAvailable(true);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findActiveDeliveryPersons() {
        return deliveryPersonRepository.findByStatus(DeliveryPerson.DeliveryPersonStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryPerson> findByPhone(String phone) {
        return deliveryPersonRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findByVehicleType(DeliveryPerson.VehicleType vehicleType) {
        return deliveryPersonRepository.findByVehicleType(vehicleType);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findByNameContaining(String name) {
        return deliveryPersonRepository.findByNameContaining(name);
    }

    public DeliveryPerson save(DeliveryPerson deliveryPerson) {
        if (deliveryPerson.getId() == null) {
            deliveryPerson.setShiftStart(LocalDateTime.now());
            deliveryPerson.setStatus(DeliveryPerson.DeliveryPersonStatus.ACTIVE);
        }
        return deliveryPersonRepository.save(deliveryPerson);
    }

    public DeliveryPerson updateDeliveryPerson(DeliveryPerson deliveryPerson) {
        return deliveryPersonRepository.save(deliveryPerson);
    }

    public DeliveryPerson updateStatus(Long deliveryPersonId, DeliveryPerson.DeliveryPersonStatus newStatus) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            deliveryPerson.setStatus(newStatus);
            
            // Set availability based on status
            if (newStatus == DeliveryPerson.DeliveryPersonStatus.ACTIVE) {
                deliveryPerson.setAvailable(true);
            } else if (newStatus == DeliveryPerson.DeliveryPersonStatus.INACTIVE) {
                deliveryPerson.setAvailable(false);
            }
            
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson markAsAvailable(Long deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            deliveryPerson.setAvailable(true);
            deliveryPerson.setStatus(DeliveryPerson.DeliveryPersonStatus.ACTIVE);
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson markAsOnDuty(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.ACTIVE);
    }

    public DeliveryPerson markAsOffDuty(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.INACTIVE);
    }

    public DeliveryPerson markAsUnavailable(Long deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            deliveryPerson.setAvailable(false);
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson incrementTotalDeliveries(Long deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            // Note: totalDeliveries field doesn't exist in DeliveryPerson entity
            // This functionality would need to be tracked separately or added to entity
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson updateRating(Long deliveryPersonId, BigDecimal newRating) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            
            // Note: averageRating field doesn't exist in DeliveryPerson entity
            // This functionality would need to be tracked separately or added to entity
            
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson updateVehicleInfo(Long deliveryPersonId, DeliveryPerson.VehicleType vehicleType, 
                                           String vehiclePlate) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            deliveryPerson.setVehicleType(vehicleType);
            deliveryPerson.setVehiclePlate(vehiclePlate);
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryPerson> findBestAvailableDeliveryPerson() {
        List<DeliveryPerson> available = findAvailableDeliveryPersons();
        
        // Since rating and totalDeliveries don't exist, just return first available
        return available.stream().findFirst();
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findDeliveryPersonsByRatingRange(BigDecimal minRating, BigDecimal maxRating) {
        // Since averageRating doesn't exist, return empty list
        return List.of();
    }

    @Transactional(readOnly = true)
    public Long countByStatus(DeliveryPerson.DeliveryPersonStatus status) {
        return deliveryPersonRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countByVehicleType(DeliveryPerson.VehicleType vehicleType) {
        return deliveryPersonRepository.countByVehicleType(vehicleType);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAverageRatingOfActiveDeliveryPersons() {
        // Since averageRating doesn't exist, return empty
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Integer getTotalDeliveriesByPerson(Long deliveryPersonId) {
        // Since totalDeliveries doesn't exist, return 0
        return 0;
    }

    @Transactional(readOnly = true)
    public boolean isDeliveryPersonAvailable(Long deliveryPersonId) {
        return deliveryPersonRepository.findById(deliveryPersonId)
                .map(DeliveryPerson::getAvailable)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findInactiveDeliveryPersons(int daysInactive) {
        // Since lastActivity doesn't exist, return inactive status delivery persons
        return deliveryPersonRepository.findByStatus(DeliveryPerson.DeliveryPersonStatus.INACTIVE);
    }

    public void activateDeliveryPerson(Long deliveryPersonId) {
        updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.ACTIVE);
    }

    public void deactivateDeliveryPerson(Long deliveryPersonId) {
        updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.INACTIVE);
    }

    public void deleteDeliveryPerson(Long deliveryPersonId) {
        deliveryPersonRepository.deleteById(deliveryPersonId);
    }
}