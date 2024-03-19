package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 追溯设备采集记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯设备采集记录")
@TableName("PRC_EPS_VEHICLE_EQUMENT_DATA")
public class EpsVehicleEqumentDataEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_EQUMENT_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 数据编号
     */
    @Schema(title = "数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_DATA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoDataId = Constant.DEFAULT_ID;

    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 工艺名称
     */
    @Schema(title = "工艺名称")
    @TableField("WO_NAME")
    private String woName = StringUtils.EMPTY;

    /**
     * 参数编码
     */
    @Schema(title = "参数编码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 参数单位
     */
    @Schema(title = "参数单位")
    @TableField("WO_UNIT")
    private String woUnit = StringUtils.EMPTY;

    /**
     * 标准值
     */
    @Schema(title = "标准值")
    @TableField("WO_STANDARD")
    private String woStandard = StringUtils.EMPTY;

    /**
     * 上限值
     */
    @Schema(title = "上限值")
    @TableField("WO_UPLIMIT")
    private String woUplimit = StringUtils.EMPTY;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    @TableField("WO_DOWNLIMIT")
    private String woDownlimit = StringUtils.EMPTY;

    /**
     * 参数值
     */
    @Schema(title = "参数值")
    @TableField("WO_VALUE")
    private String woValue = StringUtils.EMPTY;

    /**
     * 参数结果
     */
    @Schema(title = "参数结果")
    @TableField("WO_RESULT")
    private String woResult = StringUtils.EMPTY;


}
