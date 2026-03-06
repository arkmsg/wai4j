package com.whaleal.ai.local;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.Message;
import com.whaleal.ai.exception.AiException;
import com.whaleal.ai.spi.AiServiceProvider;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 本地模型服务实现 (LGBM, ONNX 等)
 * 
 * TODO: 实现本地模型推理
 */
public class LocalService implements AiService, AiServiceProvider {

    @Override
    public boolean support(String provider) {
        return "local".equalsIgnoreCase(provider);
    }

    @Override
    public AiService getService() {
        return this;
    }

    @Override
    public String chat(AiConfig config, String prompt) {
        // TODO: 实现本地模型推理
        String modelType = config.getModel();
        
        if (modelType != null && modelType.toLowerCase().contains("lgbm")) {
            // LGBM 模型推理
            throw new AiException("LGBM model inference not implemented yet.");
        } else if (modelType != null && modelType.toLowerCase().contains("onnx")) {
            // ONNX 模型推理
            throw new AiException("ONNX model inference not implemented yet.");
        } else {
            throw new AiException("Local model type not supported: " + modelType);
        }
    }

    @Override
    public String chat(AiConfig config, List<Message> messages) {
        // 本地模型暂不支持多轮对话
        throw new AiException("Local models do not support multi-turn conversation.");
    }

    @Override
    public Flux<String> chatStream(AiConfig config, String prompt) {
        return Flux.error(new AiException("Local models do not support streaming."));
    }

    @Override
    public Flux<String> chatStream(AiConfig config, List<Message> messages) {
        return Flux.error(new AiException("Local models do not support streaming."));
    }
}
