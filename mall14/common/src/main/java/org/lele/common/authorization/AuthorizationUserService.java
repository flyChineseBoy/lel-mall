package org.lele.common.authorization;

import org.lele.common.entity.MPermission;
import org.lele.common.entity.MUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * org.lele.common.authorization
 *
 * @author: lele
 * @date: 2020-05-25
 */
@FeignClient(name = "authorization")
//@Service
public interface AuthorizationUserService {

    @GetMapping("/mUser/selectByUserName/{username}")
    public MUser selectByUserName(@PathVariable String username) ;
    @GetMapping("/mPermission/selectPermissionByUserId/{userid}")
    public List<MPermission> selectPermissionByUserId(@PathVariable("userid") Long userid);

}
