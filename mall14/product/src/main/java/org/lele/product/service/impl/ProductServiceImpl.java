package org.lele.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.lele.product.dao.ProductDao;
import org.lele.product.dao.ProductSpecsDao;
import org.lele.product.dto.request.CreateProductRequest;
import org.lele.product.dto.request.QueryProductRequest;
import org.lele.product.entity.ESProduct;
import org.lele.product.entity.Product;
import org.lele.product.repository.ProductRepository;
import org.lele.product.service.ProductService;
import org.lele.product.utils.converts.ESConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品表(Product)表服务实现类
 *
 * @author lele
 * @since 2020-05-25 20:43:28
 */
@Transactional(rollbackFor = Exception.class)
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductDao, Product> implements ProductService {

    @Autowired
    private ProductSpecsDao productSpecsDao;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean saveProduct(CreateProductRequest request) {
        // 商品入数据库，规格入数据库
        Product product = new Product();
        BeanUtils.copyProperties( request,product );
        baseMapper.insert( product );

        Optional.ofNullable( request.getSpecs() )
                .ifPresent( productSpecs ->
                    productSpecs.stream().forEach( spec->{
                        spec.setProductId( product.getId() );
                        productSpecsDao.insert( spec );
                    }
                 )
             );

        // 后续考虑解耦
        // 商品入es
        ESProduct esProduct = ESConverter.convertProduct( product );
        productRepository.save( esProduct );
        return true;
    }

    @Override
    public List<ESProduct> search(QueryProductRequest request) {
        List<ESProduct> result;
        if( request==null ){
            result = productRepository.findAll( PageRequest.of(request.getOffset(),request.getPagesize())).getContent();
        }else{
            result = productRepository.search( buildCondition(request).build().getQuery(),PageRequest.of(request.getOffset(),request.getPagesize()) ).getContent();
        }
        return result;
    }

    /**
     *  request.name 对商品名精确查
     *  request.keywords 对keywords近语义查，该关键字类似淘宝的20字关键字描述
     *  request.productClassName 按商品分类查
     *  request.minPrice && request.maxPrice 按照价格区间查
     * @return
     */
    NativeSearchQueryBuilder buildCondition(QueryProductRequest request){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if( StringUtils.isNotBlank(request.getName()) ){
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("name.keyword", request.getName()));}
        if( StringUtils.isNotBlank(request.getKeywords()) ){
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.matchQuery("keywords", request.getKeywords()));}
        if( StringUtils.isNotBlank(request.getProductClassName()) ){
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.termQuery("productClassName.keyword", request.getProductClassName()));}
        if( request.getMinPrice()!=null && request.getMaxPrice()!=null){
            boolQueryBuilder = boolQueryBuilder.must(QueryBuilders.rangeQuery("price").from(request.getMinPrice()).to(request.getMaxPrice()));}

        if( StringUtils.isNotBlank(request.getAsc()) ){
            queryBuilder.withSort( SortBuilders.fieldSort(request.getAsc()).order(SortOrder.ASC) );}
        if( StringUtils.isNotBlank(request.getDesc()) ){
            queryBuilder.withSort( SortBuilders.fieldSort(request.getDesc()).order(SortOrder.DESC) );}

        return queryBuilder.withQuery( boolQueryBuilder );
    }
}