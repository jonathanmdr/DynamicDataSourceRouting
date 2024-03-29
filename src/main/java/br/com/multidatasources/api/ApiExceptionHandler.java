package br.com.multidatasources.api;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String GENERIC_ERROR_MESSAGE = "An unexpected internal error has occurred. Try again later, if the problem persists, please contact the system administrator.";

    private final MessageSource messageSource;

    public ApiExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleStateNotFoundException(final EntityNotFoundException ex, final WebRequest request) {
        final HttpStatus status = HttpStatus.NOT_FOUND;

        final ApiError error = createApiError(status, ex.getMessage());
        error.setUserMessage(ex.getMessage());

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Object> handleStateExistsException(final EntityExistsException ex, final WebRequest request) {
        final HttpStatus status = HttpStatus.CONFLICT;

        final ApiError error = createApiError(status, ex.getMessage());
        error.setUserMessage(ex.getMessage());

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class) 
    protected ResponseEntity<Object> handlerUncaught(final Exception ex, final WebRequest request) {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        final ApiError error = createApiError(status, GENERIC_ERROR_MESSAGE);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final String detail = "The resource '%s' not found.".formatted(ex.getRequestURL());

        final ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormatException(invalidFormatException, headers, status, request);
        }

        if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBindingException(propertyBindingException, headers, status, request);
        }

        if (rootCause instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException, headers, status, request);
        }

        final ApiError error = createApiError(status, "The request body is invalid. Verify syntax error.");
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(final InvalidFormatException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final String path = joinPath(ex.getPath());
        final String detail = "The property '%s' received value '%s', which is an invalid value. Inform the compatible value with data type %s."
            .formatted(
                path,
                ex.getValue(),
                ex.getTargetType().getSimpleName()
            );

        final ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(final PropertyBindingException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final String path = joinPath(ex.getPath());
        final String detail = "Property '%s' not found.".formatted(path);

        final ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final String detail = "The URL parameter '%s' received value '%s', which is an invalid value. Inform the compatible value with data type %s."
            .formatted(
                ex.getName(),
                ex.getValue(),
                Objects.requireNonNull(ex.getRequiredType()).getSimpleName()
            );

        final ApiError error = createApiError(status, detail);
        error.setUserMessage(GENERIC_ERROR_MESSAGE);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(final Exception ex, final BindingResult bindingResult, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        String detail = "One or more fields are invalid. fill in correctly and try again.";

        final List<ApiError.FieldError> fields = bindingResult.getAllErrors()
                .stream()
                .map(objectError -> {
                    final String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }

                    return new ApiError.FieldError(name, message);
                })
                .toList();

        final ApiError error = createApiError(status, detail);
        error.setUserMessage(detail);
        error.setFields(fields);

        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ApiError createApiError(final HttpStatusCode status, final String detail) {
        final ApiError apiError = new ApiError();

        apiError.setStatus(status.value());
        apiError.setDetail(detail);
        apiError.setTimestamp(OffsetDateTime.now());

        return apiError;
    }

    private String joinPath(final List<Reference> references) {
        return references.stream()
                .map(Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

}
