package org.lele.product.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.lele.product.entity.ProductClass;

/**
 * 商品类别(ProductClass)表数据库访问层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Mapper
public interface ProductClassDao extends BaseMapper<ProductClass> {

}