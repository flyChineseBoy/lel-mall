package org.lele.common.mapper;/*
 * com.lele.common.mapper
 * @author: lele
 * @date: 2020-05-01
 */

import org.lele.common.dao.MUserDao;
import org.lele.common.entity.MUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private MUserDao mUserDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testInsert(){
        MUser user = MUser.builder().username("root").password(bCryptPasswordEncoder.encode("root")).build();
        mUserDao.insert(user);
    }

}
