package com.whaleal.ai;

import com.whaleal.ai.exception.AiException;
import com.whaleal.ai.spi.AiServiceProvider;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI 工具类 (类似 Hutool 的静态工具类风格)
 */
public class Ai {

    private static final ConcurrentHashMap<String, AiService> SERVICE_CACHE = new ConcurrentHashMap<>();

    static {
        // 自动加载所有 SPI 实现
        ServiceLoader.load(AiServiceProvider.class);
    }

    private Ai() {
        // 防止实例化
    }

    /**
     * 单次对话
     * @param config 配置
     * @param prompt 提示词
     * @return 响应
     */
    public static String chat(AiConfig config, String prompt) {
        AiService service = getService(config.getProvider());
        return service.chat(config, prompt);
    }

    /**
     * 多轮对话
     * @param config 配置
     * @param messages 消息列表
     * @return 响应
     */
    public static String chat(AiConfig config, List<Message> messages) {
        AiService service = getService(config.getProvider());
        return service.chat(config, messages);
    }

    /**
     * 流式对话
     * @param config 配置
     * @param prompt 提示词
     * @return 响应流
     */
    public static Flux<String> chatStream(AiConfig config, String prompt) {
        AiService service = getService(config.getProvider());
        return service.chatStream(config, prompt);
    }

    /**
     * 流式对话
     * @param config 配置
     * @param messages 消息列表
     * @return 响应流
     */
    public static Flux<String> chatStream(AiConfig config, List<Message> messages) {
        AiService service = getService(config.getProvider());
        return service.chatStream(config, messages);
    }

    /**
     * 获取 AI 服务
     * @param provider 提供商
     * @return AI 服务
     */
    public static AiService getService(String provider) {
        return SERVICE_CACHE.computeIfAbsent(provider, key -> {
            ServiceLoader<AiServiceProvider> loader = ServiceLoader.load(AiServiceProvider.class);
            for (AiServiceProvider spi : loader) {
                if (spi.support(provider)) {
                    return spi.getService();
                }
            }
            throw new AiException("No AiService found for provider: " + provider);
        });
    }

    /**
     * 注册 AI 服务
     * @param provider 提供商
     * @param service AI 服务
     */
    public static void registerService(String provider, AiService service) {
        SERVICE_CACHE.put(provider, service);
    }

    /**
     * 移除 AI 服务
     * @param provider 提供商
     */
    public static void removeService(String provider) {
        SERVICE_CACHE.remove(provider);
    }

    /**
     * 清空所有服务
     */
    public static void clearServices() {
        SERVICE_CACHE.clear();
    }
}
