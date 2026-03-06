package com.whaleal.ai.doubao;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DoubaoService 测试
 */
public class DoubaoServiceTest {

    private DoubaoService service;

    @BeforeEach
    public void setUp() {
        service = new DoubaoService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("doubao"));
        assertTrue(service.support("DOUBAO"));
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
                .setProvider("doubao")
                .setApiKey("test-key")
                .setModel("doubao-pro");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Hello");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }
}
