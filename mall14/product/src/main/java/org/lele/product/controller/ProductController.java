package org.lele.product.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lele.product.dto.request.CreateProductRequest;
import org.lele.product.dto.request.QueryProductRequest;
import org.lele.product.entity.ESProduct;
import org.lele.product.entity.Product;
import org.lele.product.service.ProductService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 商品表(Product)表控制层
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@RestController
@RequestMapping("product")
@Api(tags = "商品API")
public class ProductController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProductService productService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.productService.getById(id));
    }

    /**
     * 修改数据
     *
     * @param product 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody Product product) {
        return success(this.productService.updateById(product));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.productService.removeByIds(idList));
    }


    /**
     * 新增一条商品
     *
     * @param request 商品，包含规格参数
     * @return 新增结果
     */
    @ApiOperation("新增商品，包含商品各种规格/价格/库存，注意新增后可能不能立刻查询到")
    @PostMapping
    public R saveProduct(@RequestBody CreateProductRequest request ) {
        return success(this.productService.saveProduct(request));
    }

    /**
     * 搜索商品
     * @param page 分页对象
     * @param request 查询实体
     * @return 所有数据
     */
    @ApiOperation("ES中查询商品，包含商品各种规格/价格/库存。")
    @PostMapping("/search")
    public R search( QueryProductRequest request) {
        return success(this.productService.search(request));
    }
}