package br.com.careplus.becare.exception;

/** Lançada quando uma regra de negócio é violada. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
