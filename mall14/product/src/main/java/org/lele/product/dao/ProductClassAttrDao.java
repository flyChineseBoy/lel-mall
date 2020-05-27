package org.lele.product.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.lele.product.entity.ProductClassAttr;

/**
 * 类别属性表，属于商品类别的一个属性，没有值只有属性key(ProductClassAttr)表数据库访问层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Mapper
public interface ProductClassAttrDao extends BaseMapper<ProductClassAttr> {

}