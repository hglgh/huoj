package com.hgl.huojbackend.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName CaptchaVO
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 15:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 图形验证码 key
     */
    private String captchaKey;

    /**
     * 图形验证码 base64 图片
     */
    private String captchaImage;
}
