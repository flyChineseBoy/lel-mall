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
 * 权限表(MPermission)表实体类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@SuppressWarnings("serial")
public class MPermission extends Model<MPermission> {
    
    private Long id;
    //父权限
    private Long parentId;
    //权限名称
    private String name;
    //权限英文名称
    private String enname;
    //授权路径
    private String url;
    //备注
    private String description;
    
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