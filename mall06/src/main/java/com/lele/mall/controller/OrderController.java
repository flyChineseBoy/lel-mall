package com.lele.mall.controller;/*
 * com.lele.mall.controller
 * @author: lele
 * @date: 2020-04-08
 */

import com.lele.mall.feignclient.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/order")
@RestController
public class OrderController {

    // 像注入本地服务一样注入远端服务
    @Resource
    private ProductService productService;

    @GetMapping("apply")
    public String applyOrder(){
        return productService.reduceInventory();
    }

}
