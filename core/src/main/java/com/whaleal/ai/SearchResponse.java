package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索响应
 */
@Data
@Accessors(chain = true)
public class SearchResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String object;
    private Long created;
    private List<SearchResult> results;

    @Data
    @Accessors(chain = true)
    public static class SearchResult implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer index;
        private String text;
        private Double score;
    }
}
