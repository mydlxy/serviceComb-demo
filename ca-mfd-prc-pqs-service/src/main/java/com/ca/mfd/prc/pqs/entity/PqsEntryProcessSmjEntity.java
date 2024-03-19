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
 * @Description: 质检工单-过程检验_首末检验实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质检工单-过程检验_首末检验")
@TableName("PRC_PQS_ENTRY_PROCESS_SMJ")
public class PqsEntryProcessSmjEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_PROCESS_SMJ_ID", type = IdType.INPUT)
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
     * 初检结论;（0未判定 1通过 2不通过）
     */
    @Schema(title = "初检结论;（0未判定 1通过 2不通过）")
    @TableField("FIRST_RESULT")
    private Integer firstResult = 0;


    /**
     * 初检备注
     */
    @Schema(title = "初检备注")
    @TableField("FIRST_REMARK")
    private String firstRemark = StringUtils.EMPTY;


    /**
     * 初检时间
     */
    @Schema(title = "初检时间")
    @TableField("FIRST_QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date firstQcDt;


    /**
     * 质检人员
     */
    @Schema(title = "质检人员")
    @TableField("FIRST_QC_USER")
    private String firstQcUser = StringUtils.EMPTY;


    /**
     * 终检结论;（0未判定 1通过 2通过）
     */
    @Schema(title = "终检结论;（0未判定 1通过 2通过）")
    @TableField("LAST_RESULT")
    private Integer lastResult = 0;


    /**
     * 终检质检时间
     */
    @Schema(title = "终检质检时间")
    @TableField("LAST_QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date lastQcDt;


    /**
     * 终检质检人员
     */
    @Schema(title = "终检质检人员")
    @TableField("LAST_QC_USER")
    private String lastQcUser = StringUtils.EMPTY;


    /**
     * 质检备注
     */
    @Schema(title = "质检备注")
    @TableField("LAST_REMARK")
    private String lastRemark = StringUtils.EMPTY;


}