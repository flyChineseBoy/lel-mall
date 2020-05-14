package org.lele.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.common.dao.MUserDao;
import org.lele.common.dto.UserDTO;
import org.lele.common.entity.MUser;
import org.lele.common.service.MUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * 用户表(MUser)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mUserService")
public class MUserServiceImpl extends ServiceImpl<MUserDao, MUser> implements MUserService {


}