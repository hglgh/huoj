package com.hgl.huojbackend.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName systemController
 * @Author 请别把我整破防
 * @Description //TODO
 * @Date 2025/7/8 10:57
 */
@RestController("/system")
public class systemController {
    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
