package org.lele.common.security;/*
 * com.lele.common.service
 * @author: lele
 * @date: 2020-05-01
 */

import org.lele.common.dao.MPermissionDao;
import org.lele.common.dao.MUserDao;
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
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class UserDetailServiceImpl  implements UserDetailsService{

    @Autowired
    private MUserDao mUserDao;

    @Autowired
    private MPermissionDao mPermissionDao;

    /**
     * 用于spring security认证用户
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        List<MUser> mUsers = mUserDao.selectByMap(new HashMap(){{put("username",s);}} );
        if( CollectionUtils.isEmpty(mUsers) ) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<MPermission> permissionList = mPermissionDao.selectPermissionByUserId( mUsers.get(0).getId() );

        List<GrantedAuthority> permisss =
                permissionList.stream().map(permission -> new SimpleGrantedAuthority(permission.getUrl()))
                .collect(Collectors.toList());

        // 用户名，密码，资源。
        UserDTO user = new UserDTO(s,mUsers.get(0).getPassword(),permisss);
        BeanUtils.copyProperties( mUsers.get(0),user );
        return user;
    }




}
