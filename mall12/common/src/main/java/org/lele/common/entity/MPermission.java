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
 * 权限表(MPermission)表实体类
 *
 * @author lele
 * @since 2020-05-07 23:38:48
 */
@SuppressWarnings("serial")
@ApiModel("权限表" )
public class MPermission extends Model<MPermission> {
        
    @ApiModelProperty("")
    private Long id;
    /**父权限*/    
    @ApiModelProperty("父权限")
    private Long parentId;
    /**权限名称*/    
    @ApiModelProperty("权限名称")
    private String name;
    /**权限英文名称*/    
    @ApiModelProperty("权限英文名称")
    private String enname;
    /**授权路径*/    
    @ApiModelProperty("授权路径")
    private String url;
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