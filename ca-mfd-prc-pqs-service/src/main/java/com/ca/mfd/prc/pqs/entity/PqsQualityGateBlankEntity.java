package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author inkelink
 * @Description: QG检验项-色块实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "QG检验项-色块")
@TableName("PRC_PQS_QUALITY_GATE_BLANK")
public class PqsQualityGateBlankEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_QUALITY_GATE_BLANK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * QG检查项ID
     */
    @Schema(title = "QG检查项ID")
    @TableField("PRC_PQS_QUALITY_GATE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsQualityGateId = Constant.DEFAULT_ID;


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