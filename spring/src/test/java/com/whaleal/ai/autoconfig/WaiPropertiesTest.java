package com.whaleal.ai.autoconfig;

import com.whaleal.ai.AiConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WaiProperties 测试
 */
public class WaiPropertiesTest {

    @Test
    public void testDefaultProperties() {
        WaiProperties properties = new WaiProperties();

        assertEquals("deepseek", properties.getDefaultProvider());
        assertTrue(properties.getEnabled());
        assertNotNull(properties.getConfigs());
        assertTrue(properties.getConfigs().isEmpty());
    }

    @Test
    public void testProviderConfig() {
        WaiProperties.ProviderConfig config = new WaiProperties.ProviderConfig();
        config.setApiKey("test-key");
        config.setModel("deepseek-chat");
        config.setBaseUrl("https://api.deepseek.com/v1");
        config.setConnectTimeout(30);
        config.setReadTimeout(60);
        config.setCallTimeout(120);
        config.setEnabled(true);

        assertEquals("test-key", config.getApiKey());
        assertEquals("deepseek-chat", config.getModel());
        assertEquals("https://api.deepseek.com/v1", config.getBaseUrl());
        assertEquals(30, config.getConnectTimeout());
        assertEquals(60, config.getReadTimeout());
        assertEquals(120, config.getCallTimeout());
        assertTrue(config.getEnabled());
    }

    @Test
    public void testProviderConfigExtra() {
        WaiProperties.ProviderConfig config = new WaiProperties.ProviderConfig();
        config.setExtra("key1", "value1");
        config.setExtra("key2", 123);

        assertEquals("value1", config.getExtra().get("key1"));
        assertEquals(123, config.getExtra().get("key2"));
    }
}
