package com.ca.mfd.prc.rc.rcavi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 滑橇使用日志实体
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "滑橇使用日志")
@TableName("PRC_RC_AVI_SKID_LOG")
public class RcAviSkidLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_SKID_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 滑橇外键
     */
    @Schema(title = "滑橇外键")
    @TableField("PRC_RC_AVI_SKID_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcAviSkidId = Constant.DEFAULT_ID;


    /**
     * 操作内容
     */
    @Schema(title = "操作内容")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;


    /**
     * 操作人
     */
    @Schema(title = "操作人")
    @TableField("OPERATER")
    private String operater = StringUtils.EMPTY;


    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @TableField("OPERATE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date operateDt;


    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    @TableField("OPERATE_TYPE")
    private Integer operateType = 0;
}