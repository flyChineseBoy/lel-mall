package org.lele.product.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.lele.product.entity.ProductSpecs;

/**
 * 商品规格表，记录sku数据和对应价格、库存数量(ProductSpecs)表数据库访问层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Mapper
public interface ProductSpecsDao extends BaseMapper<ProductSpecs> {

}