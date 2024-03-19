package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

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
 * @Description: 现场设备版本
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "现场设备版本")
@TableName("PRC_SYS_VERSION")
public class SysVersionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_VERSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间
     */
    @Schema(title = "车间")
    @TableField("WORK_SHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORK_SHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 线体
     */
    @Schema(title = "线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 类型
     */
    @Schema(title = "类型")
    @TableField("TYPE")
    private String type = StringUtils.EMPTY;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("NAME")
    private String name = StringUtils.EMPTY;

    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("CODE")
    private String code = StringUtils.EMPTY;

    /**
     * ip
     */
    @Schema(title = "ip")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;

    /**
     * 版本
     */
    @Schema(title = "版本")
    @TableField("VERSION")
    private String version = StringUtils.EMPTY;

    /**
     * 版本刷新时间
     */
    @Schema(title = "版本刷新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("REFRESH_DT")
    private Date refreshDt = new Date();

}
