package org.lele.product.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 商品规格表，记录sku数据和对应价格、库存数量(ProductSpecs)表实体类
 *
 * @author lele
 * @since 2020-05-26 14:17:54
 */
@SuppressWarnings("serial")
@ApiModel("商品规格表，记录sku数据和对应价格、库存数量" )
public class ProductSpecs extends Model<ProductSpecs> {

    @ApiModelProperty(hidden = true)
    private Long id;
    /**规格，json存储，key和value来自类别属性表、类别属性值表*/    
    @ApiModelProperty("规格，json存储，key和value来自类别属性表、类别属性值表")
    private String productSpecs;

    @ApiModelProperty(hidden = true)
    private LocalDateTime created;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updated;
    /**这种规格下的商品价格*/    
    @ApiModelProperty("这种规格下的商品价格")
    private Double price;
    /**库存数量*/    
    @ApiModelProperty("库存数量")
    private Long stock;
    /**商品id*/    
    @ApiModelProperty("商品id")
    private Long productId;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }