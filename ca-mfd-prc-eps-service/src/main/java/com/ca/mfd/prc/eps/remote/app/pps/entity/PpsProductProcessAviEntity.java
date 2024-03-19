package com.ca.mfd.prc.eps.remote.app.pps.entity;

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
 * @Description: 工艺路径详细
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工艺路径详细")
@TableName("PRC_PPS_PRODUCT_PROCESS_AVI")
public class PpsProductProcessAviEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PRODUCT_PROCESS_AVI_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * AVI编码
     */
    @Schema(title = "AVI编码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 查看系统配置ProcessAviType
     */
    @Schema(title = "查看系统配置ProcessAviType")
    @TableField("AVI_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer aviType = 0;

    /**
     * 验证顺序
     */
    @Schema(title = "验证顺序")
    @TableField("IS_SEQUENCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSequence = false;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 生产路线ID
     */
    @Schema(title = "生产路线ID")
    @TableField("PRC_PPS_PRODUCT_PROCESS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcPpsProductProcessId = Constant.DEFAULT_ID;

}
