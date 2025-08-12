package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.infra.repository.payment.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findTodaysApprovedPayments() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return paymentRepository.findPaymentsAppovedBetweenDates(startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Payment> findByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public List<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<Payment> findByMethod(Payment.PaymentMethod method) {
        return paymentRepository.findByMethod(method);
    }

    @Transactional(readOnly = true)
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Payment> findPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Payment> findApprovedPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsAppovedBetweenDates(startDate, endDate);
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setPaymentDateTime(LocalDateTime.now());
            payment.setStatus(Payment.PaymentStatus.PENDING);
        }
        return paymentRepository.save(payment);
    }

    public Payment processPayment(Payment payment) {
        payment.setPaymentDateTime(LocalDateTime.now());
        
        // Basic payment processing logic - in real world, this would integrate with payment gateway
        switch (payment.getMethod()) {
            case CASH:
                payment.setStatus(Payment.PaymentStatus.APPROVED);
                break;
            case CREDIT_CARD:
            case DEBIT_CARD:
                // Simulate card processing
                payment.setStatus(Payment.PaymentStatus.APPROVED);
                break;
            case PIX:
                payment.setStatus(Payment.PaymentStatus.APPROVED);
                break;
            default:
                payment.setStatus(Payment.PaymentStatus.REJECTED);
        }
        
        return paymentRepository.save(payment);
    }

    public Payment approvePayment(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.APPROVED);
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found with id: " + paymentId);
    }

    public Payment rejectPayment(Long paymentId, String reason) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.REJECTED);
            // Could add rejection reason field to Payment entity
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found with id: " + paymentId);
    }

    public Payment refundPayment(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
            return paymentRepository.save(payment);
        }
        throw new RuntimeException("Payment not found with id: " + paymentId);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(Long orderId) {
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.APPROVED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateChange(Long orderId, BigDecimal orderTotal) {
        BigDecimal totalPaid = calculateOrderTotal(orderId);
        return totalPaid.subtract(orderTotal);
    }

    @Transactional(readOnly = true)
    public boolean isOrderFullyPaid(Long orderId, BigDecimal orderTotal) {
        BigDecimal totalPaid = calculateOrderTotal(orderId);
        return totalPaid.compareTo(orderTotal) >= 0;
    }

    @Transactional(readOnly = true)
    public Long countByStatus(Payment.PaymentStatus status) {
        return paymentRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countByMethod(Payment.PaymentMethod method) {
        return paymentRepository.countByMethod(method);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.sumApprovedAmountsByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAveragePaymentAmount() {
        return paymentRepository.findAverageApprovedPaymentAmount();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getPaymentMethodStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentMethodStatistics(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Payment> findPendingPayments() {
        return paymentRepository.findByStatus(Payment.PaymentStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<Payment> findFailedPayments() {
        return paymentRepository.findByStatus(Payment.PaymentStatus.REJECTED);
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}