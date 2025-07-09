package com.hgl.huojbackend.model.qo.user;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName UserRegisterRequest
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 16:26
 */
@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;
    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 验证码key
     */
    private String captchaKey;
    /**
     * 验证码
     */
    private String captcha;
}
