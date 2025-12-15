# reservation-restaurant-api
Application de réservation de restaurants développée avec Spring Boot intégrant les microservices (restaurant-service, reservation-service) et les clients REST (FeignClient, WebClient). Permet aux utilisateurs de trouver, réserver et noter des restaurants grâce à la géolocalisation et aux avis.

## 🧩 Architecture

Voici une vue globale du système.

![Architecture microservices](images/architecture.png)

Les services communiquent via **OpenFeign** et sont enregistrés auprès d’Eureka. Le routing externe passe par **Spring Cloud Gateway**.

---

## 🏗️ Microservices Breakdown

### **1. Discovery Service (Eureka Server)**

* **Port** : `8761`
* URL : `http://localhost:8761/`

### **2. Gateway Service**

* **Port** : `8091`
* Route automatiquement les requêtes vers les microservices en fonction de leur `serviceId`.
* Intègre les endpoints actuator : `/actuator/gateway/routes`.

Points clés :

* Basé sur `spring-cloud-gateway`
* Dépend de Eureka pour le service discovery

### **3. Config Server**

* Centralise la configuration.
* Pointe vers un repo Git distant privé.


### **4. Restaurant Service**

* Gère : restaurants, capacité, horaires.
* Communique avec Customer Service et Reservation Service via Feign.
* **Port** : `8081`

### **5. Customer Service**


### **6. Reservation Service**

* Gère les réservations
* Parle avec restaurant-service + customer-service via Feign
* Utilise Resilience4J (circuit breaker + fallback)


---

## ⚙️ Technologies

* **Java 21**
* **Spring Boot 3.3.x**
* **Spring Cloud 2025.0.0**
* **Spring Cloud Gateway**
* **Spring Cloud OpenFeign**
* **Spring Cloud Config**
* **Eureka Discovery Server**
* **Resilience4J** (circuit breaker)
* **Hibernate / JPA / MySQL**
* **Lombok**
* **Maven**

---

## 📦 Project Structure

```
restaurant-reservation-system/
│
├── discovery-service/
├── config-server/
├── gateway-service/
├── restaurant-service/
├── customer-service/
└── reservation-service/
```

Tous les services utilisent :

```
src/main/resources/application.yml
```

---

## 🔧 Installation & Execution

### 📌 Prérequis

* JDK 21+
* Maven 3.8+
* MySQL 8+

### 🛠️ 1. Cloner le projet

```
git clone [https://github.com/your/repo.git](https://github.com/Omega-Hopkin/reservation-restaurant-api)
cd restaurant-reservation-api
```

### 🛠️ 2. Lancer les services **dans cet ordre**

#### 1) Config Server

```
cd config-server
mvn spring-boot:run
```

#### 2) Discovery Service

```
cd discovery-service
mvn spring-boot:run
```

#### 3) Gateway

```
cd gateway-service
mvn spring-boot:run
```

#### 4) Restaurant Service

```
cd restaurant-service
mvn spring-boot:run
```

#### 5) Customer Service

```
cd customer-service
mvn spring-boot:run
```

#### 6) Reservation Service

```
cd reservation-service
mvn spring-boot:run
```

---

## 🗄️ Database Setup

Chaque service propriétaire d’une base :

* api_restaurant
* api_customer
* api_reservation

---

## 🧪 Test avec Postman

1. Gateway : `http://localhost:8091/`
2. Pour chaque service :

   * Via Gateway : `/restaurant/...`
   * Direct : `http://localhost:8081/...`

---

## 🛡️ Resilience4J Example

```java
@CircuitBreaker(name = "restaurantService", fallbackMethod = "fallback")
public ReservationDto createReservation(Request req) {
    return ...
}
```

Fallback :

```java
public ReservationDto fallback(Request req, Exception e) {
    throw new RuntimeException("Restaurant service unavailable");
}
```

---

## 📚 Roadmap

* [ ] Ajouter Keycloak pour auth
* [ ] Ajouter Grafana + Prometheus
* [ ] Dockerisation
* [ ] Tests unitaires & intégration
* [ ] UI React

---
