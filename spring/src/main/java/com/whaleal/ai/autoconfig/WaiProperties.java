package com.whaleal.ai.autoconfig;

import com.whaleal.ai.AiConfig;
import com.whaleal.ai.AiService;
import com.whaleal.ai.Ai;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * WAI 配置属性
 */
@Data
@ConfigurationProperties(prefix = "wai")
public class WaiProperties {

    /**
     * 默认提供商
     */
    private String defaultProvider = "deepseek";

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 配置映射
     */
    private Map<String, ProviderConfig> configs = new HashMap<>();

    /**
     * 提供商配置
     */
    @Data
    public static class ProviderConfig {
        
        /**
         * API 密钥
         */
        private String apiKey;

        /**
         * 模型名称
         */
        private String model;

        /**
         * 基础 URL
         */
        private String baseUrl;

        /**
         * 连接超时 (秒)
         */
        private Integer connectTimeout;

        /**
         * 读取超时 (秒)
         */
        private Integer readTimeout;

        /**
         * 调用超时 (秒)
         */
        private Integer callTimeout;

        /**
         * 是否启用
         */
        private Boolean enabled = true;

        /**
         * 额外配置
         */
        private Map<String, Object> extra = new HashMap<>();
    }
}
