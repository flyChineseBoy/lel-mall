package org.lele.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.common.dao.MUserRoleDao;
import org.lele.common.entity.MUserRole;
import org.lele.common.service.MUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色表(MUserRole)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mUserRoleService")
public class MUserRoleServiceImpl extends ServiceImpl<MUserRoleDao, MUserRole> implements MUserRoleService {

}