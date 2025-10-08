package br.com.hub.connect.domain.exception;

public class UserAlreadyHasBadgeException extends RuntimeException {
    public UserAlreadyHasBadgeException(Long userId, Long badgeId) {
        super("User " + userId + " already has badge " + badgeId);
    }
}