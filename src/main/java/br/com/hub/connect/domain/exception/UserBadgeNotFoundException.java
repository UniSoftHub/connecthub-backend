package br.com.hub.connect.domain.exception;

public class UserBadgeNotFoundException extends RuntimeException {
    public UserBadgeNotFoundException(Long id) {
        super("UserBadge not found with ID: " + id);
    }
}