package com.whaleal.ai;

import com.whaleal.ai.exception.AiException;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * AI 服务扩展接口
 */
public interface AiServiceEx extends AiService {

    /**
     * 获取嵌入向量
     */
    default EmbeddingResponse embedding(AiConfig config, String text) {
        return embedding(config, List.of(text));
    }

    /**
     * 获取嵌入向量列表
     */
    EmbeddingResponse embedding(AiConfig config, List<String> texts);

    /**
     * 流式嵌入向量
     */
    default Flux<EmbeddingResponse> embeddingStream(AiConfig config, String text) {
        return embeddingStream(config, List.of(text));
    }

    /**
     * 流式嵌入向量列表
     */
    default Flux<EmbeddingResponse> embeddingStream(AiConfig config, List<String> texts) {
        return Flux.error(new AiException("Streaming embedding not supported"));
    }

    /**
     * 获取模型列表
     */
    default ListModelsResponse listModels(AiConfig config) {
        throw new AiException("listModels not supported for this provider");
    }

    /**
     * 搜索
     */
    default SearchResponse search(AiConfig config, String query) {
        throw new AiException("search not supported for this provider");
    }

    /**
     * 流式搜索
     */
    default Flux<SearchResponse> searchStream(AiConfig config, String query) {
        return Flux.error(new AiException("streaming search not supported for this provider"));
    }

    /**
     * 内容审核
     */
    default ModerationResponse moderation(AiConfig config, String text) {
        throw new AiException("moderation not supported for this provider");
    }
}
