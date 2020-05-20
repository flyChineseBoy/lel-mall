package org.lele.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lele.common.dto.UserDTO;
import org.lele.common.security.SessionUtils;
import org.lele.common.service.MUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * org.lele.user.controller
 * @author: lele
 * @date: 2020-05-07
 */
@RestController
@RequestMapping("currentUser")
@Api(tags = "当前用户相关操作")
public class CurrentUserController {
    @Autowired
    private SessionUtils sessionUtils;

    @ApiOperation("返回当前对象")
    @GetMapping("getCurrentUser")
    public UserDTO getCurrentUser(){
        return sessionUtils.getCurrentUser();
    }
}
