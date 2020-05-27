package org.lele.product.repository;

import org.lele.product.entity.ESProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * org.lele.product.repository
 *
 * @author: lele
 * @date: 2020-05-26
 */
@Repository
public interface ProductRepository  extends ElasticsearchRepository<ESProduct,String> {
}
