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
 * @Description: 关键过点配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "关键过点配置")
//@TableName("AVI_REPEAT_TRACKING_RECORD")
@TableName("PRC_AVI_REPEAT_TRACKING_RECORD")
public class AviRepeatTrackingRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_REPEAT_TRACKING_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 关键点
     */
    //    @Schema(title = "关键点")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;


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

    /**
     * 是否启用
     */
    //    @Schema(title = "是否启用")
    //    @TableField("IS_ENABLE")
    //    private Boolean isEnable = false;
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    //@JsonSerialize(using= JsonSerializeBooleanInt.class )
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isEnable = false;

}
