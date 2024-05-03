package br.com.multidatasources.exception;

public class JsonProcessingException extends BusinessException{

    public JsonProcessingException(final String message, final Throwable cause){
        super(message, cause);
    }

}
