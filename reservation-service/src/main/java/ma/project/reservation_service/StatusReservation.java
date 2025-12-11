package ma.project.reservation_service;

public enum StatusReservation {
    EN_ATTENTE,      // Réservation créée, en attente de confirmation
    CONFIRMEE,       // Réservation confirmée par le restaurant
    ANNULEE,         // Réservation annulée
    TERMINEE,        // Réservation terminée (client venu)
    NON_PRESENTEE    // Client ne s'est pas présenté
}
