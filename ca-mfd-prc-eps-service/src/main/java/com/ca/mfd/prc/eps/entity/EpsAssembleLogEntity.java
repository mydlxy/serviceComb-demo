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
 * @Description: 装配单日志
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "装配单日志")
@TableName("PRC_EPS_ASSEMBLE_LOG")
public class EpsAssembleLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_ASSEMBLE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 整车编码
     */
    @Schema(title = "整车编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 装配指示代码
     */
    @Schema(title = "装配指示代码")
    @TableField("TPL_CODE")
    private String tplCode = StringUtils.EMPTY;

    /**
     * 装配标题
     */
    @Schema(title = "装配标题")
    @TableField("KEY_TITLE")
    private String keyTitle = StringUtils.EMPTY;

    /**
     * 装配内容
     */
    @Schema(title = "装配内容")
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
     * 装配岗位
     */
    @Schema(title = "装配岗位")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

}
