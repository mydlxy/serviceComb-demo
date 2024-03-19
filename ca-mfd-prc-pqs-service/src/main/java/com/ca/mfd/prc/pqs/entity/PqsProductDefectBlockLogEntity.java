package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 缺陷堵塞记录实体
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "缺陷堵塞记录")
@TableName("PRC_PQS_PRODUCT_DEFECT_BLOCK_LOG")
public class PqsProductDefectBlockLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PRODUCT_DEFECT_BLOCK_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 类别;1、整车/电池  2、零部件
     */
    @Schema(title = "类别;1、整车/电池  2、零部件")
    @TableField("CATEGORY")
    private Integer category = 0;


    /**
     * 站点代码
     */
    @Schema(title = "站点代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 站点名称
     */
    @Schema(title = "站点名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 阻塞说明
     */
    @Schema(title = "阻塞说明")
    @TableField("BLOCK_NOTE")
    private String blockNote = StringUtils.EMPTY;


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
     * 阻塞时间
     */
    @Schema(title = "阻塞时间")
    @TableField("BLOCK_DT")
    private String blockDt = StringUtils.EMPTY;


}