package org.lele.common.entity;

import java.util.Date;
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
 * 角色权限表(MRolePermission)表实体类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@SuppressWarnings("serial")
public class MRolePermission extends Model<MRolePermission> {
    
    private Long id;
    //角色 ID
    private Long roleId;
    //权限 ID
    private Long permissionId;
    
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