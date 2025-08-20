package com.example.studentmanagement.exception;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void noArgsCtor_and_setters() {
        Date ts = new Date();
        ErrorResponse er = new ErrorResponse();
        er.setStatus(404);
        er.setMessage("Not Found");
        er.setTimestamp(ts);

        assertEquals(404, er.getStatus());
        assertEquals("Not Found", er.getMessage());
        assertEquals(ts, er.getTimestamp());
    }

    @Test
    void allArgsCtor_setsAllFields() {
        Date now = new Date();
        ErrorResponse er = new ErrorResponse(500, "Internal Server Error", now);

        assertEquals(500, er.getStatus());
        assertEquals("Internal Server Error", er.getMessage());
        assertEquals(now, er.getTimestamp());
    }

    // (message, status) -> timestamp should be "now"
    @Test
    void ctor_message_status_setsTimestampToNow() {
        long before = System.currentTimeMillis();
        ErrorResponse er = new ErrorResponse("boom", 418);
        long after = System.currentTimeMillis();

        assertEquals(418, er.getStatus());
        assertEquals("boom", er.getMessage());
        assertNotNull(er.getTimestamp());
        long ts = er.getTimestamp().getTime();
        assertTrue(ts >= before && ts <= after, "timestamp should be created by ctor (now)");
    }

    // (message, timestamp) -> status should default to 500 (per your class)
    @Test
    void ctor_message_timestamp_defaultsStatusTo500() {
        Date ts = new Date(1_725_000_000_000L);
        ErrorResponse er = new ErrorResponse("boom", ts);

        assertEquals(500, er.getStatus());   // default in your code
        assertEquals("boom", er.getMessage());
        assertEquals(ts, er.getTimestamp());
    }

    // equals/hashCode branches
    @Test
    void equals_sameInstance_true() {
        ErrorResponse a = new ErrorResponse(400, "X", new Date());
        assertTrue(a.equals(a)); // triggers 'this == o'
    }

    @Test
    void equals_differentType_false() {
        ErrorResponse a = new ErrorResponse(400, "X", new Date());
        assertFalse(a.equals("not-an-error")); // triggers '!(o instanceof ErrorResponse)'
    }

    @Test
    void equals_and_hashCode_allFieldsEqual() {
        Date ts = new Date(1_725_000_000_000L);
        ErrorResponse a = new ErrorResponse(400, "X", ts);
        ErrorResponse b = new ErrorResponse(400, "X", ts);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_false_whenAnyFieldDiffers() {
        Date ts = new Date(1_725_000_000_000L);
        ErrorResponse base = new ErrorResponse(400, "X", ts);

        assertNotEquals(base, new ErrorResponse(401, "X", ts));                  // status differs
        assertNotEquals(base, new ErrorResponse(400, "Y", ts));                  // message differs
        assertNotEquals(base, new ErrorResponse(400, "X", new Date(ts.getTime()+1))); // timestamp differs
    }

    @Test
    void toString_containsFields() {
        Date now = new Date();
        ErrorResponse er = new ErrorResponse(403, "Forbidden", now);

        String out = er.toString();
        assertTrue(out.contains("Forbidden"));
        assertTrue(out.contains("403"));
        assertTrue(out.contains(now.toString()));
    }
}
