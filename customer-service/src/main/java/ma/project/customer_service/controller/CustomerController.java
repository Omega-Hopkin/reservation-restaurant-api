package ma.project.customer_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.project.customer_service.StatusCustomer;
import ma.project.customer_service.dto.CreateCustomerRequest;
import ma.project.customer_service.dto.CustomerDto;
import ma.project.customer_service.dto.CustomerStatsDto;
import ma.project.customer_service.dto.UpdateCustomerRequest;
import ma.project.customer_service.model.Customer;
import ma.project.customer_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {
    private final CustomerService customerService;

    //créer un client
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("Création d'un nouveau client : {}", request.getEmail());
        CustomerDto customer = customerService.createCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    //obtenir tous les clients
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        log.info("Récupération de tous les clients");
        List<CustomerDto> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }

    //obtenir un client par id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        log.info("Récupération du client ID : {}", id);
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Obtenir un client par email
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDto> getCustomerByEmail(@PathVariable String email) {
        log.info("Récupération du client par email : {}", email);
        CustomerDto customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

    // Obtenir un client par téléphone
    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<CustomerDto> getCustomerByPhone(@PathVariable String telephone) {
        log.info("Récupération du client par téléphone : {}", telephone);
        CustomerDto customer = customerService.getCustomerByTelephone(telephone);
        return ResponseEntity.ok(customer);
    }

    // Mettre à jour un client
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        log.info("Mise à jour du client : {}", id);
        CustomerDto customer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(customer);
    }

    // Ajouter une réservation à un client
    @PostMapping("/{customerId}/reservations/{reservationId}/restaurant/{restaurantId}")
    public ResponseEntity<CustomerDto> addReservation(
            @PathVariable Long customerId,
            @PathVariable Long reservationId,
            @PathVariable Long restaurantId) {
        log.info("Ajout de la réservation {} au client {}", reservationId, customerId);
        CustomerDto customer = customerService.addReservation(customerId, reservationId, restaurantId);
        return ResponseEntity.ok(customer);
    }

    // Obtenir les statistiques d'un client
    @GetMapping("/{id}/statistiques")
    public ResponseEntity<CustomerStatsDto> getCustomerStats(@PathVariable Long id) {
        log.info("Récupération des statistiques du client : {}", id);
        CustomerStatsDto stats = customerService.getCustomerStats(id);
        return ResponseEntity.ok(stats);
    }

    // Obtenir les clients VIP
    @GetMapping("/vip")
    public ResponseEntity<List<CustomerDto>> getVipCustomers() {
        log.info("Récupération des clients VIP");
        List<CustomerDto> customers = customerService.getVipCustomers();
        return ResponseEntity.ok(customers);
    }

    // Obtenir les top clients
    @GetMapping("/top")
    public ResponseEntity<List<CustomerDto>> getTopCustomers() {
        log.info("Récupération des top clients");
        List<CustomerDto> customers = customerService.getTopCustomers();
        return ResponseEntity.ok(customers);
    }

    // Changer le statut d'un client
    @PutMapping("/{id}/statut/{statut}")
    public ResponseEntity<CustomerDto> changeCustomerStatus(
            @PathVariable Long id,
            @PathVariable StatusCustomer statut) {
        log.info("Changement du statut du client {} à {}", id, statut);
        CustomerDto customer = customerService.changeCustomerStatus(id, statut);
        return ResponseEntity.ok(customer);
    }

    // Vérifier si un client existe
    @GetMapping("/existe/{email}")
    public ResponseEntity<Boolean> customerExists(@PathVariable String email) {
        boolean existe = customerService.customerExists(email);
        return ResponseEntity.ok(existe);
    }

    // Supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Suppression du client : {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}