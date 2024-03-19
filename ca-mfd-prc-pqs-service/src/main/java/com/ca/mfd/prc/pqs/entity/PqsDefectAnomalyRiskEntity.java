package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 质量围堵实体
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质量围堵")
@TableName("PRC_PQS_DEFECT_ANOMALY_RISK")
public class PqsDefectAnomalyRiskEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_DEFECT_ANOMALY_RISK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 问题编号
     */
    @Schema(title = "问题编号")
    @TableField("RISK_NO")
    private String riskNo = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 类别;1 整车 2压铸 3机加 4冲压
     */
    @Schema(title = "类别;1 整车 2压铸 3机加 4冲压")
    @TableField("CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 0;


    /**
     * 组合代码
     */
    @Schema(title = "组合代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 风险问题补充说明
     */
    @Schema(title = "风险问题补充说明")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 发起人
     */
    @Schema(title = "发起人")
    @TableField("START_BY")
    private String startBy = StringUtils.EMPTY;


    /**
     * 发起时间
     */
    @Schema(title = "发起时间")
    @TableField("START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDt = new Date();


    /**
     * 关闭人
     */
    @Schema(title = "关闭人")
    @TableField("CLOSE_BY")
    private String closeBy = StringUtils.EMPTY;


    /**
     * 关闭时间
     */
    @Schema(title = "关闭时间")
    @TableField("CLOSE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date closeDt;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;
}