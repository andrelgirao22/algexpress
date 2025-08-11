package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import br.com.alg.algexpress.repository.delivery.DeliveryPersonRepository;
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
        return deliveryPersonRepository.findByStatus(DeliveryPerson.DeliveryPersonStatus.AVAILABLE);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findActiveDeliveryPersons() {
        return deliveryPersonRepository.findByStatus(DeliveryPerson.DeliveryPersonStatus.ON_DUTY);
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

    @Transactional(readOnly = true)
    public List<Object[]> findDeliveryPersonPerformanceStats() {
        return deliveryPersonRepository.findDeliveryPersonPerformanceStats();
    }

    @Transactional(readOnly = true)
    public List<Object[]> findTopPerformersLastMonth() {
        return deliveryPersonRepository.findTopPerformersLastMonth();
    }

    public DeliveryPerson save(DeliveryPerson deliveryPerson) {
        if (deliveryPerson.getId() == null) {
            deliveryPerson.setHireDate(LocalDateTime.now());
            deliveryPerson.setStatus(DeliveryPerson.DeliveryPersonStatus.OFF_DUTY);
            deliveryPerson.setTotalDeliveries(0);
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
            
            // Update last activity timestamp
            if (newStatus == DeliveryPerson.DeliveryPersonStatus.ON_DUTY ||
                newStatus == DeliveryPerson.DeliveryPersonStatus.AVAILABLE) {
                deliveryPerson.setLastActivity(LocalDateTime.now());
            }
            
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson markAsAvailable(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.AVAILABLE);
    }

    public DeliveryPerson markAsOnDuty(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.ON_DUTY);
    }

    public DeliveryPerson markAsOffDuty(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.OFF_DUTY);
    }

    public DeliveryPerson markAsUnavailable(Long deliveryPersonId) {
        return updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.UNAVAILABLE);
    }

    public DeliveryPerson incrementTotalDeliveries(Long deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            deliveryPerson.setTotalDeliveries(deliveryPerson.getTotalDeliveries() + 1);
            deliveryPerson.setLastActivity(LocalDateTime.now());
            return deliveryPersonRepository.save(deliveryPerson);
        }
        throw new RuntimeException("Delivery person not found with id: " + deliveryPersonId);
    }

    public DeliveryPerson updateRating(Long deliveryPersonId, BigDecimal newRating) {
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOpt.get();
            
            // Calculate new average rating
            if (deliveryPerson.getAverageRating() == null) {
                deliveryPerson.setAverageRating(newRating);
            } else {
                // Simple averaging - in real world, you'd track number of ratings
                BigDecimal currentRating = deliveryPerson.getAverageRating();
                BigDecimal averageRating = currentRating.add(newRating).divide(new BigDecimal("2"));
                deliveryPerson.setAverageRating(averageRating);
            }
            
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
        
        return available.stream()
                .filter(dp -> dp.getAverageRating() != null)
                .max((dp1, dp2) -> {
                    // First compare by rating, then by total deliveries (experience)
                    int ratingComparison = dp1.getAverageRating().compareTo(dp2.getAverageRating());
                    if (ratingComparison != 0) {
                        return ratingComparison;
                    }
                    return dp1.getTotalDeliveries().compareTo(dp2.getTotalDeliveries());
                });
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findDeliveryPersonsByRatingRange(BigDecimal minRating, BigDecimal maxRating) {
        return deliveryPersonRepository.findAll().stream()
                .filter(dp -> dp.getAverageRating() != null)
                .filter(dp -> dp.getAverageRating().compareTo(minRating) >= 0)
                .filter(dp -> dp.getAverageRating().compareTo(maxRating) <= 0)
                .toList();
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
        List<DeliveryPerson> activeDeliveryPersons = findByStatus(DeliveryPerson.DeliveryPersonStatus.ON_DUTY);
        
        if (activeDeliveryPersons.isEmpty()) {
            return Optional.empty();
        }
        
        BigDecimal totalRating = activeDeliveryPersons.stream()
                .filter(dp -> dp.getAverageRating() != null)
                .map(DeliveryPerson::getAverageRating)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long countWithRating = activeDeliveryPersons.stream()
                .filter(dp -> dp.getAverageRating() != null)
                .count();
        
        if (countWithRating == 0) {
            return Optional.empty();
        }
        
        return Optional.of(totalRating.divide(new BigDecimal(countWithRating)));
    }

    @Transactional(readOnly = true)
    public Integer getTotalDeliveriesByPerson(Long deliveryPersonId) {
        return deliveryPersonRepository.findById(deliveryPersonId)
                .map(DeliveryPerson::getTotalDeliveries)
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public boolean isDeliveryPersonAvailable(Long deliveryPersonId) {
        return deliveryPersonRepository.findById(deliveryPersonId)
                .map(dp -> dp.getStatus() == DeliveryPerson.DeliveryPersonStatus.AVAILABLE)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPerson> findInactiveDeliveryPersons(int daysInactive) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysInactive);
        
        return deliveryPersonRepository.findAll().stream()
                .filter(dp -> dp.getLastActivity() != null)
                .filter(dp -> dp.getLastActivity().isBefore(cutoffDate))
                .toList();
    }

    public void activateDeliveryPerson(Long deliveryPersonId) {
        updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.AVAILABLE);
    }

    public void deactivateDeliveryPerson(Long deliveryPersonId) {
        updateStatus(deliveryPersonId, DeliveryPerson.DeliveryPersonStatus.UNAVAILABLE);
    }

    public void deleteDeliveryPerson(Long deliveryPersonId) {
        deliveryPersonRepository.deleteById(deliveryPersonId);
    }
}