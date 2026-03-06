package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 模型列表响应
 */
@Data
@Accessors(chain = true)
public class ListModelsResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String object;
    private List<ModelData> data;

    @Data
    @Accessors(chain = true)
    public static class ModelData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String object;
        private Long created;
        private String ownedBy;
    }
}
