package org.lele.authorization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.lele.authorization.dao.MUserDao;
import org.lele.authorization.service.MUserService;
import org.lele.common.entity.MUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 用户表(MUser)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mUserService")
public class MUserServiceImpl extends ServiceImpl<MUserDao, MUser> implements MUserService {

    @Autowired
    private MUserDao mUserDao;
    @Override

    public MUser selectByUserName(String username) {
        if(StringUtils.isBlank(username)) {return null;}
        return mUserDao.selectByUserName(username);
    }
}