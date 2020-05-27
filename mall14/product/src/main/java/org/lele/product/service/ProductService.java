package org.lele.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lele.product.dto.request.CreateProductRequest;
import org.lele.product.dto.request.QueryProductRequest;
import org.lele.product.entity.ESProduct;
import org.lele.product.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {
    boolean saveProduct( CreateProductRequest request );
    List<ESProduct> search(QueryProductRequest request);
}