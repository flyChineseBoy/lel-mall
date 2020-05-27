package org.lele.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.product.dao.ProductClassAttrDao;
import org.lele.product.entity.ProductClassAttr;
import org.lele.product.service.ProductClassAttrService;
import org.springframework.stereotype.Service;

/**
 * 类别属性表，属于商品类别的一个属性，没有值只有属性key(ProductClassAttr)表服务实现类
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Service("productClassAttrService")
public class ProductClassAttrServiceImpl extends ServiceImpl<ProductClassAttrDao, ProductClassAttr> implements ProductClassAttrService {

}