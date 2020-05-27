package org.lele.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.authorization.dao.MRoleDao;
import org.lele.authorization.service.MRoleService;
import org.lele.common.entity.MRole;
import org.springframework.stereotype.Service;

/**
 * 角色表(MRole)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mRoleService")
public class MRoleServiceImpl extends ServiceImpl<MRoleDao, MRole> implements MRoleService {

}