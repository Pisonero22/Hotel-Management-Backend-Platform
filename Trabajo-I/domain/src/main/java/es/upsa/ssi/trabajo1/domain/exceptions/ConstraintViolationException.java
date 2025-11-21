package es.upsa.ssi.trabajo1.domain.exceptions;

public class ConstraintViolationException extends AppException {
    public ConstraintViolationException(String message) {
        super(message);
    }
}
