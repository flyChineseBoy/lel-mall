package com.lele.mall;/*
 * com.lele.mall
 * @author: lele
 * @date: 2020-04-19
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MyUserDetailsService implements UserDetailsService {

    /**
     * 这里应该实现自定义认证逻辑：通过username查库找到user
     * @param s
     * @return 返回一个Srping Security规定的UserDetails，包含密码和用户权限
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if( !s.equals("admin") )
            throw new UsernameNotFoundException(s + "用户名不存在");

        // 资源权限，之后可以通过这里赋予的权限控制接口访问。
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority( "ROLE_ADMIN"));
        //roles.add(new SimpleGrantedAuthority( "normal"));

        return new User(s,"admin",roles);
    }
}
