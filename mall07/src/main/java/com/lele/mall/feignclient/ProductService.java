package com.lele.mall.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


// name与服务名保持一致
@FeignClient(name = "product",fallback = ProductServiceFallback.class,configuration = ProductServiceFallback.class)
public interface ProductService {

    @GetMapping("/product/reduce_inventory")
    public String reduceInventory();
}

// product的降级服务类
class ProductServiceFallback implements ProductService{

    public String reduceInventory(){
        return "订单模块的库存减一";
    };
}



