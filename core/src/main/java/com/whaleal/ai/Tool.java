package com.whaleal.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 工具调用
 */
@Data
@Accessors(chain = true)
public class Tool implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "function";
    private Function function;

    public static Tool fromFunction(Function function) {
        Tool tool = new Tool();
        tool.setFunction(function);
        return tool;
    }

    public static List<Tool> fromFunctions(List<Function> functions) {
        return functions.stream()
                .map(Tool::fromFunction)
                .toList();
    }
}
