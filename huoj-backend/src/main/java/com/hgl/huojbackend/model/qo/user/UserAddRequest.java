package com.hgl.huojbackend.model.qo.user;


import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName UserAddRequest
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/9 9:31
 */
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}
