package com.ca.mfd.prc.audit.entity;

import com.ca.mfd.prc.audit.dto.DefectAnomalyDto;
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
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: AUDIT QG检验项-色块实体
 * @author inkelink
 * @date 2023年12月04日
 * @变更说明 BY inkelink At 2023年12月04日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "AUDIT QG检验项-色块")
@TableName("PRC_AUDIT_QUALITY_GATE_BLANK")
public class AuditQualityGateBlankEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AUDIT_QUALITY_GATE_BLANK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * QG检查项ID
     */
    @Schema(title = "QG检查项ID")
    @TableField("PRC_AUDIT_QUALITY_GATE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcAuditQualityGateId = Constant.DEFAULT_ID;


    /**
     * 色块类型
     */
    @Schema(title = "色块类型")
    @TableField("BLOCK_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer blockType = 0;


    /**
     * 色块高度
     */
    @Schema(title = "色块高度")
    @TableField("BLOCK_HEIGHT")
    private BigDecimal blockHeight = BigDecimal.valueOf(0);


    /**
     * 色块宽度
     */
    @Schema(title = "色块宽度")
    @TableField("BLOCK_WIDTH")
    private BigDecimal blockWidth = BigDecimal.valueOf(0);


    /**
     * 色块TOP
     */
    @Schema(title = "色块TOP")
    @TableField("BLOCK_TOP")
    private BigDecimal blockTop = BigDecimal.valueOf(0);


    /**
     * 色块LEFT
     */
    @Schema(title = "色块LEFT")
    @TableField("BLOCK_LEFT")
    private BigDecimal blockLeft = BigDecimal.valueOf(0);


    /**
     * 色块JSON
     */
    @Schema(title = "色块JSON")
    @TableField("BLOCK_JSON")
    private String blockJson = StringUtils.EMPTY;


    /**
     * 缺陷编号列表 --初始化接口不填写。编辑时填写
     */
    @TableField(exist = false)
    private List<DefectAnomalyDto> anomalyCodes;
}