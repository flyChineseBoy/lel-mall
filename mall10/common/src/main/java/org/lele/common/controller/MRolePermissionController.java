package org.lele.common.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.lele.common.entity.MRolePermission;
import org.lele.common.service.MRolePermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 角色权限表(MRolePermission)表控制层
 *
 * @author lele
 * @since 2020-05-02 13:23:09
 */
@RestController
@RequestMapping("mRolePermission")
public class MRolePermissionController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MRolePermissionService mRolePermissionService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param mRolePermission 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<MRolePermission> page, MRolePermission mRolePermission) {
        return success(this.mRolePermissionService.page(page, new QueryWrapper<>(mRolePermission)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.mRolePermissionService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param mRolePermission 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody MRolePermission mRolePermission) {
        return success(this.mRolePermissionService.save(mRolePermission));
    }

    /**
     * 修改数据
     *
     * @param mRolePermission 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody MRolePermission mRolePermission) {
        return success(this.mRolePermissionService.updateById(mRolePermission));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.mRolePermissionService.removeByIds(idList));
    }
}