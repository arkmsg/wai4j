package com.whaleal.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Message 测试
 */
public class MessageTest {

    @Test
    public void testMessageCreation() {
        Message message = new Message("user", "Hello");

        assertEquals("user", message.getRole());
        assertEquals("Hello", message.getContent());
    }

    @Test
    public void testStaticFactoryMethods() {
        Message system = Message.system("System message");
        Message user = Message.user("User message");
        Message assistant = Message.assistant("Assistant message");

        assertEquals("system", system.getRole());
        assertEquals("System message", system.getContent());
        
        assertEquals("user", user.getRole());
        assertEquals("User message", user.getContent());
        
        assertEquals("assistant", assistant.getRole());
        assertEquals("Assistant message", assistant.getContent());
    }

    @Test
    public void testMessageRoleCheck() {
        Message system = Message.system("test");
        Message user = Message.user("test");
        Message assistant = Message.assistant("test");

        assertTrue(system.isSystem());
        assertFalse(system.isUser());
        assertFalse(system.isAssistant());

        assertFalse(user.isSystem());
        assertTrue(user.isUser());
        assertFalse(user.isAssistant());

        assertFalse(assistant.isSystem());
        assertFalse(assistant.isUser());
        assertTrue(assistant.isAssistant());
    }
}
