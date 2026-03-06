package com.whaleal.ai.demo;

import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;
import com.whaleal.ai.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * WAI 示例应用
 */
@SpringBootApplication
public class WaiDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WaiDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== WAI Demo Start ===");
        
        // 示例 1: 单次对话
        simpleChat();
        
        // 示例 2: 多轮对话
        multiTurnChat();
        
        System.out.println("=== WAI Demo End ===");
    }

    /**
     * 单次对话示例
     */
    private void simpleChat() {
        System.out.println("\n--- Simple Chat ---");
        
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("your-api-key-here")
                .setModel("deepseek-chat");
        
        try {
            String response = Ai.chat(config, "用一句话介绍 Java");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * 多轮对话示例
     */
    private void multiTurnChat() {
        System.out.println("\n--- Multi-turn Chat ---");
        
        AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("your-api-key-here");
        
        List<Message> messages = new ArrayList<>();
        messages.add(Message.system("你是一位 Java 技术专家"));
        messages.add(Message.user("什么是 Spring 框架？"));
        
        try {
            String response = Ai.chat(config, messages);
            System.out.println("Response: " + response);
            
            // 继续对话
            messages.add(Message.assistant(response));
            messages.add(Message.user("它有哪些核心特性？"));
            
            response = Ai.chat(config, messages);
            System.out.println("Follow-up: " + response);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
