package org.lele.common.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Iterator;

/**
 * org.lele.common.security
 *  权限决策器，自定义 "是否拥有权限" 的规则
 * @author: lele
 * @date: 2020-05-08
 */
@Component
public class MallAccessDecisionManager implements AccessDecisionManager {

    /**
     *  若用户的权限中包含当前路径所需权限，则可以通过，否则认证异常
     * @param authentication 用户认证信息，包含用户所拥有的权限
     * @param object
     * @param configAttributes 访问当前路径所需要的权限
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes==null){
            return;
        }
        String requestUrl = ((FilterInvocation)object).getRequestUrl() ;

        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            // 字符串等于匹配 TODO  动态url
            if( authority.getAuthority().equals( requestUrl )) { return; }
            // ant匹配
            if( antPathMatcher.match(authority.getAuthority(),requestUrl) ) {return;}
            // 对已登录用户放开属于当前服务器静态资源，其实只有swagger资源
            boolean hasPower = authentication.isAuthenticated() && !authority.getAuthority().equals("ROLE_ANONYMOUS")
                    && (antPathMatcher.match("/webjars/**",requestUrl) ||
                    (antPathMatcher.match("/swagger-resources",requestUrl))
                    || (antPathMatcher.match("/v2/api-docs",requestUrl))
                    || (antPathMatcher.match("/doc.html",requestUrl))
            );
            if( hasPower  ) {
                return;
            }
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
}
