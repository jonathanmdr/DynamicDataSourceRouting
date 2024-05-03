package br.com.multidatasources.exception;

public class BusinessException extends RuntimeException{

    private static final boolean ENABLE_SUPPRESSION = true;
    private static final boolean WRITEABLE_STACK_TRACE = false;

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final String message, final Throwable cause) {
        super(message, cause, ENABLE_SUPPRESSION, WRITEABLE_STACK_TRACE);
    }

}
