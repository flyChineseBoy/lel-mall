package org.lele.common.security;

import org.lele.common.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * org.lele.common.security
 *
 * @author: lele
 * @date: 2020-05-10
 */
@Component
public class SessionUtils {
    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * 获取当前session用户。
     * @return TODO UserDTO 为继承自spring security User且包含MUser业务字段的实体。
     */
    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object myUser = (auth != null) ? auth.getPrincipal() : null;
        return (UserDTO)myUser;
    }

    /**
     * 加载当前所有登录用户
     *
     */
    public List getAllSessionUser() {
        return sessionRegistry.getAllPrincipals();
    }
}
