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
 * @Description: 关键点行为配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "关键点行为配置")
@TableName("PRC_AVI_OPER")
public class AviOperEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_OPER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 行为
     */
    @Schema(title = "行为")
    @TableField("ACTION")
    private String action = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPALY_NO")
    private Integer dispalyNo = 0;

    /**
     * 组名
     */
    @Schema(title = "组名")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 类型(0,异步;1.同步)
     */
    @Schema(title = "类型(0,异步 1.同步)")
    @TableField("AVI_OPER_TYPE")
    private Integer aviOperType = 0;

    /**
     * AVI类型
     */
    @Schema(title = "AVI类型")
    @TableField("AVI_TYPE")
    private Integer aviType = 0;

    /**
     * 是否重复过点
     */
    @Schema(title = "是否重复过点")
    @TableField("IS_REPEAT")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isRepeat = false;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isEnabled = false;

}
