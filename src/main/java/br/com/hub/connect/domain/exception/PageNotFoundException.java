
package br.com.hub.connect.domain.exception;

public class PageNotFoundException extends RuntimeException {
  public PageNotFoundException() {
    super("Page number must be >= 1");
  }
}
