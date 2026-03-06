package com.whaleal.ai.deepseek;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whaleal.ai.*;
import com.whaleal.ai.exception.AiException;
import com.whaleal.ai.spi.AiServiceProvider;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import reactor.core.publisher.Flux;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek AI 服务实现
 */
public class DeepSeekService implements AiService, AiServiceProvider, AiServiceEx {

    private static final String DEFAULT_BASE_URL = "https://api.deepseek.com/v1";
    private static final String DEFAULT_MODEL = "deepseek-chat";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean support(String provider) {
        return "deepseek".equalsIgnoreCase(provider);
    }

    @Override
    public AiService getService() {
        return this;
    }

    @Override
    public String chat(AiConfig config, String prompt) {
        return chat(config, List.of(Message.user(prompt)));
    }

    @Override
    public String chat(AiConfig config, List<Message> messages) {
        try {
            DeepSeekApi api = createApi(config);
            ChatCompletionRequest request = buildRequest(config, messages, false);
            retrofit2.Response<ChatCompletionResponse> response = api.chatCompletion(request).execute();
            
            if (response.body() == null || response.body().getChoices() == null || response.body().getChoices().isEmpty()) {
                throw new AiException("Empty response from DeepSeek");
            }
            
            return response.body().getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            throw new AiException("DeepSeek chat error: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> chatStream(AiConfig config, String prompt) {
        return chatStream(config, List.of(Message.user(prompt)));
    }

    @Override
    public Flux<String> chatStream(AiConfig config, List<Message> messages) {
        try {
            DeepSeekApi api = createApi(config);
            ChatCompletionRequest request = buildRequest(config, messages, true);
            
            return Flux.create(emitter -> {
                try {
                    api.chatCompletionStream(request).subscribe(response -> {
                        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                            String content = response.getChoices().get(0).getDelta().getContent();
                            if (content != null) {
                                emitter.next(content);
                            }
                        }
                    }, emitter::error, emitter::complete);
                } catch (Exception e) {
                    emitter.error(e);
                }
            });
        } catch (Exception e) {
            return Flux.error(new AiException("DeepSeek stream chat error: " + e.getMessage(), e));
        }
    }

    @Override
    public com.whaleal.ai.EmbeddingResponse embedding(AiConfig config, List<String> texts) {
        try {
            DeepSeekApi api = createApi(config);
            EmbeddingRequest request = new EmbeddingRequest();
            request.setModel("deepseek-embedding");
            request.setInput(texts);
            
            retrofit2.Response<com.whaleal.ai.EmbeddingResponse> response = api.embedding(request).execute();
            
            if (response.body() == null) {
                throw new AiException("Empty response from DeepSeek embedding");
            }
            
            return response.body();
        } catch (Exception e) {
            throw new AiException("DeepSeek embedding error: " + e.getMessage(), e);
        }
    }

    @Override
    public com.whaleal.ai.ListModelsResponse listModels(AiConfig config) {
        try {
            DeepSeekApi api = createApi(config);
            retrofit2.Response<com.whaleal.ai.ListModelsResponse> response = api.listModels().execute();
            
            if (response.body() == null) {
                throw new AiException("Empty response from DeepSeek listModels");
            }
            
            return response.body();
        } catch (Exception e) {
            throw new AiException("DeepSeek listModels error: " + e.getMessage(), e);
        }
    }

    @Override
    public com.whaleal.ai.ModerationResponse moderation(AiConfig config, String text) {
        try {
            DeepSeekApi api = createApi(config);
            ModerationRequest request = new ModerationRequest();
            request.setInput(text);
            
            retrofit2.Response<com.whaleal.ai.ModerationResponse> response = api.moderation(request).execute();
            
            if (response.body() == null) {
                throw new AiException("Empty response from DeepSeek moderation");
            }
            
            return response.body();
        } catch (Exception e) {
            throw new AiException("DeepSeek moderation error: " + e.getMessage(), e);
        }
    }

    private DeepSeekApi createApi(AiConfig config) {
        String baseUrl = config.getBaseUrl() != null ? config.getBaseUrl() : DEFAULT_BASE_URL;
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout() != null ? config.getConnectTimeout() : 30, TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout() != null ? config.getReadTimeout() : 60, TimeUnit.SECONDS)
                .callTimeout(config.getCallTimeout() != null ? config.getCallTimeout() : 120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    okhttp3.Request request = chain.request()
                            .newBuilder()
                            .header("Authorization", "Bearer " + config.getApiKey())
                            .header("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                });
        
        OkHttpClient client = clientBuilder.build();
        
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
        
        return retrofit.create(DeepSeekApi.class);
    }

    private ChatCompletionRequest buildRequest(AiConfig config, List<Message> messages, boolean stream) {
        String model = config.getModel() != null ? config.getModel() : DEFAULT_MODEL;
        
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(model);
        request.setStream(stream);
        
        List<ChatMessage> chatMessages = messages.stream()
                .map(m -> new ChatMessage(m.getRole(), m.getContent()))
                .toList();
        request.setMessages(chatMessages);
        
        return request;
    }

    // API 接口
    interface DeepSeekApi {
        @POST("/chat/completions")
        retrofit2.Call<ChatCompletionResponse> chatCompletion(@Body ChatCompletionRequest request);

        @POST("/chat/completions")
        @Streaming
        Flux<ChatCompletionResponse> chatCompletionStream(@Body ChatCompletionRequest request);

        @POST("/embeddings")
        retrofit2.Call<com.whaleal.ai.EmbeddingResponse> embedding(@Body EmbeddingRequest request);

        @GET("/models")
        retrofit2.Call<com.whaleal.ai.ListModelsResponse> listModels();

        @POST("/moderations")
        retrofit2.Call<com.whaleal.ai.ModerationResponse> moderation(@Body ModerationRequest request);
    }

    // 请求模型
    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatCompletionRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("model")
        private String model;

        @com.fasterxml.jackson.annotation.JsonProperty("messages")
        private List<ChatMessage> messages;

        @com.fasterxml.jackson.annotation.JsonProperty("stream")
        private Boolean stream = false;
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatMessage {
        @com.fasterxml.jackson.annotation.JsonProperty("role")
        private String role;

        @com.fasterxml.jackson.annotation.JsonProperty("content")
        private String content;

        public ChatMessage() {}

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatCompletionResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("choices")
        private List<Choice> choices;

        @lombok.Data
        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class Choice {
            @com.fasterxml.jackson.annotation.JsonProperty("index")
            private Integer index;

            @com.fasterxml.jackson.annotation.JsonProperty("message")
            private ChatMessage message;

            @com.fasterxml.jackson.annotation.JsonProperty("delta")
            private ChatMessage delta;
        }
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbeddingRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("model")
        private String model;

        @com.fasterxml.jackson.annotation.JsonProperty("input")
        private List<String> input;
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModerationRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("input")
        private String input;
    }
}
