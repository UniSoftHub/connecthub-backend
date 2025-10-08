package br.com.hub.connect.domain.exception;

public class XpTransactionNotFoundException extends RuntimeException {
    public XpTransactionNotFoundException(Long id) {
        super("XpTransaction not found with ID: " + id);
    }
}
