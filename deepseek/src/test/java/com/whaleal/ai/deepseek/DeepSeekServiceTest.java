package com.whaleal.ai.deepseek;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.Message;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DeepSeekService 测试
 */
public class DeepSeekServiceTest {

    private DeepSeekService service;

    @BeforeEach
    public void setUp() {
        service = new DeepSeekService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("deepseek"));
        assertTrue(service.support("DEEPSEEK"));
        assertFalse(service.support("openai"));
    }

    @Test
    public void testGetService() {
        AiService aiService = service.getService();
        assertNotNull(aiService);
        assertSame(service, aiService);
    }

    @Test
    public void testChatWithoutApiKey() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("")
                .setModel("deepseek-chat");

        assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });
    }

    @Test
    public void testChatWithMessages() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key");

        List<Message> messages = Arrays.asList(
                Message.system("You are a helpful assistant"),
                Message.user("Hello")
        );

        // 由于没有真实的 API Key，这里会抛出异常
        assertThrows(AiException.class, () -> {
            service.chat(config, messages);
        });
    }

    @Test
    public void testConfigWithCustomBaseUrl() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key")
                .setBaseUrl("https://custom.api.deepseek.com/v1")
                .setModel("deepseek-chat");

        assertEquals("https://custom.api.deepseek.com/v1", config.getBaseUrl());
    }

    @Test
    public void testConfigWithTimeout() {
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-key")
                .setConnectTimeout(60)
                .setReadTimeout(120)
                .setCallTimeout(180);

        assertEquals(60, config.getConnectTimeout());
        assertEquals(120, config.getReadTimeout());
        assertEquals(180, config.getCallTimeout());
    }
}
