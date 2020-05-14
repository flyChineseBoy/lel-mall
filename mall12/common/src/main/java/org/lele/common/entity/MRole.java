package org.lele.common.entity;

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
 * 角色表(MRole)表实体类
 *
 * @author lele
 * @since 2020-05-07 23:38:50
 */
@SuppressWarnings("serial")
@ApiModel("角色表" )
public class MRole extends Model<MRole> {
        
    @ApiModelProperty("")
    private Long id;
    /**父角色*/    
    @ApiModelProperty("父角色")
    private Long parentId;
    /**角色名称*/    
    @ApiModelProperty("角色名称")
    private String name;
    /**角色英文名称*/    
    @ApiModelProperty("角色英文名称")
    private String enname;
    /**备注*/    
    @ApiModelProperty("备注")
    private String description;
        
    @ApiModelProperty("")
    private LocalDateTime created;
        
    @ApiModelProperty("")
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