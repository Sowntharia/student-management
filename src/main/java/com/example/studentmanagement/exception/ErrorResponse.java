package com.example.studentmanagement.exception;

import java.util.Date;
import java.util.Objects;

/**
 * Simple error DTO returned by exception handlers.
 * - status: HTTP-like status code (e.g., 400, 404, 500)
 * - message: human-readable message
 * - timestamp: when the error happened
 */
public class ErrorResponse {

    private int status;
    private String message;
    private Date timestamp;

    /** No-args ctor for frameworks and JSON mappers */
    public ErrorResponse() { }

    /** All-args ctor */
    public ErrorResponse(int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    /** message + status; timestamp defaults to "now" */
    public ErrorResponse(String message, int status) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }

    /** message + timestamp; status defaults to 500 */
    public ErrorResponse(String message, Date timestamp) {
        this.status = 500;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters / Setters

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    // equals / hashCode / toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse other = (ErrorResponse) o;
        return status == other.status
            && Objects.equals(message, other.message)
            && Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, timestamp);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
               "status=" + status +
               ", message='" + message + '\'' +
               ", timestamp=" + timestamp +
               '}';
    }
}
