package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 百格图-车型实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "百格图-车型")
@TableName("PRC_PQS_QUALITY_MATRIK_TC")
public class PqsQualityMatrikTcEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId("PRC_PQS_QUALITY_MATRIK_TC_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 百格图ID
     */
    @Schema(title = "百格图ID")
    @TableField("PRC_PQS_QUALITY_MATRIK_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsQualityMatrikId = Constant.DEFAULT_ID;


    /**
     * 车型代码
     */
    @Schema(title = "车型代码")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 车型名称
     */
    @Schema(title = "车型名称")
    @TableField("MODEL_NAME")
    private String modelName = StringUtils.EMPTY;


}