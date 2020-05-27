package org.lele.authorization.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.lele.common.entity.MUser;

/**
 * 用户表(MUser)表数据库访问层
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Mapper
public interface MUserDao extends BaseMapper<MUser> {
    MUser selectByUserName(String username);
}