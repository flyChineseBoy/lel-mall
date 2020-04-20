package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 * @date: 2020-04-08
 */

import com.lele.mall.feignclient.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "订单服务")
@RequestMapping("/order")
@RestController
public class OrderController {

    // 像注入本地服务一样注入远端服务
    @Resource
    private ProductService productService;


    @ApiOperation("通过并执行订单")
    //@PreAuthorize("hasRole('ROLE_ADMIN')") // super是我们在MyUserDetailsService中赋予admin用户的。
    @GetMapping("apply")
    public String applyOrder(){
        return productService.reduceInventory();
    }

    @ApiOperation("通过并执行订单2号")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("apply02")
    public String applyOrder02(){
        return "权限专用测试";
    }
}
