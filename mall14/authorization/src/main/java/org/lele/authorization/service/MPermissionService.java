package org.lele.authorization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lele.common.entity.MPermission;

import java.util.List;

public interface MPermissionService extends IService<MPermission> {
    List<MPermission> selectPermissionByUserId(Long userid);
}