package xyz.bycor.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.auth.anno.RequiredLogin;

@RestController
// @RequiredLogin
@RequestMapping("/blacklist")
@Slf4j
public class testBlackList {
    @RequestMapping("/test")
    public void test(){
        log.error("test");
    }

    @RequiredLogin
    @RequestMapping("/test2")
    public void test2(){
        log.error("test");
    }


}
