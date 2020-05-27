package org.lele.product.dao;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.lele.product.entity.ProductClassAttrValue;

/**
 * 类别属性值表，类别属性表的value(ProductClassAttrValue)表数据库访问层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Mapper
public interface ProductClassAttrValueDao extends BaseMapper<ProductClassAttrValue> {

}