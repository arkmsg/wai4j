package com.whaleal.ai.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AiException 测试
 */
public class AiExceptionTest {

    @Test
    public void testExceptionWithMessage() {
        AiException exception = new AiException("Test error message");

        assertEquals("Test error message", exception.getMessage());
        assertNull(exception.getCode());
    }

    @Test
    public void testExceptionWithCodeAndMessage() {
        AiException exception = new AiException(401, "Invalid API Key");

        assertEquals(401, exception.getCode());
        assertEquals("Invalid API Key", exception.getMessage());
    }

    @Test
    public void testExceptionWithCause() {
        Throwable cause = new RuntimeException("Cause");
        AiException exception = new AiException(cause);

        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testExceptionWithCodeMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        AiException exception = new AiException(500, "Server error", cause);

        assertEquals(500, exception.getCode());
        assertEquals("Server error", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
