package com.ca.mfd.prc.eps.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 岗位工艺关联人员
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工位工艺关联人员")
@TableName("PRC_PM_STATION_USER")
public class PmWorkStationUserEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PM_STATION_USER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id;

    /**
     * 岗位名称
     */
    @Schema(title = "工位名称")
    @TableField("STATION_NAME")
    private String stationName = StringUtils.EMPTY;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @TableField("STATION_USER_NAME")
    private String stationUserName = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
