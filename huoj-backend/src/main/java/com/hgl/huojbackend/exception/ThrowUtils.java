package com.hgl.huojbackend.exception;

/**
 * ClassName: TrowUtils
 * Package: com.hgl.huojbackend.exception
 * Description: 异常处理工具类
 *
 * @Author HGL
 * @Create: 2024/12/8 11:00
 */
public class ThrowUtils {
    /**
     * 如果条件成立则抛出异常
     *
     * @param condition        判断条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * @param condition 判断条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * @param condition 判断条件
     * @param errorCode 错误码
     * @param message   自定义错误信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
