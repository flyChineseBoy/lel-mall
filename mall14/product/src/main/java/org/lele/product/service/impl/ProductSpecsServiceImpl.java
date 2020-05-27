package org.lele.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.product.dao.ProductSpecsDao;
import org.lele.product.entity.ProductSpecs;
import org.lele.product.service.ProductSpecsService;
import org.springframework.stereotype.Service;

/**
 * 商品规格表，记录sku数据和对应价格、库存数量(ProductSpecs)表服务实现类
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Service("productSpecsService")
public class ProductSpecsServiceImpl extends ServiceImpl<ProductSpecsDao, ProductSpecs> implements ProductSpecsService {

}