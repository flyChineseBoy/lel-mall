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
 * 角色表(MRole)表实体类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@SuppressWarnings("serial")
public class MRole extends Model<MRole> {
    
    private Long id;
    //父角色
    private Long parentId;
    //角色名称
    private String name;
    //角色英文名称
    private String enname;
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