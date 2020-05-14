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
 * 用户表(MUser)表实体类
 *
 * @author lele
 * @since 2020-05-07 23:38:52
 */
@SuppressWarnings("serial")
@ApiModel("用户表" )
public class MUser extends Model<MUser> {
        
    @ApiModelProperty("")
    private Long id;
    /**用户名*/    
    @ApiModelProperty("用户名")
    private String username;
    /**密码，加密存储*/    
    @ApiModelProperty("密码，加密存储")
    private String password;
    /**注册手机号*/    
    @ApiModelProperty("注册手机号")
    private String phone;
        
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