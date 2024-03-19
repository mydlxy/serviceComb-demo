package com.ca.mfd.prc.eps.entity;

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
 * @author inkelink ${email}
 * @Description: 装配单明细
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "装配单明细")
@TableName("PRC_EPS_ASSEMBLE_DETAIL")
public class EpsAssembleDetailEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_ASSEMBLE_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 装配单设置外键
     */
    @Schema(title = "装配单设置外键")
    @TableField("PRC_EPS_ASSEMBLE_SET_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcReportAssembleSetId = Constant.DEFAULT_ID;

    /**
     * 标题
     */
    @Schema(title = "标题")
    @TableField("TITLE")
    private String title = StringUtils.EMPTY;

    /**
     * 关键字
     */
    @Schema(title = "关键字")
    @TableField("KEY_DATA")
    private String keyData = StringUtils.EMPTY;

    /**
     * 关键字值
     */
    @Schema(title = "关键字值")
    @TableField("KEY_CONTENT")
    private String keyContent = StringUtils.EMPTY;

    /**
     * 排序
     */
    @Schema(title = "排序")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

}
