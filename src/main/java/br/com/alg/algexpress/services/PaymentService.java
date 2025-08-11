package br.com.alg.algexpress.services;

import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.repository.payment.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findTodaysApprovedPayments() {
        // Pega a data de hoje e define a hora para o início do dia (00:00:00)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        // Pega a data de hoje, adiciona 1 dia e define a hora para o início do dia (para criar um intervalo exclusivo)
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        return paymentRepository.findPaymentsAppovedBetweenDates(startOfDay, endOfDay);
    }
}
