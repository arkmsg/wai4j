package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 内容审核响应
 */
@Data
@Accessors(chain = true)
public class ModerationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String model;
    private Results results;

    @Data
    @Accessors(chain = true)
    public static class Results implements Serializable {
        private static final long serialVersionUID = 1L;

        private Boolean flagged;
        private Categories categories;
        private CategoryScores categoryScores;
    }

    @Data
    @Accessors(chain = true)
    public static class Categories implements Serializable {
        private static final long serialVersionUID = 1L;

        private Boolean hate;
        private Boolean hateThreatening;
        private Boolean harassment;
        private Boolean harassmentThreatening;
        private Boolean selfHarm;
        private Boolean selfHarmIntent;
        private Boolean selfHarmInstructions;
        private Boolean sexual;
        private Boolean sexualMinors;
        private Boolean violence;
        private Boolean violenceGraphic;
    }

    @Data
    @Accessors(chain = true)
    public static class CategoryScores implements Serializable {
        private static final long serialVersionUID = 1L;

        private Double hate;
        private Double hateThreatening;
        private Double harassment;
        private Double harassmentThreatening;
        private Double selfHarm;
        private Double selfHarmIntent;
        private Double selfHarmInstructions;
        private Double sexual;
        private Double sexualMinors;
        private Double violence;
        private Double violenceGraphic;
    }
}
