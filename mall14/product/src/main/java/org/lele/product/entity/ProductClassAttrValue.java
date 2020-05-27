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
 * 类别属性值表，类别属性表的value(ProductClassAttrValue)表实体类
 *
 * @author lele
 * @since 2020-05-26 14:17:53
 */
@SuppressWarnings("serial")
@ApiModel("类别属性值表，类别属性表的value" )
public class ProductClassAttrValue extends Model<ProductClassAttrValue> {

    @ApiModelProperty(hidden = true)
    private Long id;
    /**类别属性的key*/    
    @ApiModelProperty("类别属性的key")
    private String attrValue;

    @ApiModelProperty(hidden = true)
    private LocalDateTime created;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updated;
    /**类别属性表的id*/    
    @ApiModelProperty("类别属性表的id")
    private Long productClassAttrId;


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