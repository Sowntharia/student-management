package com.example.studentmanagement.exception;

import java.util.Date;
import java.util.Objects;

public class ErrorResponse {
    private int status;
    private String message;
    private Date timestamp;

    public ErrorResponse() {
    }
    
    public ErrorResponse(int status, String message, Date timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(String message, int status) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date(); 
    }

    public ErrorResponse(String message, Date timestamp) {
        this.status = 500; 
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() { 
    	return status; 
    	}
    public void setStatus(int status) { 
    	this.status = status; 
    	}

    public String getMessage() { 
    	return message; 
    	}
    public void setMessage(String message) { 
    	this.message = message; 
    	}

    public Date getTimestamp() { 
    	return timestamp; 
    	}
    public void setTimestamp(Date timestamp) { 
    	this.timestamp = timestamp; 
    	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ErrorResponse)) {
            return false;
        }

        ErrorResponse that = (ErrorResponse) o;
        return status == that.status
            && Objects.equals(message, that.message)
            && Objects.equals(timestamp, that.timestamp);
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
