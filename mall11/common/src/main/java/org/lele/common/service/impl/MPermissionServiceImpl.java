package org.lele.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.lele.common.dao.MPermissionDao;
import org.lele.common.dto.UserDTO;
import org.lele.common.entity.MPermission;
import org.lele.common.service.MPermissionService;
import org.lele.common.service.MUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限表(MPermission)表服务实现类
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@Service("mPermissionService")
public class MPermissionServiceImpl extends ServiceImpl<MPermissionDao, MPermission> implements MPermissionService {
}