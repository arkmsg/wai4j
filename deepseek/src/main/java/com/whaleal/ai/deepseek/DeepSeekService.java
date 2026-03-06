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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek AI 服务实现
 */
public class DeepSeekService implements AiService, AiServiceProvider {

    @Override
    public boolean support(String provider) {
        return "deepseek".equalsIgnoreCase(provider);
    }

    @Override
    public AiService getService() {
        return this;
    }

    private static final String DEFAULT_BASE_URL = "https://api.deepseek.com/v1";
    private static final String DEFAULT_MODEL = "deepseek-chat";

    private final ObjectMapper objectMapper = new ObjectMapper();

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
            ChatCompletionResponse body = response.body();
            
            if (body == null || body.getChoices() == null || body.getChoices().isEmpty()) {
                throw new AiException("Empty response from DeepSeek");
            }
            
            return body.getChoices().get(0).getMessage().getContent();
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
}
