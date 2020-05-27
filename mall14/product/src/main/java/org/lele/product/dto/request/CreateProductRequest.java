package org.lele.product.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lele.product.entity.Product;
import org.lele.product.entity.ProductSpecs;

import java.util.List;

/**
 * org.lele.product.dto.request
 *
 * @author: lele
 * @date: 2020-05-26
 */
@ApiModel("创建商品Request结构体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest extends Product {
    @ApiModelProperty("商品的规格参数以及其对应的价格/库存")
    List<ProductSpecs> specs;
}
