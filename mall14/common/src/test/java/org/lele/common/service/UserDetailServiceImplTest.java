package org.lele.common.service;/*
 * com.lele.common.mapper
 * @author: lele
 * @date: 2020-05-01
 */

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailServiceImplTest {

    @Autowired
    UserDetailsService userDetailsService;



    @Test
    public void testLoadUserByUsername(){
        System.out.println(JSONObject.toJSON(userDetailsService.loadUserByUsername("root")));
    }

}
