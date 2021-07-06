package com.gaoyong.springsecurityoauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 高勇01
 * @date 2021/7/6 12:57
 */
@RestController("/user")
public class UserController {
    @GetMapping("/hello")
    public String test() {
        return "Hello world.";
    }
}
