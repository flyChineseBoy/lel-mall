package org.lele.common.repository;

import org.lele.common.entity.SystemLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * org.lele.common.repository
 *  写访问日志到es
 * @author: lele
 * @date: 2020-05-18
 */
@Repository
public interface SystemLogRepository extends ElasticsearchCrudRepository<SystemLog,String> {
}
