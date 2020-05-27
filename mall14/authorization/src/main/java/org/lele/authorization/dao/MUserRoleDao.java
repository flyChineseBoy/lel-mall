package org.lele.authorization.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lele.common.entity.MUserRole;

/**
 * 用户角色表(MUserRole)表数据库访问层
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Mapper
public interface MUserRoleDao extends BaseMapper<MUserRole> {

}