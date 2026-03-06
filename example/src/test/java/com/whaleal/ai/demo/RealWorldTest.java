package com.whaleal.ai.demo;

import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;
import com.whaleal.ai.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * WAI4J 真实可用性测试
 * 
 * 测试说明：
 * 1. 本测试用于验证 WAI4J 核心功能是否真实可用
 * 2. 需要配置真实的 API Key 才能执行
 * 3. 如果不想实际调用 API，可以跳过执行
 */
public class RealWorldTest {

    public static void main(String[] args) {
        System.out.println("=== WAI4J 真实可用性测试 ===\n");
        
        // 测试 1: 验证核心类是否可以加载
        testClassLoading();
        
        // 测试 2: 验证配置创建
        testConfigCreation();
        
        // 测试 3: 验证消息创建
        testMessageCreation();
        
        // 测试 4: 实际 API 调用 (需要真实 API Key)
        // testRealAPICall();
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 测试 1: 验证核心类是否可以加载
     */
    private static void testClassLoading() {
        System.out.println("测试 1: 验证核心类加载");
        
        try {
            Class<?> aiClass = Class.forName("com.whaleal.ai.Ai");
            System.out.println("  ✓ Ai 类加载成功：" + aiClass.getName());
            
            Class<?> configClass = Class.forName("com.whaleal.ai.AiConfig");
            System.out.println("  ✓ AiConfig 类加载成功：" + configClass.getName());
            
            Class<?> messageClass = Class.forName("com.whaleal.ai.Message");
            System.out.println("  ✓ Message 类加载成功：" + messageClass.getName());
            
            Class<?> serviceClass = Class.forName("com.whaleal.ai.AiService");
            System.out.println("  ✓ AiService 类加载成功：" + serviceClass.getName());
            
            System.out.println("  ✅ 核心类加载测试通过\n");
        } catch (ClassNotFoundException e) {
            System.err.println("  ❌ 核心类加载失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试 2: 验证配置创建
     */
    private static void testConfigCreation() {
        System.out.println("测试 2: 验证配置创建");
        
        try {
            AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("test-api-key")
                .setModel("deepseek-chat")
                .setConnectTimeout(30)
                .setReadTimeout(60);
            
            System.out.println("  ✓ 配置创建成功");
            System.out.println("    - Provider: " + config.getProvider());
            System.out.println("    - Model: " + config.getModel());
            System.out.println("    - ConnectTimeout: " + config.getConnectTimeout());
            System.out.println("    - ReadTimeout: " + config.getReadTimeout());
            System.out.println("    - Valid: " + config.isValid());
            
            System.out.println("  ✅ 配置创建测试通过\n");
        } catch (Exception e) {
            System.err.println("  ❌ 配置创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试 3: 验证消息创建
     */
    private static void testMessageCreation() {
        System.out.println("测试 2: 验证消息创建");
        
        try {
            Message systemMsg = Message.system("你是一位 AI 助手");
            System.out.println("  ✓ System 消息创建：" + systemMsg.getRole() + " - " + systemMsg.getContent());
            
            Message userMsg = Message.user("你好");
            System.out.println("  ✓ User 消息创建：" + userMsg.getRole() + " - " + userMsg.getContent());
            
            Message assistantMsg = Message.assistant("你好，有什么可以帮助你的吗？");
            System.out.println("  ✓ Assistant 消息创建：" + assistantMsg.getRole() + " - " + assistantMsg.getContent());
            
            List<Message> messages = new ArrayList<>();
            messages.add(systemMsg);
            messages.add(userMsg);
            
            System.out.println("  ✓ 消息列表创建：" + messages.size() + " 条消息");
            
            System.out.println("  ✅ 消息创建测试通过\n");
        } catch (Exception e) {
            System.err.println("  ❌ 消息创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试 4: 实际 API 调用 (需要真实 API Key)
     * 
     * 注意：此测试需要真实的 DeepSeek API Key
     * 取消注释并配置 API Key 后可以执行实际调用
     */
    private static void testRealAPICall() {
        System.out.println("测试 4: 实际 API 调用测试");
        System.out.println("  ⚠️  此测试需要真实的 API Key");
        System.out.println("  请取消代码注释并配置 API Key 后执行\n");
        
        /*
        try {
            // 创建配置
            AiConfig config = AiConfig.create()
                .setProvider("deepseek")
                .setApiKey("你的真实 API Key")  // 替换为真实 API Key
                .setModel("deepseek-chat");
            
            System.out.println("  正在调用 DeepSeek API...");
            
            // 单次对话
            String response = Ai.chat(config, "用一句话介绍 Java");
            System.out.println("  ✓ API 调用成功");
            System.out.println("  响应：" + response);
            
            // 多轮对话
            List<Message> messages = new ArrayList<>();
            messages.add(Message.system("你是一位 Java 专家"));
            messages.add(Message.user("什么是 Spring 框架？"));
            
            String multiTurnResponse = Ai.chat(config, messages);
            System.out.println("  ✓ 多轮对话成功");
            System.out.println("  响应：" + multiTurnResponse);
            
            System.out.println("  ✅ API 调用测试通过\n");
        } catch (Exception e) {
            System.err.println("  ❌ API 调用失败：" + e.getMessage());
            e.printStackTrace();
        }
        */
    }
}
