package ma.project.customer_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.project.customer_service.StatusCustomer;
import ma.project.customer_service.dto.CreateCustomerRequest;
import ma.project.customer_service.dto.CustomerDto;
import ma.project.customer_service.dto.CustomerStatsDto;
import ma.project.customer_service.dto.UpdateCustomerRequest;
import ma.project.customer_service.model.Customer;
import ma.project.customer_service.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import ma.project.customer_service.mapping.CustomerMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    //créer un client
    public CustomerDto createCustomer(CreateCustomerRequest request) {
        log.info("Creation d'un nouveau client : {}", request.getEmail());

        //vérifier que l'email existe déjà
        if (customerRepository.existsByEmail(request.getEmail())) {
            log.error("Echec de la création du client. Email déjà utilisé : {}", request.getEmail());
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        Customer customer = Customer.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .adresse(request.getAdresse())
                .ville(request.getVille())
                .preferences(request.getPreferences())
                .status(StatusCustomer.ACTIF)
                .build();

        customer = customerRepository.save(customer);
        log.info("Client créé avec succès : {}", customer.getId());

        return CustomerMapper.toDto(customer);
    }

    //obtenir tous les clients
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper::toDto)
                .toList();
    }

    //obtenir client par id
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client non trouvé avec l'id : {}", id);
                    return new IllegalArgumentException("Client non trouvé");
                });
        return CustomerMapper.toDto(customer);
    }

    //obtenir client par email
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Client non trouvé avec l'email : {}", email);
                    return new IllegalArgumentException("Client non trouvé");
                });
        return CustomerMapper.toDto(customer);
    }

    //obtenir client par téléphone
    public CustomerDto getCustomerByTelephone(String telephone) {
        Customer customer = customerRepository.findByTelephone(telephone)
                .orElseThrow(() -> {
                    log.error("Client non trouvé avec le téléphone : {}", telephone);
                    return new IllegalArgumentException("Client non trouvé");
                });
        return CustomerMapper.toDto(customer);
    }

    //mettre à jour le client
    public CustomerDto updateCustomer(long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Client non trouvé");
                });

        if (request.getNom() != null) customer.setNom(request.getNom());
        if (request.getPrenom() != null) customer.setPrenom(request.getPrenom());
        if (request.getTelephone() != null) customer.setTelephone(request.getTelephone());
        if (request.getAdresse() != null) customer.setAdresse(request.getAdresse());
        if (request.getVille() != null) customer.setVille(request.getVille());
        if (request.getPreferences() != null) customer.setPreferences(request.getPreferences());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(customer.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
                log.error("Echec de la mise à jour du client. Email déjà utilisé : {}", request.getEmail());
                throw new RuntimeException("Email déjà utilisé");
            }
        }

        customer = customerRepository.save(customer);
        log.info("Client mis à jour avec succès : {}", customer.getId());

        return CustomerMapper.toDto(customer);
    }

    //ajouter une réservation au client
    public CustomerDto addReservation(Long customerId, Long reservationId, Long restaurantId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    return new RuntimeException("Client non trouvé");
                });

        customer.ajouterReservation(reservationId);
        customer.ajouterRestaurantVisite(restaurantId);

        //gérer statut VIP
        if (customer.getPointsFidelite() >= 100 && customer.getStatus() != StatusCustomer.VIP) {
            customer.setStatus(StatusCustomer.VIP);
            log.info("Le client {} est maintenant un client VIP", customer.getId());
        }

        customer = customerRepository.save(customer);
        log.info("Réservation {} ajoutée au client {}", reservationId, customer.getId());

        return CustomerMapper.toDto(customer);
    }

    //obtenir les stats d'un client
    public CustomerStatsDto getCustomerStats(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Client non trouvé");
                });

        return CustomerMapper.toStatsDto(customer);
    }

    //obtenir clients VIP
    public List<CustomerDto> getVipCustomers() {
        return customerRepository.findTopCustomers().stream()
                .map(CustomerMapper::toDto)
                .toList();
    }

    //obtenir les tops clients
    public List<CustomerDto> getTopCustomers() {
        return customerRepository.findTopCustomers().stream()
                .limit(10)
                .map(CustomerMapper::toDto)
                .toList();
    }

    //changer le statut d'un client
    public CustomerDto changeCustomerStatus(Long id, StatusCustomer newStatus) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Client non trouvé");
                });

        customer.setStatus(newStatus);
        customer = customerRepository.save(customer);
        log.info("Statut du client {} changé en {}", customer.getId(), newStatus);

        return CustomerMapper.toDto(customer);
    }

    //supprimer client (Rest IN Peace 💀)
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new IllegalArgumentException("Client non trouvé");
        } else {
            customerRepository.deleteById(id);
            log.info("Client supprimé avec succès : {}", id);
        }

        //autre démarche possible 7ta hia f'l'code :
        /*
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client non trouvé avec l'id : {}", id);
                    return new IllegalArgumentException("Client non trouvé");
                });
        customerRepository.delete(customer);
        log.info("Client supprimé avec succès : {}", id);
         */
    }

    //vérifier si un client existe par email
    public boolean customerExists(String email) {
        return customerRepository.existsByEmail(email);
    }
}
