package com.whaleal.ai.router;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RouterService 测试
 */
public class RouterServiceTest {

    private RouterService service;

    @BeforeEach
    public void setUp() {
        service = new RouterService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("router"));
        assertTrue(service.support("ROUTER"));
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
                .setProvider("router")
                .setApiKey("test-key");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
