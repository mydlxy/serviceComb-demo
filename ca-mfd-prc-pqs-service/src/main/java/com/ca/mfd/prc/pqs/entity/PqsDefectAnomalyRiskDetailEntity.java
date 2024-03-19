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
 * @Description: 质量围堵-清单实体
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质量围堵-清单")
@TableName("PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL")
public class PqsDefectAnomalyRiskDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_DEFECT_ANOMALY_RISK_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 风险问题编号
     */
    @Schema(title = "风险问题编号")
    @TableField("RISK_NO")
    private String riskNo;


    /**
     * 产品唯一码
     */
    @Schema(title = "产品唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


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
    @TableField("RISK_REMARK")
    private String riskRemark = StringUtils.EMPTY;


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
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date startDt;


    /**
     * 关闭备注
     */
    @Schema(title = "关闭备注")
    @TableField("CLOSE_REMARK")
    private String closeRemark = StringUtils.EMPTY;


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
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date closeDt;


    /**
     * 是否激活;0 未激活 1激活
     */
    @Schema(title = "是否激活;0 未激活 1激活")
    @TableField("IS_ACTIVED")
    private Boolean isActived = false;


    /**
     * 激活人
     */
    @Schema(title = "激活人")
    @TableField("ACTIVE_BY")
    private String activeBy = StringUtils.EMPTY;


    /**
     * 激活时间
     */
    @Schema(title = "激活时间")
    @TableField("ACTIVE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date activeDt;


    /**
     * 需要DMS锁定
     */
    @Schema(title = "需要DMS锁定")
    @TableField("DMS_CHECK")
    private Boolean dmsCheck = false;


    /**
     * DMS发送状态
     */
    @Schema(title = "DMS发送状态")
    @TableField("DMS_SEND_STATUS")
    private Integer dmsSendStatus = 0;


    /**
     * 需要库区锁定
     */
    @Schema(title = "需要库区锁定")
    @TableField("INVENTORY_CHECK")
    private Boolean inventoryCheck = false;


    /**
     * 库区发送状态
     */
    @Schema(title = "库区发送状态")
    @TableField("INVENTORY_SEND_STATUS")
    private Integer inventorySendStatus = 0;


    /**
     * 状态 1 待处理 20 激活  90 释放
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 1;


    /**
     * 类别;1、整车 2、零部件
     */
    @Schema(title = "类别;1、整车 2、零部件")
    @TableField("CATEGORY")
    private Integer category = 1;

}