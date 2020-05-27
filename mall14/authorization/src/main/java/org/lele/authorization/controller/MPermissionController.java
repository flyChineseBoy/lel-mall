package org.lele.authorization.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lele.authorization.service.MPermissionService;
import org.lele.common.entity.MPermission;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 权限表(MPermission)表控制层
 *
 * @author lele
 * @since 2020-05-07 20:53:46
 */
@Api(tags = "权限接口")
@RestController
@RequestMapping("mPermission")
public class MPermissionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MPermissionService mPermissionService;


    @GetMapping("selectPermissionByUserId/{userid}")
    public List<MPermission> selectPermissionByUserId(@PathVariable("userid") Long userid) {
        return this.mPermissionService.selectPermissionByUserId(userid);
    }

}