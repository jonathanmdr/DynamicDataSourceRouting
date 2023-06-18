package br.com.multidatasources.api;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class ApiError {

    private Integer status;
    private OffsetDateTime timestamp;
    private String detail;
    private String userMessage;
    private List<FieldError> fields;

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
    ) { }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ApiError apiError = (ApiError) o;
        return Objects.equals(status, apiError.status)
            && Objects.equals(timestamp, apiError.timestamp)
            && Objects.equals(detail, apiError.detail)
            && Objects.equals(userMessage, apiError.userMessage)
            && Objects.equals(fields, apiError.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, timestamp, detail, userMessage, fields);
    }

}
