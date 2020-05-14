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
 * 用户角色表(MUserRole)表实体类
 *
 * @author lele
 * @since 2020-05-07 23:38:53
 */
@SuppressWarnings("serial")
@ApiModel("用户角色表" )
public class MUserRole extends Model<MUserRole> {
        
    @ApiModelProperty("")
    private Long id;
    /**用户 ID*/    
    @ApiModelProperty("用户 ID")
    private Long userId;
    /**角色 ID*/    
    @ApiModelProperty("角色 ID")
    private Long roleId;
        
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