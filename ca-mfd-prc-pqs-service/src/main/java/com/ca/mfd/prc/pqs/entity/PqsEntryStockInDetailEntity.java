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
 * @Description: 入库质检工单实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "入库质检工单")
@TableName("PRC_PQS_ENTRY_STOCK_IN_DETAIL")
public class PqsEntryStockInDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_STOCK_IN_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 结论（0未判定 1 通过 2不通过）
     */
    @Schema(title = "结论（0未判定 1 通过 2不通过）")
    @TableField("RESULT")
    private Integer result = 0;


    /**
     * 质检时间
     */
    @Schema(title = "质检时间")
    @TableField("QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date qcDt;


    /**
     * 质检人员
     */
    @Schema(title = "质检人员")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 特判时间
     */
    @Schema(title = "特判时间")
    @TableField("SPECIAL_CHECK_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date specialCheckDt;


    /**
     * 特判状结论（0未判定 1合格 2不合格）
     */
    @Schema(title = "特判状结论（0未判定 1合格 2不合格）")
    @TableField("SPECIAL_CHECK_RESULT")
    private Integer specialCheckResult = 0;


    /**
     * 特判人员
     */
    @Schema(title = "特判人员")
    @TableField("SPECIAL_CHECK_USER")
    private String specialCheckUser = StringUtils.EMPTY;


    /**
     * 特判备注
     */
    @Schema(title = "特判备注")
    @TableField("SPECIAL_CHECK_REMARK")
    private String specialCheckRemark = StringUtils.EMPTY;


}