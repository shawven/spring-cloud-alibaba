package com.sca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

/**
 * Created by wangyunfei on 2017/6/12.
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public Principal user(Principal user){
        return user;
    }

    @GetMapping("/test")
    public String test(Principal user){
        System.out.println(user);
        return "test";
    }
}
