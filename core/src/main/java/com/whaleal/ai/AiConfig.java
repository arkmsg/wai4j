package com.whaleal.ai;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * AI 配置
 */
@Data
@Accessors(chain = true)
public class AiConfig implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 提供商：deepseek, openai, gemini, glm, qwen, doubao, grok, local
     */
    private String provider;

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

    public AiConfig() {
    }

    public AiConfig(String provider, String apiKey) {
        this.provider = provider;
        this.apiKey = apiKey;
    }

    public static AiConfig create() {
        return new AiConfig();
    }

    public static AiConfig of(String provider, String apiKey) {
        return new AiConfig(provider, apiKey);
    }

    public boolean isValid() {
        return StrUtil.isNotBlank(provider) && StrUtil.isNotBlank(apiKey);
    }
}
