package org.lele.authorization.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import org.lele.common.entity.MUser;

public interface MUserService extends IService<MUser> {
    MUser selectByUserName(String username);
}