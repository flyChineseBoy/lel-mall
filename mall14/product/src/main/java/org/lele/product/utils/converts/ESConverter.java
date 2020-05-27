package org.lele.product.utils.converts;

import org.lele.product.entity.ESProduct;
import org.lele.product.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * org.lele.product.utils.converts
 *
 * @author: lele
 * @date: 2020-05-26
 */
public class ESConverter {
    /**
     * 两边结构一致，暂时捡漏实现
      */
    public static ESProduct convertProduct( Product product ){
        ESProduct esProduct = new ESProduct();
        BeanUtils.copyProperties( product,esProduct );
        esProduct.setId( product.getId().toString() );
        return esProduct;
    }
}
