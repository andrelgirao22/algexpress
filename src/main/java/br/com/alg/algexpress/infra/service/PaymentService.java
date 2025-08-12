package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.domain.valueObjects.Money;
import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;
import br.com.alg.algexpress.infra.repository.payment.PaymentRepository;
import br.com.alg.algexpress.infra.repository.order.OrderRepository;
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
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public List<Payment> findTodaysApprovedPayments() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return paymentRepository.findTodaysApprovedPayments(startOfDay);
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
    public List<Payment> findByPaymentMethodType(PaymentMethod.PaymentType paymentMethodType) {
        return paymentRepository.findByPaymentMethodType(paymentMethodType);
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
        if (!payment.canBeProcessed()) {
            throw new IllegalArgumentException("Payment cannot be processed");
        }
        
        payment.setPaymentDateTime(LocalDateTime.now());
        
        // Basic payment processing logic - in real world, this would integrate with payment gateway
        if (payment.getPaymentMethod() != null) {
            switch (payment.getPaymentMethod().getType()) {
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
        } else {
            payment.setStatus(Payment.PaymentStatus.REJECTED);
        }
        
        return paymentRepository.save(payment);
    }

    public Payment processPayment(Long orderId, PaymentMethod.PaymentType paymentMethodType, BigDecimal amount, BigDecimal amountPaid) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(new PaymentMethod(paymentMethodType));
        payment.setAmount(Money.of(amount));
        
        if (amountPaid != null) {
            payment.setAmountPaid(Money.of(amountPaid));
        }
        
        return processPayment(payment);
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
        Optional<BigDecimal> total = paymentRepository.sumApprovedPaymentsByOrderId(orderId);
        return total.orElse(BigDecimal.ZERO);
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
    public Long countByPaymentMethodType(PaymentMethod.PaymentType paymentMethodType) {
        List<Payment> payments = paymentRepository.findByPaymentMethodType(paymentMethodType);
        return (long) payments.size();
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.sumApprovedPaymentsByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAveragePaymentAmount() {
        return paymentRepository.findAverageApprovedPaymentAmount();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getPaymentMethodStatistics() {
        return paymentRepository.findPaymentMethodStatistics();
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