package org.lele.common.mapper;/*
 * com.lele.common.mapper
 * @author: lele
 * @date: 2020-05-01
 */

import org.lele.common.dao.MPermissionDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionMapperTest {

    @Autowired
    MPermissionDao mPermissionDao;



    @Test
    public void testSelectPermissionByUserId(){
        mPermissionDao.selectPermissionByUserId(123L);
    }

}
