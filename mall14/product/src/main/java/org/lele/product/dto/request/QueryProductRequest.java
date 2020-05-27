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
 * @author: lele
 * @date: 2020-05-26
 */
@ApiModel("查询/排序商品Request结构体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryProductRequest extends Product {
    @ApiModelProperty("正序排序的字段")
    private String asc;
    @ApiModelProperty("倒叙排序的字段")
    private String desc;

    @ApiModelProperty("价格搜索：最大区间")
    private String maxPrice;
    @ApiModelProperty("价格搜索：最小区间")
    private String minPrice;

    @ApiModelProperty(value = "必填：分页offset",required = true)
    private Integer offset;
    @ApiModelProperty(value = "必填：分页pagesize",required = true)
    private Integer pagesize;
}
