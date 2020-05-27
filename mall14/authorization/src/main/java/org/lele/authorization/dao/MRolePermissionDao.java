package org.lele.authorization.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lele.common.entity.MRolePermission;

/**
 * 角色权限表(MRolePermission)表数据库访问层
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Mapper
public interface MRolePermissionDao extends BaseMapper<MRolePermission> {

}