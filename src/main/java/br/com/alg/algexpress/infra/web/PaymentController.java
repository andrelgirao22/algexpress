package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.dto.payment.PaymentDTO;
import br.com.alg.algexpress.infra.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagamentos")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrder(@PathVariable Long orderId) {
        List<Payment> payments = paymentService.findByOrderId(orderId);
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(payment -> ResponseEntity.ok(PaymentDTO.fromEntity(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentDTO dto) {
        try {
            Payment payment = paymentService.processPayment(dto.orderId(), dto.method(), dto.amount(), dto.changeFor());
            return ResponseEntity.status(HttpStatus.CREATED).body(PaymentDTO.fromEntity(payment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(@PathVariable Payment.PaymentMethod method) {
        List<Payment> payments = paymentService.findByMethod(method);
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }
}