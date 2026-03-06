package com.whaleal.ai.autoconfig;

import com.whaleal.ai.Ai;
import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.exception.AiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * WAI 自动配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WaiProperties.class)
@ConditionalOnClass(AiService.class)
@ConditionalOnProperty(prefix = "wai", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WaiAutoConfiguration {

    private final WaiProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public AiService aiService() {
        log.info("Initializing WAI AI Service with default provider: {}", properties.getDefaultProvider());
        
        // 初始化所有配置的提供商
        Map<String, WaiProperties.ProviderConfig> configs = properties.getConfigs();
        for (Map.Entry<String, WaiProperties.ProviderConfig> entry : configs.entrySet()) {
            String provider = entry.getKey();
            WaiProperties.ProviderConfig config = entry.getValue();
            
            if (Boolean.FALSE.equals(config.getEnabled())) {
                log.debug("Skipping disabled provider: {}", provider);
                continue;
            }
            
            try {
                AiConfig aiConfig = AiConfig.create()
                        .setProvider(provider)
                        .setApiKey(config.getApiKey())
                        .setModel(config.getModel())
                        .setBaseUrl(config.getBaseUrl())
                        .setConnectTimeout(config.getConnectTimeout())
                        .setReadTimeout(config.getReadTimeout())
                        .setCallTimeout(config.getCallTimeout())
                        .setExtra(config.getExtra());
                
                AiService service = Ai.getService(provider);
                log.info("Registered AI provider: {} with model: {}", provider, config.getModel());
            } catch (Exception e) {
                log.error("Failed to initialize provider {}: {}", provider, e.getMessage());
            }
        }
        
        // 返回默认提供商的服务
        try {
            return Ai.getService(properties.getDefaultProvider());
        } catch (AiException e) {
            log.warn("Default provider {} not available, returning fallback service", properties.getDefaultProvider());
            return new AiService() {
                @Override
                public String chat(AiConfig config, String prompt) {
                    throw new AiException("No AI service configured");
                }

                @Override
                public String chat(AiConfig config, java.util.List<com.whaleal.ai.Message> messages) {
                    throw new AiException("No AI service configured");
                }

                @Override
                public reactor.core.publisher.Flux<String> chatStream(AiConfig config, String prompt) {
                    return reactor.core.publisher.Flux.error(new AiException("No AI service configured"));
                }

                @Override
                public reactor.core.publisher.Flux<String> chatStream(AiConfig config, java.util.List<com.whaleal.ai.Message> messages) {
                    return reactor.core.publisher.Flux.error(new AiException("No AI service configured"));
                }
            };
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public WaiTemplate waiTemplate(AiService aiService) {
        return new WaiTemplate(aiService);
    }
}
