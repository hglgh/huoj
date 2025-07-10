package com.hgl.huojbackend.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 13:08
 */
@RestController
@RequestMapping("/test")
public class TestController {
    // 会话登录接口
    @GetMapping("doLogin")
    public SaResult doLogin(String name, String pwd) {
        // 第一步：比对前端提交的账号名称、密码
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(100018808);
            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    @GetMapping("/isLogin")
    public SaResult isLogin() {
        // 判断当前会话是否已登录
        if (StpUtil.isLogin()) {
            return SaResult.ok("已登录");
        }
        return SaResult.error("未登录");
    }

    @GetMapping("/getInfo")
    public SaResult getInfo() {
        // 获取当前登录的账号id
        long loginId = StpUtil.getLoginIdAsLong();
        return SaResult.ok("当前登录的账号id:" + loginId);
    }

    //    @GetMapping("/logout")
    public SaResult logout() {
        // 登出
        StpUtil.logout();
        return SaResult.ok("登出成功");
    }

    @GetMapping("/checkLogin")
    public SaResult checkLogin() {
        try {
            StpUtil.checkLogin();
            return SaResult.ok("已登录");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("getAllInfo")
    public SaResult getAllInfo() {
        long id = StpUtil.getLoginIdAsLong();
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return SaResult.ok()
                .set("id", id)
                .set("tokenInfo", tokenInfo);

    }

}
