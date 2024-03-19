package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: AVI缺陷阻塞配置[作废]实体
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AVI缺陷阻塞配置")
@TableName("PRC_AVI_DEFECT_ANOMALY_AVI")
public class AviDefectAnomalyAviEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_DEFECT_ANOMALY_AVI_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 缺陷编号
     */
    @Schema(title = "缺陷编号")
    @TableField("PQS_DEFECT_ANOMALY_CODE")
    private String pqsDefectAnomalyCode = StringUtils.EMPTY;


    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    @TableField("PQS_DEFECT_ANOMALY_DESCRIPTION")
    private String pqsDefectAnomalyDescription = StringUtils.EMPTY;


    /**
     * AVI代码
     */
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;


    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    @Schema(title = "是否风控")
    @TableField(exist = false)
    private Boolean isRisk = false;
}