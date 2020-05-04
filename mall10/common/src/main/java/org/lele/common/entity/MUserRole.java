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
 * 用户角色表(MUserRole)表实体类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@SuppressWarnings("serial")
public class MUserRole extends Model<MUserRole> {
    
    private Long id;
    //用户 ID
    private Long userId;
    //角色 ID
    private Long roleId;
    
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