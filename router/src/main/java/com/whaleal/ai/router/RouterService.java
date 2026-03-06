package com.whaleal.ai.router;

import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.Message;
import com.whaleal.ai.exception.AiException;
import com.whaleal.ai.spi.AiServiceProvider;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI 模型路由服务
 * 
 * TODO: 实现智能路由和负载均衡
 */
public class RouterService implements AiService, AiServiceProvider {

    @Override
    public boolean support(String provider) {
        return "router".equalsIgnoreCase(provider);
    }

    @Override
    public AiService getService() {
        return this;
    }

    @Override
    public String chat(AiConfig config, String prompt) {
        // TODO: 实现路由逻辑
        throw new AiException("Router service not implemented yet. " +
                "Please implement the RouterService.chat() method.");
    }

    @Override
    public String chat(AiConfig config, List<Message> messages) {
        // TODO: 实现路由逻辑
        throw new AiException("Router service not implemented yet. " +
                "Please implement the RouterService.chat() method.");
    }

    @Override
    public Flux<String> chatStream(AiConfig config, String prompt) {
        return Flux.error(new AiException("Router stream service not implemented yet."));
    }

    @Override
    public Flux<String> chatStream(AiConfig config, List<Message> messages) {
        return Flux.error(new AiException("Router stream service not implemented yet."));
    }
}
