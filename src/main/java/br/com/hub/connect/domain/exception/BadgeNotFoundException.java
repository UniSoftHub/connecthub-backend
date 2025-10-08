package br.com.hub.connect.domain.exception;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(Long id) {
        super("Badge not found with  ID: " + id);
    }

}
