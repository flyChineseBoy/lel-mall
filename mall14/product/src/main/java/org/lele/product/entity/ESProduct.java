package org.lele.product.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lele.common.constant.FieldAnalyzer;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * org.lele.product.entity
 *
 * @author: lele
 * @date: 2020-05-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("ES存储product结构")
@Document(indexName = "products",type = "product")
public class ESProduct {

    @Field
    private String id;
    /**商品名*/
    @Field
    @ApiModelProperty("商品名")
    private String name;

    @Field
    private LocalDateTime created;
    @Field
    private LocalDateTime updated;

    @Field
    @ApiModelProperty("商品主图集合")
    private String picUrls;

    @Field(analyzer = FieldAnalyzer.IK_MAX_WORD, type = FieldType.Text)
    @ApiModelProperty("商品搜索关键字，最多20个汉字")
    private String keywords;


    @ApiModelProperty("商品的描述文")
    @Field(analyzer = FieldAnalyzer.IK_MAX_WORD, type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    @ApiModelProperty("是否上架，默认1位上架")
    private Integer listing;

    @Field(type = FieldType.Keyword)
    @ApiModelProperty("所属类别的id")
    private String productClassId;

    @ApiModelProperty("所属类别的名称")
    @Field(type = FieldType.Keyword)
    private String productClassName;

    @Field(type = FieldType.Double)
    @ApiModelProperty("展示在商品列表的商品价格，不是用户实际要支付的价格")
    private Double price;

}
