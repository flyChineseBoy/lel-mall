package org.lele.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 用户表(MUser)表实体类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@SuppressWarnings("serial")
public class MUser extends Model<MUser> {
    
    private Long id;
    //用户名
    private String username;

    /**
     *密码，加密存储
     */
    @TableField( el = "password,typeHandler = BCryptHandler")
    private String password;
    //注册手机号
    private String phone;
    
    private Date created;
    
    private Date updated;


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