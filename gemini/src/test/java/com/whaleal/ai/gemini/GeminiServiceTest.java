package com.whaleal.ai.gemini;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GeminiService 测试
 */
public class GeminiServiceTest {

    private GeminiService service;

    @BeforeEach
    public void setUp() {
        service = new GeminiService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("gemini"));
        assertTrue(service.support("GEMINI"));
        assertFalse(service.support("openai"));
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
                .setProvider("gemini")
                .setApiKey("test-key")
                .setModel("gemini-ultra");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
