package org.lele.user.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.lele.common.entity.MUserRole;
import org.lele.user.service.MUserRoleService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 用户角色表(MUserRole)表控制层
 *
 * @author lele
 * @since 2020-05-07 20:55:22
 */
@Api(tags = "用户-角色接口")
@RestController
@RequestMapping("mUserRole")
public class MUserRoleController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MUserRoleService mUserRoleService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param mUserRole 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(@ApiIgnore Page<MUserRole> page, MUserRole mUserRole) {
        return success(this.mUserRoleService.page(page, new QueryWrapper<>(mUserRole)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.mUserRoleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param mUserRole 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody MUserRole mUserRole) {
        return success(this.mUserRoleService.save(mUserRole));
    }

    /**
     * 修改数据
     *
     * @param mUserRole 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody MUserRole mUserRole) {
        return success(this.mUserRoleService.updateById(mUserRole));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.mUserRoleService.removeByIds(idList));
    }
}