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
 * 角色权限表(MRolePermission)表实体类
 *
 * @author lele
 * @since 2020-05-07 23:38:51
 */
@SuppressWarnings("serial")
@ApiModel("角色权限表" )
public class MRolePermission extends Model<MRolePermission> {
        
    @ApiModelProperty("")
    private Long id;
    /**角色 ID*/    
    @ApiModelProperty("角色 ID")
    private Long roleId;
    /**权限 ID*/    
    @ApiModelProperty("权限 ID")
    private Long permissionId;
        
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