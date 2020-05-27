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
 * 商品表(Product)表实体类
 *
 * @author lele
 * @since 2020-05-26 15:16:35
 */
@SuppressWarnings("serial")
@ApiModel("商品表" )
public class Product extends Model<Product> {

    @ApiModelProperty(hidden = true)
    private Long id;
    /**商品名*/    
    @ApiModelProperty("商品名")
    private String name;

    @ApiModelProperty(hidden = true)
    private LocalDateTime created;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updated;
    /**商品主图集合*/    
    @ApiModelProperty("商品主图集合")
    private String picUrls;
    /**商品搜索关键字，最多20个汉字*/    
    @ApiModelProperty("商品搜索关键字，最多20个汉字")
    private String keywords;
    /**商品的描述文*/    
    @ApiModelProperty("商品的描述文")
    private String description;
    /**是否上架，默认1位上架*/
    @ApiModelProperty(value = "是否上架，默认1位上架",hidden = true)
    private Integer listing;
    /**是否已被删除，默认0没有被删除*/    
    @ApiModelProperty(value = "是否已被删除，默认0没有被删除",hidden = true)
    private Integer deleted;
    /**所属类别的id*/    
    @ApiModelProperty("所属类别的id")
    private Long productClassId;
    /**所属类别的名称*/    
    @ApiModelProperty("所属类别的名称")
    private String productClassName;
    /**展示在商品列表的商品价格，不是用户实际要支付的价格*/    
    @ApiModelProperty("展示在商品列表的商品价格，不是用户实际要支付的价格")
    private Double price;


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