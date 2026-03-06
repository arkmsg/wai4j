package com.whaleal.ai.openai;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OpenAiService 测试
 */
public class OpenAiServiceTest {

    private OpenAiService service;

    @BeforeEach
    public void setUp() {
        service = new OpenAiService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("openai"));
        assertTrue(service.support("OPENAI"));
        assertFalse(service.support("deepseek"));
    }

    @Test
    public void testGetService() {
        AiService aiService = service.getService();
        assertNotNull(aiService);
        assertSame(service, aiService);
    }

    @Test
    public void testChatNotImplemented() {
        AiConfig config = AiConfig.create()
                .setProvider("openai")
                .setApiKey("test-key")
                .setModel("gpt-4");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
