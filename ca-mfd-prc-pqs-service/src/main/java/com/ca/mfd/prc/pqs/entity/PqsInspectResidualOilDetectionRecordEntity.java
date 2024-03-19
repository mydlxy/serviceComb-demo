package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 残油量检测记录表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "残油量检测记录表")
@TableName("PRC_PQS_INSPECT_RESIDUAL_OIL_DETECTION_RECORD")
public class PqsInspectResidualOilDetectionRecordEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_RESIDUAL_OIL_DETECTION_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检测时间
     */
    @Schema(title = "检测时间")
    @TableField("SAMPLING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date samplingDate;


    /**
     * 冲压A线-零件（1次/月） 标准＜2.5g/㎡
     */
    @Schema(title = "冲压A线-零件（1次/月） 标准＜2.5g/㎡")
    @TableField("A_LINE_RESIDUAL_OIL_AMOUNT")
    private BigDecimal aLineResidualOilAmount = BigDecimal.valueOf(0);


    /**
     * 冲压B线-零件（1次/月） 标准＜2.5g/㎡
     */
    @Schema(title = "冲压B线-零件（1次/月） 标准＜2.5g/㎡")
    @TableField("B_LINE_RESIDUAL_OIL_AMOUNT")
    private BigDecimal bLineResidualOilAmount = BigDecimal.valueOf(0);


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}