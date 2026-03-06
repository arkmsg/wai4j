package com.whaleal.ai.spi;

import com.whaleal.ai.AiService;

/**
 * AI 服务提供商 SPI 接口
 */
public interface AiServiceProvider {

    /**
     * 是否支持该提供商
     * @param provider 提供商名称
     * @return 是否支持
     */
    boolean support(String provider);

    /**
     * 获取 AI 服务
     * @return AI 服务
     */
    AiService getService();
}
