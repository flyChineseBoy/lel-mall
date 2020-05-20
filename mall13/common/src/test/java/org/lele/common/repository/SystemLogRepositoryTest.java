package org.lele.common.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lele.common.constant.LogConstant;
import org.lele.common.entity.SystemLog;
import org.lele.common.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * org.lele.repository
 *
 * @author: lele
 * @date: 2020-05-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemLogRepositoryTest {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Test
    public void testSave(){
        SystemLog log = SystemLog.builder()
                .id( UUID.randomUUID().toString() )
                .sourceUrl( "1" )
                .userDetails( "1" )
                .requestUrl( "1")
                .requestMethod( "1")
                .requestParam( "1")
                .type(LogConstant.LogType.AFTER_EXCEPTION)
                .build();
        systemLogRepository.save( log );
    }
}
