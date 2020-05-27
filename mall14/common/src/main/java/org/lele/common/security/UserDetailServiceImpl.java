package org.lele.common.security;/*
 * com.lele.common.service
 * @author: lele
 * @date: 2020-05-01
 */

import com.alibaba.csp.sentinel.util.StringUtil;
import org.lele.common.authorization.AuthorizationUserService;
import org.lele.common.dto.UserDTO;
import org.lele.common.entity.MPermission;
import org.lele.common.entity.MUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class UserDetailServiceImpl  implements UserDetailsService{

    @Autowired
    private AuthorizationUserService authorizationUserService;
    /**
     * 用于spring security认证用户
     * 依赖authorization认证中心
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        MUser mUser = authorizationUserService.selectByUserName( s );
        if(StringUtil.isBlank( mUser.getUsername() )  ) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<MPermission> permissionList = authorizationUserService.selectPermissionByUserId( mUser.getId() );

        List<GrantedAuthority> permisss =
                permissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getUrl()))
                .collect(Collectors.toList());

        // 用户名，密码，资源。
        UserDTO user = new UserDTO(s,mUser.getPassword(),permisss);
        BeanUtils.copyProperties( mUser,user );
        return user;
    }




}
