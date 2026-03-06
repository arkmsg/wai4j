package com.whaleal.ai.qwen;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QwenService 测试
 */
public class QwenServiceTest {

    private QwenService service;

    @BeforeEach
    public void setUp() {
        service = new QwenService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("qwen"));
        assertTrue(service.support("QWEN"));
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
                .setProvider("qwen")
                .setApiKey("test-key")
                .setModel("qwen-max");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
