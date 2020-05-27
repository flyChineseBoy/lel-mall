package org.lele.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.product.dao.ProductClassDao;
import org.lele.product.entity.ProductClass;
import org.lele.product.service.ProductClassService;
import org.springframework.stereotype.Service;

/**
 * 商品类别(ProductClass)表服务实现类
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Service("productClassService")
public class ProductClassServiceImpl extends ServiceImpl<ProductClassDao, ProductClass> implements ProductClassService {

}