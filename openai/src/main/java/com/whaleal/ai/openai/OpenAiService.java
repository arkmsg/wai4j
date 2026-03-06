package com.whaleal.ai.openai;

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
 * OpenAI GPT 服务实现
 */
public class OpenAiService implements AiService, AiServiceProvider {

    private static final String DEFAULT_BASE_URL = "https://api.openai.com/v1";
    private static final String DEFAULT_MODEL = "gpt-4";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean support(String provider) {
        return "openai".equalsIgnoreCase(provider);
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
            OpenAiApi api = createApi(config);
            ChatCompletionRequest request = buildRequest(config, messages, false);
            retrofit2.Response<ChatCompletionResponse> response = api.chatCompletion(request).execute();
            
            if (!response.isSuccessful() || response.body() == null) {
                throw new AiException("OpenAI API error: " + response.code());
            }
            
            ChatCompletionResponse body = response.body();
            if (body.getChoices() == null || body.getChoices().isEmpty()) {
                throw new AiException("Empty response from OpenAI");
            }
            
            return body.getChoices().get(0).getMessage().getContent();
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            throw new AiException("OpenAI chat error: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> chatStream(AiConfig config, String prompt) {
        return chatStream(config, List.of(Message.user(prompt)));
    }

    @Override
    public Flux<String> chatStream(AiConfig config, List<Message> messages) {
        try {
            OpenAiApi api = createApi(config);
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
            return Flux.error(new AiException("OpenAI stream chat error: " + e.getMessage(), e));
        }
    }

    private OpenAiApi createApi(AiConfig config) {
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
        
        return retrofit.create(OpenAiApi.class);
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

    // OpenAI API 接口
    interface OpenAiApi {
        @POST("/chat/completions")
        retrofit2.Call<ChatCompletionResponse> chatCompletion(@Body ChatCompletionRequest request);

        @POST("/chat/completions")
        @Streaming
        Flux<ChatCompletionResponse> chatCompletionStream(@Body ChatCompletionRequest request);
    }

    // 请求和响应模型
    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatCompletionRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("model")
        private String model;

        @com.fasterxml.jackson.annotation.JsonProperty("messages")
        private List<ChatMessage> messages;

        @com.fasterxml.jackson.annotation.JsonProperty("stream")
        private Boolean stream = false;

        @com.fasterxml.jackson.annotation.JsonProperty("temperature")
        private Double temperature;

        @com.fasterxml.jackson.annotation.JsonProperty("max_tokens")
        private Integer maxTokens;
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

            @com.fasterxml.jackson.annotation.JsonProperty("finish_reason")
            private String finishReason;
        }
    }
}
