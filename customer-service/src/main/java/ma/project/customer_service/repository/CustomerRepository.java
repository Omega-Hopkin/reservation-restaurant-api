package ma.project.customer_service.repository;

import ma.project.customer_service.StatusCustomer;
import ma.project.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByTelephone(String telephone);
    List<Customer> findByStatus(StatusCustomer status);
    List<Customer> findByVille(String ville);
    boolean existsByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.pointsFidelite >= :points")
    List<Customer> findCustomersWithMinPoints(Integer points);

    @Query("SELECT c FROM Customer c ORDER BY c.nombreReservations DESC")
    List<Customer> findTopCustomers();

    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIF' ORDER BY c.pointsFidelite DESC")
    List<Customer> findVIPCustomers();
}