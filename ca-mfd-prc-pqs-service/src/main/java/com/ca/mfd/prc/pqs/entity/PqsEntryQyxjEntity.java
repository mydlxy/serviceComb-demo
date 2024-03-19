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
 * @Description: 区域巡检实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "区域巡检")
@TableName("PRC_PQS_ENTRY_QYXJ")
public class PqsEntryQyxjEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_QYXJ_ID", type = IdType.INPUT)
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
     * 检验类型编码
     */
    @Schema(title = "检验类型编码")
    @TableField("ENTRY_TYPE")
    private String entryType = StringUtils.EMPTY;


    /**
     * 检验类型名称
     */
    @Schema(title = "检验类型名称")
    @TableField("ENTRY_TYPE_DESC")
    private String entryTypeDesc = StringUtils.EMPTY;


    /**
     * 质检单状态;1:创建 2进行中 90已完成
     */
    @Schema(title = "质检单状态;1:创建 2进行中 90已完成")
    @TableField("STATUS")
    private Integer status = 0;


    /**
     * 质检单结论;1 不适用 0待判定 1通过 2不通过
     */
    @Schema(title = "质检单结论;1 不适用 0待判定 1通过 2不通过")
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
     * 处理人
     */
    @Schema(title = "处理人")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;


    /**
     * 纠正措施
     */
    @Schema(title = "纠正措施")
    @TableField("CORRECTIVE_ACTION")
    private String correctiveAction = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}