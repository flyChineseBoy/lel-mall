package org.lele.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.common.entity.MRolePermission;
import org.lele.user.dao.MRolePermissionDao;
import org.lele.user.service.MRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 角色权限表(MRolePermission)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mRolePermissionService")
public class MRolePermissionServiceImpl extends ServiceImpl<MRolePermissionDao, MRolePermission> implements MRolePermissionService {

}