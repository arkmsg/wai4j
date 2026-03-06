package com.whaleal.ai.exception;

/**
 * AI 异常
 */
public class AiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    public AiException() {
        super();
    }

    public AiException(String message) {
        super(message);
    }

    public AiException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiException(Throwable cause) {
        super(cause);
    }

    public AiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AiException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
