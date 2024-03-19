package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
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
 * @Description: 整车AVI锁定
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "整车AVI锁定")
//@TableName("AVI_BLOCK")
@TableName("PRC_AVI_BLOCK")
public class AviBlockEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_BLOCK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 对象编号
     */
    //    @Schema(title = "对象编号")
    //    @TableField("TPS")
    //    private String tps = StringUtils.EMPTY;
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 锁定站点
     */
    //    @Schema(title = "锁定站点")
    //    @TableField("BLOCK_AVI_ID")
    //    private String blockAviId = StringUtils.EMPTY;
    //    @Schema(title = "锁定站点")
    //    @TableField("PRC_PM_AVI_ID")
    //    @JsonSerialize(using = ToStringSerializer.class)
    //    private Long prcPmAviId = Constant.DEFAULT_ID;

    /**
     * 是否处理
     */
    @Schema(title = "是否处理")
    @TableField("IS_PROCESS")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isProcess = false;

    /**
     * 车间代码
     */
    //    @Schema(title = "车间代码")
    //    @TableField("PM_AREA_CODE")
    //    private String pmAreaCode = StringUtils.EMPTY;
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体代码
     */
    //    @Schema(title = "线体代码")
    //    @TableField("PM_WORK_CENTER_CODE")
    //    private String pmWorkCenterCode = StringUtils.EMPTY;
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    //    @Schema(title = "AVI代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

}
