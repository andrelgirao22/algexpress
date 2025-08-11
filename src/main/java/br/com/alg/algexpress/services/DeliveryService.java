package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.delivery.Delivery;
import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.repository.delivery.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<Delivery> findTodaysDeliveries() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return deliveryRepository.findTodaysDeliveries(startOfDay, endOfDay);
    }

    public Long countTodaysDeliveriesByPerson(Long deliveryPersonId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return deliveryRepository.countTodaysDeliveriesByPerson(deliveryPersonId, startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Delivery> findByOrder(Order order) {
        return deliveryRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public Optional<Delivery> findByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<Delivery> findByDeliveryPerson(DeliveryPerson deliveryPerson) {
        return deliveryRepository.findByDeliveryPerson(deliveryPerson);
    }

    @Transactional(readOnly = true)
    public List<Delivery> findByDeliveryPersonId(Long deliveryPersonId) {
        return deliveryRepository.findByDeliveryPersonId(deliveryPersonId);
    }

    @Transactional(readOnly = true)
    public List<Delivery> findByStatus(Delivery.DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Delivery> findActiveDeliveries() {
        return deliveryRepository.findActiveDeliveries();
    }

    @Transactional(readOnly = true)
    public List<Delivery> findDeliveriesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.findDeliveriesBetweenDates(startDate, endDate);
    }

    public Delivery save(Delivery delivery) {
        if (delivery.getId() == null) {
            delivery.setStatus(Delivery.DeliveryStatus.WAITING_DELIVERY_PERSON);
            delivery.setDeliveryAttempts(0);
        }
        return deliveryRepository.save(delivery);
    }

    public Delivery assignDeliveryPerson(Long deliveryId, Long deliveryPersonId) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            // Note: You would need to inject DeliveryPersonRepository to get the DeliveryPerson
            // For now, we'll just set the status
            delivery.setStatus(Delivery.DeliveryStatus.EN_ROUTE);
            delivery.setDepartureTime(LocalDateTime.now());
            return deliveryRepository.save(delivery);
        }
        throw new RuntimeException("Delivery not found with id: " + deliveryId);
    }

    public Delivery updateDeliveryStatus(Long deliveryId, Delivery.DeliveryStatus newStatus) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            delivery.setStatus(newStatus);
            
            switch (newStatus) {
                case EN_ROUTE:
                    if (delivery.getDepartureTime() == null) {
                        delivery.setDepartureTime(LocalDateTime.now());
                    }
                    break;
                case DELIVERED:
                    delivery.setDeliveryTime(LocalDateTime.now());
                    break;
                case DELIVERY_ATTEMPT:
                    delivery.setDeliveryAttempts(delivery.getDeliveryAttempts() + 1);
                    break;
                case CANCELLED:
                    // Could set cancellation reason here
                    break;
            }
            
            return deliveryRepository.save(delivery);
        }
        throw new RuntimeException("Delivery not found with id: " + deliveryId);
    }

    public Delivery markAsEnRoute(Long deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery.DeliveryStatus.EN_ROUTE);
    }

    public Delivery markAsDelivered(Long deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery.DeliveryStatus.DELIVERED);
    }

    public Delivery markAsDeliveryAttempt(Long deliveryId) {
        return updateDeliveryStatus(deliveryId, Delivery.DeliveryStatus.DELIVERY_ATTEMPT);
    }

    public Delivery cancelDelivery(Long deliveryId, String reason) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            delivery.setStatus(Delivery.DeliveryStatus.CANCELLED);
            delivery.setCancellationReason(reason);
            return deliveryRepository.save(delivery);
        }
        throw new RuntimeException("Delivery not found with id: " + deliveryId);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(Delivery.DeliveryStatus status) {
        return deliveryRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countDeliveredByDeliveryPerson(Long deliveryPersonId) {
        return deliveryRepository.countDeliveredByDeliveryPerson(deliveryPersonId);
    }

    @Transactional(readOnly = true)
    public Optional<Double> getAverageDeliveryTimeMinutes() {
        return deliveryRepository.findAverageDeliveryTimeMinutes();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getDeliveryPersonPerformance(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.findDeliveryPersonPerformance(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getTotalDeliveryFees(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryRepository.sumDeliveryFeesByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAverageDeliveryDistance() {
        return deliveryRepository.findAverageDeliveryDistance();
    }

    @Transactional(readOnly = true)
    public List<Delivery> findDeliveriesWithMultipleAttempts(Integer attempts) {
        return deliveryRepository.findDeliveriesWithMultipleAttempts(attempts);
    }

    @Transactional(readOnly = true)
    public List<Delivery> findCancelledDeliveriesWithReason() {
        return deliveryRepository.findCancelledDeliveriesWithReason();
    }

    public BigDecimal calculateDeliveryFee(BigDecimal distanceKm) {
        // Basic delivery fee calculation - can be made more sophisticated
        BigDecimal baseRate = new BigDecimal("5.00"); // Base rate
        BigDecimal perKmRate = new BigDecimal("1.50"); // Per km rate
        
        if (distanceKm != null && distanceKm.compareTo(BigDecimal.ZERO) > 0) {
            return baseRate.add(distanceKm.multiply(perKmRate));
        }
        
        return baseRate;
    }

    public void deleteDelivery(Long deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }
}