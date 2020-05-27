package org.lele.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.product.dao.ProductClassAttrValueDao;
import org.lele.product.entity.ProductClassAttrValue;
import org.lele.product.service.ProductClassAttrValueService;
import org.springframework.stereotype.Service;

/**
 * 类别属性值表，类别属性表的value(ProductClassAttrValue)表服务实现类
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Service("productClassAttrValueService")
public class ProductClassAttrValueServiceImpl extends ServiceImpl<ProductClassAttrValueDao, ProductClassAttrValue> implements ProductClassAttrValueService {

}