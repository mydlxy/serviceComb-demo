package com.ca.mfd.prc.pmc.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @author inkelink
 * @Description: 岗位操作指导书实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位操作指导书")
@TableName("PRC_PM_STATION_OPER_BOOK")
public class PmStationOperBookEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_STATION_OPER_BOOK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORK_SHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkShopId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORK_STATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkStationId = Constant.DEFAULT_ID;


    /**
     * 文件名称
     */
    @Schema(title = "文件名称")
    @TableField("FILE_NAME")
    private String fileName = StringUtils.EMPTY;


    /**
     * 文件路径
     */
    @Schema(title = "文件路径")
    @TableField("FILE_PATH")
    private String filePath = StringUtils.EMPTY;


    /**
     * 岗位名称
     */
    @Schema(title = "岗位名称")
    @TableField("STATION_NAME")
    private String stationName = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("SERIAL_NUMBER")
    private Integer serialNumber;


    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    @TableField("WO_TYPE")
    private String woType = StringUtils.EMPTY;


    /**
     * 机型
     */
    @Schema(title = "机型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


}