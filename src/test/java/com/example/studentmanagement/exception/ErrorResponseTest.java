package com.example.studentmanagement.exception;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testGettersAndSetters() {
        Date timestamp = new Date();
        ErrorResponse error = new ErrorResponse();
        error.setStatus(404);
        error.setMessage("Not Found");
        error.setTimestamp(timestamp);

        assertEquals(404, error.getStatus());
        assertEquals("Not Found", error.getMessage());
        assertEquals(timestamp, error.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        Date now = new Date();
        ErrorResponse error = new ErrorResponse(500, "Internal Server Error", now);

        assertEquals(500, error.getStatus());
        assertEquals("Internal Server Error", error.getMessage());
        assertEquals(now, error.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        Date now = new Date();
        ErrorResponse e1 = new ErrorResponse(400, "Bad Request", now);
        ErrorResponse e2 = new ErrorResponse(400, "Bad Request", now);
        ErrorResponse e3 = new ErrorResponse(401, "Unauthorized", now);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1, e3);
    }

    @Test
    void testToStringIncludesFields() {
        Date now = new Date();
        ErrorResponse error = new ErrorResponse(403, "Forbidden", now);

        String output = error.toString();
        assertTrue(output.contains("Forbidden"));
        assertTrue(output.contains("403"));
        assertTrue(output.contains(now.toString()));
    }

    @Test
    void testSimulatedGlobalExceptionHandlerUsage() {
        
        Date now = new Date();
        ErrorResponse simulated = new ErrorResponse(401, "Unauthorized access", now);

        assertNotNull(simulated);
        assertEquals(401, simulated.getStatus());
        assertEquals("Unauthorized access", simulated.getMessage());
        assertEquals(now, simulated.getTimestamp());
    }
}
