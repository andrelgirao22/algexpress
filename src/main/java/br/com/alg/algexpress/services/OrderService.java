package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.customer.Customer;
import br.com.alg.algexpress.domain.order.Order;
import br.com.alg.algexpress.repository.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findTodaysOrders() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return orderRepository.findTodaysOrders(startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Order> findByType(Order.OrderType type) {
        return orderRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Order> findActiveOrders() {
        return orderRepository.findActiveOrders();
    }

    @Transactional(readOnly = true)
    public List<Order> findActiveDeliveryOrders() {
        return orderRepository.findActiveDeliveryOrders();
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Order> findCustomerOrderHistory(Long customerId) {
        return orderRepository.findCustomerOrderHistory(customerId);
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setOrderDateTime(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);
        }
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(newStatus);
            
            if (newStatus == Order.OrderStatus.CONFIRMED) {
                order.setConfirmationTime(LocalDateTime.now());
            }
            
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    public Order confirmOrder(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.CONFIRMED);
    }

    public Order cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.CANCELLED);
    }

    public Order markAsReady(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.READY);
    }

    public Order markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, Order.OrderStatus.DELIVERED);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.countOrdersBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.sumTotalByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getAverageOrderTotal() {
        return orderRepository.findAverageOrderTotal();
    }

    @Transactional(readOnly = true)
    public List<Order> findByTotalBetween(BigDecimal minTotal, BigDecimal maxTotal) {
        return orderRepository.findByTotalBetween(minTotal, maxTotal);
    }

    @Transactional(readOnly = true)
    public List<Order> findByCustomerIdAndStatus(Long customerId, Order.OrderStatus status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }

    public BigDecimal calculateOrderTotal(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (order.getOrderItems() != null) {
            total = order.getOrderItems().stream()
                    .map(item -> item.getSubtotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        // Add delivery fee if it's a delivery order
        if (order.getType() == Order.OrderType.DELIVERY && order.getDeliveryFee() != null) {
            total = total.add(order.getDeliveryFee());
        }
        
        order.setTotal(total);
        return total;
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}