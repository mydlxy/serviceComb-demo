package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author inkelink
 * @Description: 百格图实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "百格图")
@TableName("PRC_PQS_QUALITY_MATRIK")
public class PqsQualityMatrikEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_QUALITY_MATRIK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("ITEM_CODE")
    private String itemCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ITEM_NAME")
    private String itemName = StringUtils.EMPTY;


    /**
     * 图片
     */
    @Schema(title = "图片")
    @TableField("IMAGE")
    private String image = StringUtils.EMPTY;


    /**
     * 行
     */
    @Schema(title = "行")
    @TableField("ROW_COUNT")
    private Integer rowCount = 0;


    /**
     * 列
     */
    @Schema(title = "列")
    @TableField("COLUMN_COUNT")
    private Integer columnCount = 0;


    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODELS")
    private String models = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAMES")
    private String workstationNames = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;

    /**
     * 百格图-车型
     */
    @TableField(exist = false)
    private List<PqsQualityMatrikTcEntity> vehicles;

    /**
     * 百格图-缺陷
     */
    @TableField(exist = false)
    private List<PqsQualityMatrikAnomalyEntity> defects;
}