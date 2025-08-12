package br.com.alg.algexpress.infra.web;

import br.com.alg.algexpress.domain.delivery.Delivery;
import br.com.alg.algexpress.domain.delivery.DeliveryPerson;
import br.com.alg.algexpress.dto.delivery.DeliveryDTO;
import br.com.alg.algexpress.dto.delivery.DeliveryPersonDTO;
import br.com.alg.algexpress.infra.service.DeliveryService;
import br.com.alg.algexpress.infra.service.DeliveryPersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entregas")
@CrossOrigin(origins = "*")
@Tag(name = "Entregas", description = "API para gerenciamento de entregas e entregadores")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryPersonService deliveryPersonService;

    public DeliveryController(DeliveryService deliveryService, DeliveryPersonService deliveryPersonService) {
        this.deliveryService = deliveryService;
        this.deliveryPersonService = deliveryPersonService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.findActiveDeliveries();
        return ResponseEntity.ok(deliveries.stream().map(DeliveryDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        return deliveryService.findById(id)
                .map(delivery -> ResponseEntity.ok(DeliveryDTO.fromEntity(delivery)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByStatus(@PathVariable Delivery.DeliveryStatus status) {
        List<Delivery> deliveries = deliveryService.findByStatus(status);
        return ResponseEntity.ok(deliveries.stream().map(DeliveryDTO::fromEntity).toList());
    }

    @GetMapping("/entregadores")
    public ResponseEntity<List<DeliveryPersonDTO>> getAllDeliveryPersons() {
        List<DeliveryPerson> persons = deliveryPersonService.findAvailableDeliveryPersons();
        return ResponseEntity.ok(persons.stream().map(DeliveryPersonDTO::fromEntity).toList());
    }

    @PostMapping("/entregadores")
    public ResponseEntity<DeliveryPersonDTO> createDeliveryPerson(@Valid @RequestBody DeliveryPersonDTO dto) {
        DeliveryPerson person = dto.toEntity();
        DeliveryPerson saved = deliveryPersonService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeliveryPersonDTO.fromEntity(saved));
    }

    @PatchMapping("/{id}/assign/{deliveryPersonId}")
    public ResponseEntity<DeliveryDTO> assignDeliveryPerson(@PathVariable Long id, @PathVariable Long deliveryPersonId) {
        try {
            Delivery delivery = deliveryService.assignDeliveryPerson(id, deliveryPersonId);
            return ResponseEntity.ok(DeliveryDTO.fromEntity(delivery));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryDTO> updateDeliveryStatus(@PathVariable Long id, @RequestParam Delivery.DeliveryStatus status) {
        try {
            Delivery delivery = deliveryService.updateDeliveryStatus(id, status);
            return ResponseEntity.ok(DeliveryDTO.fromEntity(delivery));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}