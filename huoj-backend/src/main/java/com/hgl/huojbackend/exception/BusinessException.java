package com.hgl.huojbackend.exception;

import lombok.Getter;

/**
 * ClassName: BusinessException
 * Package: com.hgl.huojbackend.exception
 * Description: 自定义业务异常
 *
 * @Author HGL
 * @Create: 2024/12/8 10:55
 */
@Getter
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
