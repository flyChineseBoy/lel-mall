package com.lele.mall.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/product")
@RestController
public class ProductController {

        @SentinelResource("reduce_inventory")
        @GetMapping("reduce_inventory")
        public String reduceInventory(){
            //throw new RuntimeException("asd");
            return "库存减一";
        }

}