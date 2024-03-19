package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 质量围堵-操作日志实体
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质量围堵-操作日志")
@TableName("PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL_LOG")
public class PqsDefectAnomalyRiskDetailLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 风险明细ID
     */
    @Schema(title = "风险明细ID")
    @TableField("PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsDefectAnomalyRiskDetailId = Constant.DEFAULT_ID;


    /**
     * 风险问题编号
     */
    @Schema(title = "风险问题编号")
    @TableField("RISK_NO")
    private String riskNo = StringUtils.EMPTY;


    /**
     * 产品唯一码
     */
    @Schema(title = "产品唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 风险问题补充说明
     */
    @Schema(title = "风险问题补充说明")
    @TableField("OPERATE_REMARK")
    private String operateRemark = StringUtils.EMPTY;


    /**
     * 发起人
     */
    @Schema(title = "发起人")
    @TableField("OPERATE_BY")
    private String operateBy = StringUtils.EMPTY;


    /**
     * 发起时间
     */
    @Schema(title = "发起时间")
    @TableField("OPERATE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date operateDt;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;


}