package br.com.careplus.becare.exception;

/** Lançada quando um recurso não é encontrado no banco de dados. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(String.format("%s com ID %d não encontrado.", resource, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
