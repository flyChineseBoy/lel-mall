package org.lele.product.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.lele.product.entity.ProductClassAttr;
import org.lele.product.service.ProductClassAttrService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 类别属性表，属于商品类别的一个属性，没有值只有属性key(ProductClassAttr)表控制层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@RestController
@RequestMapping("productClassAttr")
public class ProductClassAttrController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProductClassAttrService productClassAttrService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param productClassAttr 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(@ApiIgnore Page<ProductClassAttr> page, ProductClassAttr productClassAttr) {
        return success(this.productClassAttrService.page(page, new QueryWrapper<>(productClassAttr)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.productClassAttrService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param productClassAttr 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增商品属性")
    @PostMapping
    public R insert(@RequestBody ProductClassAttr productClassAttr) {
        return success(this.productClassAttrService.save(productClassAttr));
    }

    /**
     * 修改数据
     *
     * @param productClassAttr 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody ProductClassAttr productClassAttr) {
        return success(this.productClassAttrService.updateById(productClassAttr));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.productClassAttrService.removeByIds(idList));
    }
}