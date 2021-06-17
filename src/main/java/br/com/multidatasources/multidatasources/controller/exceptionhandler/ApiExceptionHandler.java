package br.com.multidatasources.multidatasources.controller.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected internal error has occurred. "
            + "Try again later, if the problem persists, please contact the system administrator.";

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleStateNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiError error = createApiError(status, ex.getMessage());
        error.setUserMessage(ex.getMessage());

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class) 
    public ResponseEntity<Object> handlerUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError error = createApiError(status, GENERIC_ERROR_MESSAGE);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = "One or more fields are invalid. fill in correctly and try again.";

        List<ApiError.FieldError> fields = bindingResult.getAllErrors()
                .stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return new ApiError.FieldError(name, message);
                })
                .collect(Collectors.toList());

        ApiError error = createApiError(status, detail);
        error.setUserMessage(detail);
        error.setFields(fields);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("The resource '%s' not found.", ex.getRequestURL());

        ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        } else if (rootCause instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) rootCause, headers, status, request);
        }

        ApiError error = createApiError(status, "The request body is invalid. Verify syntax error.");
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(org.springframework.validation.BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("The property '%s' received value '%s', which is an invalid value."
                        + " Inform the compatible value with data type %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());

        ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("Property '%s' not found.", path);

        ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format("The URL parameter '%s' received value '%s', which is an invalid value."
                        + " Inform the compatible value with data type %s.", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ApiError createApiError(HttpStatus status, String detail) {
        ApiError apiError = new ApiError();

        apiError.setStatus(status.value());
        apiError.setDetail(detail);
        apiError.setTimestamp(OffsetDateTime.now());

        return apiError;
    }

    private String joinPath(List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

}
