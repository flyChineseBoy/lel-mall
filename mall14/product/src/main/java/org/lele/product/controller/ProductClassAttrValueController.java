package org.lele.product.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.lele.product.entity.ProductClassAttrValue;
import org.lele.product.service.ProductClassAttrValueService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 类别属性值表，类别属性表的value(ProductClassAttrValue)表控制层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@RestController
@RequestMapping("productClassAttrValue")
public class ProductClassAttrValueController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProductClassAttrValueService productClassAttrValueService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param productClassAttrValue 查询实体
     * @return 所有数据
     */
    @ApiOperation("查看商品属性键值对")
    @GetMapping
    public R selectAll(@ApiIgnore Page<ProductClassAttrValue> page, ProductClassAttrValue productClassAttrValue) {
        return success(this.productClassAttrValueService.page(page, new QueryWrapper<>(productClassAttrValue)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.productClassAttrValueService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param productClassAttrValue 实体对象
     * @return 新增结果
     */
    @ApiOperation("新增商品属性值")
    @PostMapping
    public R insert(@RequestBody ProductClassAttrValue productClassAttrValue) {
        return success(this.productClassAttrValueService.save(productClassAttrValue));
    }

    /**
     * 修改数据
     *
     * @param productClassAttrValue 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody ProductClassAttrValue productClassAttrValue) {
        return success(this.productClassAttrValueService.updateById(productClassAttrValue));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.productClassAttrValueService.removeByIds(idList));
    }
}