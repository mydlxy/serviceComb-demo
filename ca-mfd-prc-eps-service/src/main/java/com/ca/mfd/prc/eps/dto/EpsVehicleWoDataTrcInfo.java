package com.ca.mfd.prc.eps.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 工艺数据(追溯)
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@Schema(description = "工艺数据(追溯)")
public class EpsVehicleWoDataTrcInfo implements Serializable {


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间编号
     */
    @Schema(title = "车间编号")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    private String lineName = StringUtils.EMPTY;

    /**
     * 线体编号
     */
    @Schema(title = "线体编号")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 车系
     */
    @Schema(title = "车系")
    private String model = StringUtils.EMPTY;

    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    private String sn = StringUtils.EMPTY;

    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    private String woCode = StringUtils.EMPTY;

    /**
     * 工艺描述
     */
    @Schema(title = "工艺描述")
    private String woDescription = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    private String barcode = StringUtils.EMPTY;

    /**
     * 0 未知 1 OK 2 NG 3 Bypass
     */
    @Schema(title = "0未知1OK2NG3Bypass")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date creationDate = new Date();

    /**
     * 创建人名称
     */
    @Schema(title = "创建人名称")
    private String createdUser = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

}
