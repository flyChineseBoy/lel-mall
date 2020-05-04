package org.lele.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.common.dao.MPermissionDao;
import org.lele.common.entity.MPermission;
import org.lele.common.service.MPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限表(MPermission)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mPermissionService")
public class MPermissionServiceImpl extends ServiceImpl<MPermissionDao, MPermission> implements MPermissionService {

}