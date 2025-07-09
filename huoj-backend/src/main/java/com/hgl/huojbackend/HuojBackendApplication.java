package com.hgl.huojbackend;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 请别把我整破防
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class HuojBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuojBackendApplication.class, args);
//        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  Huoj-Backend启动成功   ლ(´ڡ`ლ)ﾞ \s
                 .-------.       ____     __       \s
                 |  _ _   \\      \\   \\   /  /   \s
                 | ( ' )  |       \\  _. /  '      \s
                 |(_ o _) /        _( )_ .'        \s
                """
        );
    }

}
