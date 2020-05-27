package org.lele.user.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lele.common.entity.MRole;
import org.lele.user.service.MRoleService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 角色表(MRole)表控制层
 *
 * @author lele
 * @since 2020-05-07 20:54:28
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("mRole")
public class MRoleController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private MRoleService mRoleService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param mRole 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation(value = "查询所有角色")
    public R selectAll(@ApiIgnore Page<MRole> page, MRole mRole) {
        return success(this.mRoleService.page(page, new QueryWrapper<>(mRole)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.mRoleService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param mRole 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增一个角色")
    public R insert(@RequestBody MRole mRole) {
        return success(this.mRoleService.save(mRole));
    }

    /**
     * 修改数据
     *
     * @param mRole 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody MRole mRole) {
        return success(this.mRoleService.updateById(mRole));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "根据id批量删除角色")
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.mRoleService.removeByIds(idList));
    }
}