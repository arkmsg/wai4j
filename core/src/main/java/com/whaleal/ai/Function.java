package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 函数调用 - 函数定义
 */
@Data
@Accessors(chain = true)
public class Function implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Map<String, Parameter> parameters;

    @Data
    @Accessors(chain = true)
    public static class Parameter implements Serializable {
        private static final long serialVersionUID = 1L;

        private String type;
        private String description;
        private Boolean required;
        private Map<String, Object> properties;
    }

    public static Function builder() {
        return new Function();
    }

    public Function name(String name) {
        setName(name);
        return this;
    }

    public Function description(String description) {
        setDescription(description);
        return this;
    }

    public Function addParameter(String name, String type, String description, boolean required) {
        if (this.parameters == null) {
            this.parameters = new java.util.LinkedHashMap<>();
        }
        Parameter param = new Parameter();
        param.setType(type);
        param.setDescription(description);
        param.setRequired(required);
        this.parameters.put(name, param);
        return this;
    }
}
