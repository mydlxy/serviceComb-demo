package com.ca.mfd.prc.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 设备实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "设备")
@TableName("PRC_PM_EQUIPMENT")
public class PmEquipmentEntity extends PmBaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_EQUIPMENT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间外键
     */
    @Schema(title = "车间外键")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 生产线ID
     */
    @Schema(title = "生产线ID")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 工位ID
     */
    @Schema(title = "工位ID")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位号
     */
    @Schema(title = "工位号")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("EQUIPMENT_CODE")
    private String equipmentCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("EQUIPMENT_NAME")
    private String equipmentName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 供应商名称
     */
    @Schema(title = "供应商名称")
    @TableField("SUPPLIER_NAME")
    private String supplierName = StringUtils.EMPTY;


    /**
     * 供应商代码
     */
    @Schema(title = "供应商代码")
    @TableField("SUPPLIER_CODE")
    private String supplierCode = StringUtils.EMPTY;


    /**
     * 报废日期
     */
    @Schema(title = "报废日期")
    @TableField("SCRAP_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date scrapDate = new Date();


    /**
     * 安装位置
     */
    @Schema(title = "安装位置")
    @TableField("ADDRESS")
    private String address = StringUtils.EMPTY;


    /**
     * 使用部门
     */
    @Schema(title = "使用部门")
    @TableField("USE_DEPARTMENT")
    private String useDepartment = StringUtils.EMPTY;


    /**
     * 所属班组
     */
    @Schema(title = "所属班组")
    @TableField("USE_TEAM")
    private String useTeam = StringUtils.EMPTY;


    /**
     * 出厂编号
     */
    @Schema(title = "出厂编号")
    @TableField("BAR_CODE")
    private String barCode = StringUtils.EMPTY;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;


}