package com.whaleal.ai.gemini;

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
 * Google Gemini 服务实现
 */
public class GeminiService implements AiService, AiServiceProvider, AiServiceEx {

    private static final String DEFAULT_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";
    private static final String DEFAULT_MODEL = "gemini-2.0-flash";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean support(String provider) {
        return "gemini".equalsIgnoreCase(provider);
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
            GeminiApi api = createApi(config);
            String model = config.getModel() != null ? config.getModel() : DEFAULT_MODEL;
            GenerateContentRequest request = buildRequest(config, messages);
            retrofit2.Response<GenerateContentResponse> response = api.generateContent(model, request).execute();
            
            if (!response.isSuccessful() || response.body() == null) {
                throw new AiException("Gemini API error: " + response.code());
            }
            
            GenerateContentResponse body = response.body();
            if (body.getCandidates() == null || body.getCandidates().isEmpty()) {
                throw new AiException("Empty response from Gemini");
            }
            
            return body.getCandidates().get(0).getContent().getParts().get(0).getText();
        } catch (AiException e) {
            throw e;
        } catch (Exception e) {
            throw new AiException("Gemini chat error: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> chatStream(AiConfig config, String prompt) {
        return chatStream(config, List.of(Message.user(prompt)));
    }

    @Override
    public Flux<String> chatStream(AiConfig config, List<Message> messages) {
        try {
            GeminiApi api = createApi(config);
            String model = config.getModel() != null ? config.getModel() : DEFAULT_MODEL;
            GenerateContentRequest request = buildRequest(config, messages);
            
            return Flux.create(emitter -> {
                try {
                    api.generateContentStream(model, request).subscribe(response -> {
                        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                            String content = response.getCandidates().get(0).getContent().getParts().get(0).getText();
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
            return Flux.error(new AiException("Gemini stream chat error: " + e.getMessage(), e));
        }
    }

    private GeminiApi createApi(AiConfig config) {
        String baseUrl = config.getBaseUrl() != null ? config.getBaseUrl() : DEFAULT_BASE_URL;
        
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout() != null ? config.getConnectTimeout() : 30, TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout() != null ? config.getReadTimeout() : 60, TimeUnit.SECONDS)
                .callTimeout(config.getCallTimeout() != null ? config.getCallTimeout() : 120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    String url = chain.request().url().newBuilder()
                            .addQueryParameter("key", config.getApiKey())
                            .build()
                            .toString();
                    okhttp3.Request request = chain.request()
                            .newBuilder()
                            .url(url)
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
        
        return retrofit.create(GeminiApi.class);
    }

    private GenerateContentRequest buildRequest(AiConfig config, List<Message> messages) {
        GenerateContentRequest request = new GenerateContentRequest();
        
        List<Content> contents = messages.stream()
                .map(m -> {
                    String role = "assistant".equals(m.getRole()) ? "model" : "user";
                    return new Content(role, List.of(new Part(m.getContent())));
                })
                .toList();
        
        request.setContents(contents);
        return request;
    }

    interface GeminiApi {
        @POST("models/{model}:generateContent")
        retrofit2.Call<GenerateContentResponse> generateContent(
                @Path("model") String model,
                @Body GenerateContentRequest request);

        @POST("models/{model}:streamGenerateContent")
        @Streaming
        Flux<GenerateContentResponse> generateContentStream(
                @Path("model") String model,
                @Body GenerateContentRequest request);
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class GenerateContentRequest {
        @com.fasterxml.jackson.annotation.JsonProperty("contents")
        private List<Content> contents;
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        @com.fasterxml.jackson.annotation.JsonProperty("role")
        private String role;

        @com.fasterxml.jackson.annotation.JsonProperty("parts")
        private List<Part> parts;

        public Content() {}

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        @com.fasterxml.jackson.annotation.JsonProperty("text")
        private String text;

        public Part() {}

        public Part(String text) {
            this.text = text;
        }
    }

    @lombok.Data
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class GenerateContentResponse {
        @com.fasterxml.jackson.annotation.JsonProperty("candidates")
        private List<Candidate> candidates;

        @lombok.Data
        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class Candidate {
            @com.fasterxml.jackson.annotation.JsonProperty("content")
            private Content content;

            @com.fasterxml.jackson.annotation.JsonProperty("finishReason")
            private String finishReason;
        }
    }

    @Override
    public com.whaleal.ai.EmbeddingResponse embedding(AiConfig config, List<String> texts) {
        throw new AiException("Gemini embedding not implemented yet");
    }

    @Override
    public com.whaleal.ai.ListModelsResponse listModels(AiConfig config) {
        throw new AiException("Gemini listModels not implemented yet");
    }

    @Override
    public com.whaleal.ai.ModerationResponse moderation(AiConfig config, String text) {
        throw new AiException("Gemini moderation not implemented yet");
    }
}
