package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 */

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api
@RestController
public class TestController {


    @SentinelResource(value="test",fallback = "testFallback",fallbackClass = TestController.class,blockHandler = "testBlockHandler",blockHandlerClass = TestController.class)
    @GetMapping(value = "/test")
    public String test(@RequestParam String name){
        //throw new RuntimeException("asdasd");
        return name;
    }

    @GetMapping(value = "/testFallback")
    public String testFallback(@RequestParam String name){
        return "testFallback"+name;
    }

    @GetMapping(value = "/testBlockHandler")
    public String testBlockHandler(@RequestParam String name,BlockException ex){
        return "testBlockHandler"+name;
    }


}
