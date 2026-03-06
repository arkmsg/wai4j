package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 嵌入向量响应
 */
@Data
@Accessors(chain = true)
public class EmbeddingResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String object;
    private List<EmbeddingData> data;
    private String model;
    private Usage usage;

    @Data
    @Accessors(chain = true)
    public static class EmbeddingData implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer index;
        private String object;
        private List<Float> embedding;
    }

    @Data
    @Accessors(chain = true)
    public static class Usage implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer promptTokens;
        private Integer totalTokens;
    }

    public List<Float> getFirstEmbedding() {
        if (data != null && !data.isEmpty()) {
            return data.get(0).getEmbedding();
        }
        return null;
    }
}
