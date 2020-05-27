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
import springfox.documentation.annotations.ApiIgnore;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 类别属性表，属于商品类别的一个属性，没有值只有属性key(ProductClassAttr)表实体类
 *
 * @author lele
 * @since 2020-05-26 14:17:52
 */
@SuppressWarnings("serial")
@ApiModel("类别属性表，属于商品类别的一个属性，没有值只有属性key" )
public class ProductClassAttr extends Model<ProductClassAttr> {

    @ApiModelProperty(hidden = true)
    private Long id;
    /**类别属性的key*/    
    @ApiModelProperty("类别属性的key")
    private String attrKey;
        
    @ApiModelProperty(hidden = true)
    private LocalDateTime created;
        
    @ApiModelProperty(hidden = true)
    private LocalDateTime updated;
    /**分类id*/

    @ApiModelProperty(value = "分类id")
    private Long productClassId;


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