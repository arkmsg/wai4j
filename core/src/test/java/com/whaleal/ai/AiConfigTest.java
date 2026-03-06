package com.whaleal.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AiConfig 测试
 */
public class AiConfigTest {

    @Test
    public void testConfigCreation() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key")
                .setModel("deepseek-chat");

        assertNotNull(config);
        assertEquals("deepseek", config.getProvider());
        assertEquals("test-key", config.getApiKey());
        assertEquals("deepseek-chat", config.getModel());
    }

    @Test
    public void testConfigValidation() {
        AiConfig validConfig = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key");

        AiConfig invalidConfig = AiConfig.create()
                .setProvider("deepseek");

        assertTrue(validConfig.isValid());
        assertFalse(invalidConfig.isValid());
    }

    @Test
    public void testConfigTimeout() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key")
                .setConnectTimeout(30)
                .setReadTimeout(60)
                .setCallTimeout(120);

        assertEquals(30, config.getConnectTimeout());
        assertEquals(60, config.getReadTimeout());
        assertEquals(120, config.getCallTimeout());
    }

    @Test
    public void testConfigExtra() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key");

        java.util.Map<String, Object> extra = new java.util.HashMap<>();
        extra.put("key1", "value1");
        extra.put("key2", 123);
        config.setExtra(extra);

        assertEquals("value1", config.getExtra().get("key1"));
        assertEquals(123, config.getExtra().get("key2"));
    }

    @Test
    public void testStaticFactoryMethod() {
        AiConfig config = AiConfig.of("openai", "test-key");

        assertEquals("openai", config.getProvider());
        assertEquals("test-key", config.getApiKey());
    }
}
