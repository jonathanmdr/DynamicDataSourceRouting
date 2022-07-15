package br.com.multidatasources.controller.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ApiError {

    private Integer status;
    private OffsetDateTime timestamp;
    private String detail;
    private String userMessage;
    private List<FieldError> fields;

    public ApiError() { }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(final String detail) {
        this.detail = detail;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(final String userMessage) {
        this.userMessage = userMessage;
    }

    public List<FieldError> getFields() {
        return fields;
    }

    public void setFields(final List<FieldError> fields) {
        this.fields = fields;
    }

    public record FieldError(
        String name,
        String userMessage
    ) {

    }

}
