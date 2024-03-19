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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink
 * @Description: 物料质量冻结记录实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "物料质量冻结记录")
@TableName("PRC_PQS_MM_FREEZE_RECORD")
public class PqsMmFreezeRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_MM_FREEZE_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 冻结记录编号
     */
    @Schema(title = "冻结记录编号")
    @TableField("NO")
    private String no = StringUtils.EMPTY;


    /**
     * 冻结数量
     */
    @Schema(title = "冻结数量")
    @TableField("QTY")
    private BigDecimal qty = BigDecimal.valueOf(0);


    /**
     * 冻结原因说明
     */
    @Schema(title = "冻结原因说明")
    @TableField("FREEZE_REASON")
    private String freezeReason = StringUtils.EMPTY;


    /**
     * 冻结时间
     */
    @Schema(title = "冻结时间")
    @TableField("FREEZE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date freezeDt;


    /**
     * 冻结人
     */
    @Schema(title = "冻结人")
    @TableField("FREEZE_USER")
    private String freezeUser = StringUtils.EMPTY;


    /**
     * 冻结备注
     */
    @Schema(title = "冻结备注")
    @TableField("FREEZE_REMARK")
    private String freezeRemark = StringUtils.EMPTY;


    /**
     * 解冻人
     */
    @Schema(title = "解冻人")
    @TableField("UNFREEZE_USER")
    private String unfreezeUser = StringUtils.EMPTY;


    /**
     * 解冻时间
     */
    @Schema(title = "解冻时间")
    @TableField("UNFREEZE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date unfreezeDt;


    /**
     * 解冻原因说明
     */
    @Schema(title = "解冻原因说明")
    @TableField("UNFREEZE_COMMENT")
    private String unfreezeComment = StringUtils.EMPTY;


    /**
     * 状态 1待处理 2处理中 97已完成
     */
    @Schema(title = "状态 1待处理 2处理中 97已完成")
    @TableField("STATUS")
    private Integer status = 0;


    /**
     * 仓库代码
     */
    @Schema(title = "仓库代码")
    @TableField("WAREHOUSE_CODE")
    private String warehouseCode = StringUtils.EMPTY;


    /**
     * 质量冻结单类型 1产线 2仓库
     */
    @Schema(title = "质量冻结单类型 1产线 2仓库")
    @TableField("FREEZE_RECORD_TYPE")
    private Integer freezeRecordType = 0;


}