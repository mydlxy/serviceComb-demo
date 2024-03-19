package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 岗位时间日志
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位时间日志")
@TableName("PRC_EPS_WORKPLACE_TIME_LOG")
public class EpsWorkplaceTimeLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_WORKPLACE_TIME_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 车身号
     */
    @Schema(title = "车身号")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 进岗时间(可空)
     */
    @Schema(title = "进岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("INSERT_WORKPLACE_DT")
    private Date insertWorkplaceDt;

    /**
     * 工艺完成时间(可空)
     */
    @Schema(title = "工艺完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("LAST_OPER_DT")
    private Date lastOperDt;

    /**
     * 离岗时间(可空)
     */
    @Schema(title = "离岗时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("LEAVE_WORKPLACE_DT")
    private Date leaveWorkplaceDt;

    /**
     * 进岗方式(1 自动 2 手动)
     */
    @Schema(title = "进岗方式(1自动2手动)")
    @TableField("MODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer mode = 0;

}
