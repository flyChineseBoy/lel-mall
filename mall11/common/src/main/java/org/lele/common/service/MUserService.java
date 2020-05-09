package org.lele.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lele.common.dto.UserDTO;
import org.lele.common.entity.MUser;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.User;

public interface MUserService extends IService<MUser> {
    public UserDTO getCurrentUser();
}