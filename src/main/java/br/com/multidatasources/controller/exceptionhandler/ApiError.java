package br.com.multidatasources.controller.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public List<FieldError> getFields() {
        return fields;
    }

    public void setFields(List<FieldError> fields) {
        this.fields = fields;
    }

    public static class FieldError {

        private String name;
        private String userMessage;
        
        public FieldError(String name, String userMessage) {
            this.name = name;
            this.userMessage = userMessage;
        }

        public String getName() {
            return name;
        }

        public String getUserMessage() {
            return userMessage;
        }
    }

}
