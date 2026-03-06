package com.whaleal.ai.deepseek;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import reactor.core.publisher.Flux;

/**
 * DeepSeek API 接口
 */
public interface DeepSeekApi {

    /**
     * 聊天完成
     */
    @POST("/chat/completions")
    Call<ChatCompletionResponse> chatCompletion(@Body ChatCompletionRequest request);

    /**
     * 聊天完成（流式）
     */
    @POST("/chat/completions")
    @Streaming
    Flux<ChatCompletionResponse> chatCompletionStream(@Body ChatCompletionRequest request);
}
