package org.lele.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.flywaydb.core.api.android.ContextHolder;
import org.lele.common.dto.UserDTO;
import org.lele.common.security.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

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
    SessionUtils sessionUtils;
    @Pointcut("within(org.lele.*.controller..*)")
    private void logPointCut(){}

    @Before("logPointCut()")
    public void before(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——请求信息：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("请求方法:" + (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()"));
        System.out.println("请求参数:" + (jp.getArgs().toString()));
    }

    @AfterReturning(value = "logPointCut()",returning = "result")
    public void afterReturning(JoinPoint jp,Object result) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——返回结果：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("返回数据:" + JSONObject.toJSONString(result) );
    }

    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThrowing(JoinPoint jp,Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        UserDTO userDTO = sessionUtils.getCurrentUser();

        System.out.println( (jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()——异常结果：") );
        System.out.println("请求地址:" + request.getRemoteAddr());
        System.out.println("请求人:" + userDTO.toString() );
        System.out.println("异常信息:" + e.getMessage() );
    }
}
