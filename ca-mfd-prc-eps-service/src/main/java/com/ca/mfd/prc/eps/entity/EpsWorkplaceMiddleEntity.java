package com.ca.mfd.prc.eps.entity;

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
 * @author inkelink ${email}
 * @Description: 开工检查记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "开工检查记录")
@TableName("PRC_EPS_WORKPLACE_MIDDLE")
public class EpsWorkplaceMiddleEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_WORKPLACE_MIDDLE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 操作id(可空)
     */
    @Schema(title = "操作id")
    @TableField("OPERA_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long operaUserId;

    /**
     * 操作用户名称
     */
    @Schema(title = "操作用户名称")
    @TableField("OPERA_USER_NAME")
    private String operaUserName = StringUtils.EMPTY;

    /**
     * 操作时间(可空)
     */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("OPERA_DATETIME")
    private Date operaDatetime;

    /**
     * 工位id
     */
    @Schema(title = "工位编码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 检测项id
     */
    @Schema(title = "检测项id")
    @TableField("PRC_EPS_WORKPLACE_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsWorkplaceConfigId = Constant.DEFAULT_ID;

}
