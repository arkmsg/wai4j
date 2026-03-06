package com.whaleal.ai.grok;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GrokService 测试
 */
public class GrokServiceTest {

    private GrokService service;

    @BeforeEach
    public void setUp() {
        service = new GrokService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("grok"));
        assertTrue(service.support("GROK"));
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
                .setProvider("grok")
                .setApiKey("test-key")
                .setModel("grok-beta");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
