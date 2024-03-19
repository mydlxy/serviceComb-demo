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
 * @Description: 追溯设备工艺参数
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯设备工艺参数")
@TableName("PRC_EPS_VEHICLE_EQUMENT_PAR_CONFIG")
public class EpsVehicleEqumentParConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_EQUMENT_PAR_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 配置主键编号
     */
    @Schema(title = "配置主键编号")
    @TableField("PRC_EPS_VEHICLE_EQUMENT_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleEqumentConfigId = Constant.DEFAULT_ID;

    /**
     * 参数名称
     */
    @Schema(title = "参数名称")
    @TableField("EQUMENT_PAR_NAME")
    private String equmentParName = StringUtils.EMPTY;

    /**
     * 参数代码
     */
    @Schema(title = "参数代码")
    @TableField("EQUMENT_PAR_CODE")
    private String equmentParCode = StringUtils.EMPTY;


    /**
     * 工艺排序
     */
    @Schema(title = "工艺排序")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 默认上限值
     */
    @Schema(title = "默认上限值")
    @TableField("WO_UPLIMIT_DEFAULT")
    private String woUplimitDefault = StringUtils.EMPTY;
    /**
     * 默认下限值
     */
    @Schema(title = "默认下限值")
    @TableField("WO_DOWNLIMIT_DEFAULT")
    private String woDownlimitDefault = StringUtils.EMPTY;
    /**
     * 默认标准值
     */
    @Schema(title = "默认标准值")
    @TableField("WO_STANDARD_DEFAULT")
    private String woStandardDefault = StringUtils.EMPTY;
    /**
     * 默认参数结果
     */
    @Schema(title = "默认参数结果")
    @TableField("WO_RESULT_DEFAULT")
    private String woResultDefault = StringUtils.EMPTY;
    /**
     * 默认参数单位
     */
    @Schema(title = "默认参数单位")
    @TableField("WO_UNIT_DEFAULT")
    private String woUnitDefault = StringUtils.EMPTY;

    /**
     * 获取结果方式（0 采集得结果 1 对比得结果）
     */
    @Schema(title = "获取结果方式（0 采集得结果 1 对比得结果）")
    @TableField("GET_RESULT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer getResultType = 0;

    /**
     * 激活缺陷的结果值
     */
    @Schema(title = "激活缺陷的结果值")
    @TableField("ACTIVATE_DEFECT_RESULT")
    private String activateDefectResult = StringUtils.EMPTY;

    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    @TableField("DEFECT_CODE")
    private String defectCode = StringUtils.EMPTY;

    /**
     * 设置涉及到消耗
     */
    @Schema(title = "设置涉及到消耗")
    @TableField("IS_USER_OF_ENABLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isUserOfEnable = false;


    /**
     * 默认上限值绑定属性
     */
    @Schema(title = "默认上限值绑定属性")
    @TableField("WO_UPLIMIT_DEFAULT_PROPERTY")
    private String woUplimitDefaultProperty = StringUtils.EMPTY;

    /**
     * 默认下限值绑定属性
     */
    @Schema(title = "默认下限值绑定属性")
    @TableField("WO_DOWNLIMIT_DEFAULT_PROPERTY")
    private String woDownlimitDefaultProperty = StringUtils.EMPTY;

    /**
     * 默认标准值绑定属性
     */
    @Schema(title = "默认标准值绑定属性")
    @TableField("WO_STANDARD_DEFAULT_PROPERTY")
    private String woStandardDefaultProperty = StringUtils.EMPTY;

    /**
     * 默认参数结果绑定属性
     */
    @Schema(title = "默认参数结果绑定属性")
    @TableField("WO_RESULT_DEFAULT_PROPERTY")
    private String woResultDefaultProperty = StringUtils.EMPTY;

    /**
     * 默认参数单位绑定属性
     */
    @Schema(title = "默认参数单位绑定属性")
    @TableField("WO_UNIT_DEFAULT_PROPERTY")
    private String woUnitDefaultProperty = StringUtils.EMPTY;

}
