package com.hgl.huojbackend.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.hgl.huojbackend.common.BaseResponse;
import com.hgl.huojbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.hgl.huojbackend.exception
 * Description: 全局异常处理器
 *
 * @Author HGL
 * @Create: 2024/12/8 11:30
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> notLoginExceptionHandler(NotLoginException e) {
        log.error("NotLoginException:{}", e.getMessage());

        // 根据 NotLoginException 的场景值进行具体处理
/*        String type = e.getType();
        String message = switch (type) {
            case NotLoginException.NOT_TOKEN -> "未能从请求中读取到有效 token";
            case NotLoginException.INVALID_TOKEN -> "已读取到 token，但是 token 无效";
            case NotLoginException.TOKEN_TIMEOUT -> "已读取到 token，但是 token 已经过期";
            case NotLoginException.BE_REPLACED -> "已读取到 token，但是 token 已被顶下线";
            case NotLoginException.KICK_OUT -> "已读取到 token，但是 token 已被踢下线";
            case NotLoginException.TOKEN_FREEZE -> "已读取到 token，但是 token 已被冻结";
            case NotLoginException.NO_PREFIX -> "未按照指定前缀提交 token";
            default -> "未登录";
        };*/

        return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
    }

    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<?> notRoleExceptionHandler(NotRoleException e) {
        log.error("NotRoleException:{}", e.getMessage());
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR, "无权限");
    }
}
