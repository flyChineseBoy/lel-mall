package org.lele.common.aspect;

import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.lele.common.constant.LogConstant;
import org.lele.common.dto.UserDTO;
import org.lele.common.entity.SystemLog;
import org.lele.common.security.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.lele.common.repository.SystemLogRepository;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * org.lele.common.aspect
 *  拦截所有请求，记录用户/管理员行为
 *  aspect三部曲：@Aspect、@Pointcut、@Advice（@Aroud、@Befor、@After）
 * @author: lele
 * @date: 2020-05-13
 */
@Aspect
@Component
public class SystemLogAspect {
    @Autowired
    SystemLogRepository systemLogRepository;
    @Autowired
    SessionUtils sessionUtils;

    @Pointcut("within(org.lele.*.controller..*)")
    private void logPointCut(){}

    @Before("logPointCut()")
    public void before(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();
        SystemLog log = SystemLog.builder()
                .id( UUID.randomUUID().toString() )
                .type(LogConstant.LogType.BEFORE )
                .logTime( System.currentTimeMillis() )
                .sourceUrl( request.getRemoteAddr() )
                .userDetails( userDTO.toString() )
                .requestUrl( HostNameUtil.getIp() )
                .requestMethod( jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()" )
                .requestParam( JSONObject.toJSONString( jp.getArgs() ) )
                .build();
        systemLogRepository.save(log);
    }

    @AfterReturning(value = "logPointCut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        SystemLog log = SystemLog.builder()
                .id( UUID.randomUUID().toString())
                .type(LogConstant.LogType.AFTER_RETURN )
                .logTime( System.currentTimeMillis() )
                .sourceUrl( request.getRemoteAddr() )
                .userDetails( userDTO.toString() )
                .requestUrl( HostNameUtil.getIp() )
                .requestMethod( jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()" )
                .requestParam( JSONObject.toJSONString( jp.getArgs() ) )
                .result( JSONObject.toJSONString(result) )
                .build();
        systemLogRepository.save(log);
    }

    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThrowing(JoinPoint jp,Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        SystemLog log = SystemLog.builder()
                .id( UUID.randomUUID().toString() )
                .type(LogConstant.LogType.AFTER_EXCEPTION )
                .logTime( System.currentTimeMillis() )
                .sourceUrl( request.getRemoteAddr() )
                .userDetails( userDTO.toString() )
                .requestUrl( HostNameUtil.getIp() )
                .requestMethod( jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()" )
                .requestParam( JSONObject.toJSONString( jp.getArgs() ) )
                .errMessage( e.getMessage() )
                .build();
        systemLogRepository.save(log);
    }



}
