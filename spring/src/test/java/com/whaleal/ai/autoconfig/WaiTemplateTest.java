package com.whaleal.ai.autoconfig;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WaiTemplate 测试
 */
public class WaiTemplateTest {

    private WaiTemplate waiTemplate;
    private MockAiService mockService;

    @BeforeEach
    public void setUp() {
        mockService = new MockAiService();
        waiTemplate = new WaiTemplate(mockService);
    }

    @Test
    public void testChat() {
        String response = waiTemplate.chat("Hello");

        assertNotNull(response);
        assertEquals("Mock response: Hello", response);
    }

    @Test
    public void testChatWithProvider() {
        String response = waiTemplate.chat("deepseek", "Hello");

        assertNotNull(response);
        assertEquals("Mock response: Hello", response);
    }

    @Test
    public void testMultiTurnChat() {
        List<Message> messages = Arrays.asList(
                Message.system("You are helpful"),
                Message.user("Hi")
        );

        String response = waiTemplate.chat(messages);

        assertNotNull(response);
        assertEquals("Mock response: Multi-turn", response);
    }

    @Test
    public void testChatStream() {
        Flux<String> stream = waiTemplate.chatStream("Hello");

        assertNotNull(stream);
        List<String> results = stream.collectList().block();
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Mock response: Hello", results.get(0));
    }

    // Mock AiService for testing
    static class MockAiService implements com.whaleal.ai.AiService {
        @Override
        public String chat(AiConfig config, String prompt) {
            return "Mock response: " + prompt;
        }

        @Override
        public String chat(AiConfig config, List<Message> messages) {
            return "Mock response: Multi-turn";
        }

        @Override
        public Flux<String> chatStream(AiConfig config, String prompt) {
            return Flux.just("Mock response: " + prompt);
        }

        @Override
        public Flux<String> chatStream(AiConfig config, List<Message> messages) {
            return Flux.just("Mock response: Multi-turn");
        }
    }
}
