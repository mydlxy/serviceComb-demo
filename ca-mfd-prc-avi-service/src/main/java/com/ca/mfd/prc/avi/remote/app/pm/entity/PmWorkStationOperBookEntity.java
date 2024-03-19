package com.ca.mfd.prc.avi.remote.app.pm.entity;

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
 * @Description: 岗位操作指导书
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位操作指导书")
@TableName("PRC_PM_STATION_OPER_BOOK")
public class PmWorkStationOperBookEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "RPC_PM_STATION_OPER_BOOK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id;

    /**
     * 车间外键
     */
    @Schema(title = "车间外键")
    @TableField("RPC_PM_WORK_SHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkShopId;

    /**
     * 生产线
     */
    @Schema(title = "生产线")
    @TableField("PRC_PM_WORK_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkLineId;

    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("PRC_PM_WORK_STATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcWorkStationId;

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
    @Schema(title = "工位名称")
    @TableField("STATION_NAME")
    private String workplaceName = StringUtils.EMPTY;

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
