package com.hgl.huojbackend.common.satoken;


import cn.dev33.satoken.stp.StpInterface;
import com.hgl.huojbackend.model.entity.User;
import com.hgl.huojbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName StpInterfaceImpl
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 15:18
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        User user = userService.getById(loginId.toString());
        if (user != null) {
            return List.of(user.getUserRole());
        }
        return List.of();
    }
}
