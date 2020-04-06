package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {

    @SentinelResource("test")
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        throw new RuntimeException("asdasd");
        //return name;
    }
}
