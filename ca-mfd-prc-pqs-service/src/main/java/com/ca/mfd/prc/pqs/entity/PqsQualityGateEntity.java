package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: QG检查项实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "QG检查项")
@TableName("PRC_PQS_QUALITY_GATE")
public class PqsQualityGateEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_QUALITY_GATE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 检测项代码
     */
    @Schema(title = "检测项代码")
    @TableField("ITEM_CODE")
    private String itemCode = StringUtils.EMPTY;


    /**
     * 检测项名称
     */
    @Schema(title = "检测项名称")
    @TableField("ITEM_NAME")
    private String itemName = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAMES")
    private String workstationNames = StringUtils.EMPTY;
    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODELS")
    private String models = StringUtils.EMPTY;


    /**
     * 图片
     */
    @Schema(title = "图片")
    @TableField("IMAGE")
    private String image = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}