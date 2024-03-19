package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 零部件缺陷活动日志实体
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "零部件缺陷活动日志")
@TableName("PRC_PQS_MM_DEFECT_ANOMALY_LOG")
public class PqsMmDefectAnomalyLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_MM_DEFECT_ANOMALY_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 产品缺陷ID
     */
    @Schema(title = "产品缺陷ID")
    @TableField("PRC_PQS_MM_DEFECT_ANOMALY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcPqsMmDefectAnomalyId = Constant.DEFAULT_ID;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 状态;1.未修复、2.已修复、3未发现 4合格 、5不合格
     */
    @Schema(title = "状态;1.未修复、2.已修复、3未发现 4合格 、5不合格")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


}