package org.lele.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.authorization.dao.MPermissionDao;
import org.lele.authorization.service.MPermissionService;
import org.lele.common.entity.MPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限表(MPermission)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mPermissionService")
public class MPermissionServiceImpl extends ServiceImpl<MPermissionDao, MPermission> implements MPermissionService {
    @Autowired
    private MPermissionDao mPermissionDao;
    @Override
    public List<MPermission> selectPermissionByUserId(Long userid) {
        return mPermissionDao.selectPermissionByUserId(userid);
    }
}