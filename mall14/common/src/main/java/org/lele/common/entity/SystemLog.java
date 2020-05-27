package org.lele.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lele.common.constant.LogConstant;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


/**
 * org.lele.common.entity
 *
 * @author: lele
 * @date: 2020-05-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("系统访问日志")
@Document(indexName="logs",type="systemLog")
public class SystemLog {
    @Id
    @ApiModelProperty("日志id，uuid生成")
    private String id;

    @Field
    @ApiModelProperty("日志类型")
    private LogConstant.LogType type;

    @Field
    @ApiModelProperty("常规日志信息")
    private String messgae;

    @Field
    @ApiModelProperty("日志记录时间")
    private Long logTime;

    @Field
    @ApiModelProperty("来源地址")
    private String sourceUrl;

    @Field
    @ApiModelProperty("访问者信息")
    private String userDetails;

    @Field
    @ApiModelProperty("访问节点")
    private String requestUrl;

    @Field
    @ApiModelProperty("访问方法")
    private String requestMethod;

    @Field
    @ApiModelProperty("请求参数")
    private String requestParam;

    @Field()
    @ApiModelProperty("处理成功返回结果:与errMessage二者有一")
    private String result;

    @Field
    @ApiModelProperty("处理失败返回结果:与result二者有一")
    private String errMessage;


}


