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
 * 商品类别(ProductClass)表实体类
 *
 * @author lele
 * @since 2020-05-26 14:17:51
 */
@SuppressWarnings("serial")
@ApiModel("商品类别" )
public class ProductClass extends Model<ProductClass> {

    @ApiModelProperty(hidden = true)
    private Long id;
    /**类别名称*/    
    @ApiModelProperty("类别名称")
    private String name;

    @ApiModelProperty(hidden = true)
    private LocalDateTime created;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updated;


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