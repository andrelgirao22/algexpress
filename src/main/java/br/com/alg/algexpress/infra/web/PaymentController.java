package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.payment.Payment;
import br.com.alg.algexpress.domain.valueObjects.PaymentMethod;
import br.com.alg.algexpress.dto.payment.PaymentDTO;
import br.com.alg.algexpress.infra.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Pagamentos", description = "API para gerenciamento de pagamentos de pedidos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Buscar pagamentos por pedido", 
               description = "Retorna todos os pagamentos associados a um pedido específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamentos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrder(
            @Parameter(description = "ID do pedido", required = true) 
            @PathVariable Long orderId) {
        List<Payment> payments = paymentService.findByOrderId(orderId);
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pagamento por ID", 
               description = "Retorna os detalhes de um pagamento específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    public ResponseEntity<PaymentDTO> getPaymentById(
            @Parameter(description = "ID do pagamento", required = true) 
            @PathVariable Long id) {
        return paymentService.findById(id)
                .map(payment -> ResponseEntity.ok(PaymentDTO.fromEntity(payment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Processar pagamento", 
               description = "Cria e processa um novo pagamento para um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pagamento processado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro no processamento"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PaymentDTO> processPayment(
            @Parameter(description = "Dados do pagamento", required = true) 
            @Valid @RequestBody PaymentDTO dto) {
        try {
            Payment payment = paymentService.processPayment(dto.orderId(), dto.paymentMethodType(), dto.amount(), dto.amountPaid());
            return ResponseEntity.status(HttpStatus.CREATED).body(PaymentDTO.fromEntity(payment));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/method/{paymentMethodType}")
    @Operation(summary = "Buscar pagamentos por método", 
               description = "Retorna todos os pagamentos de um método específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamentos encontrados com sucesso")
    })
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(
            @Parameter(description = "Tipo de método de pagamento", required = true) 
            @PathVariable PaymentMethod.PaymentType paymentMethodType) {
        List<Payment> payments = paymentService.findByPaymentMethodType(paymentMethodType);
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar pagamentos por status", 
               description = "Retorna todos os pagamentos com um status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamentos encontrados com sucesso")
    })
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(
            @Parameter(description = "Status do pagamento", required = true) 
            @PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.findByStatus(status);
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Aprovar pagamento", 
               description = "Aprova um pagamento pendente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento aprovado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    public ResponseEntity<PaymentDTO> approvePayment(
            @Parameter(description = "ID do pagamento", required = true) 
            @PathVariable Long id) {
        try {
            Payment payment = paymentService.approvePayment(id);
            return ResponseEntity.ok(PaymentDTO.fromEntity(payment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rejeitar pagamento", 
               description = "Rejeita um pagamento com motivo opcional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento rejeitado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    })
    public ResponseEntity<PaymentDTO> rejectPayment(
            @Parameter(description = "ID do pagamento", required = true) 
            @PathVariable Long id, 
            @Parameter(description = "Motivo da rejeição") 
            @RequestParam(required = false) String reason) {
        try {
            Payment payment = paymentService.rejectPayment(id, reason);
            return ResponseEntity.ok(PaymentDTO.fromEntity(payment));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pending")
    @Operation(summary = "Buscar pagamentos pendentes", 
               description = "Retorna todos os pagamentos com status pendente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamentos pendentes encontrados com sucesso")
    })
    public ResponseEntity<List<PaymentDTO>> getPendingPayments() {
        List<Payment> payments = paymentService.findPendingPayments();
        return ResponseEntity.ok(payments.stream().map(PaymentDTO::fromEntity).toList());
    }

    @GetMapping("/statistics/method")
    @Operation(summary = "Estatísticas por método de pagamento", 
               description = "Retorna estatísticas de uso dos métodos de pagamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas obtidas com sucesso")
    })
    public ResponseEntity<List<Object[]>> getPaymentMethodStatistics() {
        List<Object[]> statistics = paymentService.getPaymentMethodStatistics();
        return ResponseEntity.ok(statistics);
    }
}