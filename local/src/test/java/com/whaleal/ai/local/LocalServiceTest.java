package com.whaleal.ai.local;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LocalService 测试
 */
public class LocalServiceTest {

    private LocalService service;

    @BeforeEach
    public void setUp() {
        service = new LocalService();
    }

    @Test
    public void testServiceProviderSupport() {
        assertTrue(service.support("local"));
        assertTrue(service.support("LOCAL"));
        assertFalse(service.support("openai"));
    }

    @Test
    public void testGetService() {
        AiService aiService = service.getService();
        assertNotNull(aiService);
        assertSame(service, aiService);
    }

    @Test
    public void testLgbmNotImplemented() {
        AiConfig config = AiConfig.create()
                .setProvider("local")
                .setModel("lgbm-model");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Predict");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }

    @Test
    public void testOnnxNotImplemented() {
        AiConfig config = AiConfig.create()
                .setProvider("local")
                .setModel("onnx-model");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, "Predict");
        });

        assertTrue(exception.getMessage().contains("not implemented"));
    }

    @Test
    public void testMultiTurnNotSupported() {
        AiConfig config = AiConfig.create()
                .setProvider("local")
                .setModel("test-model");

        AiException exception = assertThrows(AiException.class, () -> {
            service.chat(config, java.util.Arrays.asList());
        });

        assertTrue(exception.getMessage().contains("do not support"));
    }
}
