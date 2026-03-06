package com.whaleal.ai.glm;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GlmService 测试
 */
public class GlmServiceTest {

    private GlmService service;

    @BeforeEach
    public void setUp() {
        service = new GlmService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("glm"));
        assertTrue(service.support("GLM"));
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
                .setProvider("glm")
                .setApiKey("test-key")
                .setModel("glm-4");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
