package org.lele.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.lele.common.entity.MUser;
import org.lele.user.dao.MUserDao;
import org.lele.user.service.MUserService;
import org.springframework.beans.factory.annotation.Autowired;
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