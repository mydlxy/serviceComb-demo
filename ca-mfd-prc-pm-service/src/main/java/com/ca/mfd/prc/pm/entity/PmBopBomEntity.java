package com.ca.mfd.prc.pm.entity;

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
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: MBOM详情实体
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "MBOM详情")
@TableName("PRC_PM_BOP_BOM")
public class PmBopBomEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_BOP_BOM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * BOM_ID
     */
    @Schema(title = "BOM_ID")
    @TableField("FROM_MBOM_ID")
    private String fromMbomId = StringUtils.EMPTY;


    /**
     * BOP行号
     */
    @Schema(title = "BOP行号")
    @TableField("ROW_NUM")
    private String rowNum = StringUtils.EMPTY;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;



    /**
     * 物料编码
     */
    @Schema(title = "物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 父级物料编码
     */
    @Schema(title = "父级物料编码")
    @TableField("MASTER_MATERIAL_CODE")
    private String masterMaterialCode = StringUtils.EMPTY;


    /**
     * 车系
     */
    @Schema(title = "车系")
    @TableField("VEHICLE_SERIES")
    private String vehicleSeries = StringUtils.EMPTY;


    /**
     * 用量
     */
    @Schema(title = "用量")
    @TableField("QUANTITY")
    private String quantity = StringUtils.EMPTY;


    /**
     * 计量单位
     */
    @Schema(title = "计量单位")
    @TableField("MEASURE_UNIT")
    private String measureUnit = StringUtils.EMPTY;


    /**
     * 物料类型
     */
    @Schema(title = "物料类型")
    @TableField("MATERIAL_TYPE_CODE")
    private String materialTypeCode = StringUtils.EMPTY;


    /**
     * 研发供货状态
     */
    @Schema(title = "研发供货状态")
    @TableField("RD_SUPPLY_STATUS")
    private String rdSupplyStatus = StringUtils.EMPTY;


    /**
     * 使用规则
     */
    @Schema(title = "使用规则")
    @TableField("USAGE_VALUE")
    private String usageValue = StringUtils.EMPTY;


    /**
     * 规则描述
     */
    @Schema(title = "规则描述")
    @TableField("USAGE_DESC")
    private String usageDesc = StringUtils.EMPTY;


    /**
     * 模块编码
     */
    @Schema(title = "模块编码")
    @TableField("MODULE_CODE")
    private String moduleCode = StringUtils.EMPTY;


    /**
     * 模块名称
     */
    @Schema(title = "模块名称")
    @TableField("MODULE_NAME")
    private String moduleName = StringUtils.EMPTY;


    /**
     * 合件分组
     */
    @Schema(title = "合件分组")
    @TableField("COMPOSITES_NUM")
    private String compositesNum = StringUtils.EMPTY;


    /**
     * 替代组
     */
    @Schema(title = "替代组")
    @TableField("REPLACE_GROUP")
    private String replaceGroup = StringUtils.EMPTY;


    /**
     * 配套组
     */
    @Schema(title = "配套组")
    @TableField("SUPPORT_GROUP")
    private String supportGroup = StringUtils.EMPTY;


    /**
     * 研发应用工程师姓名
     */
    @Schema(title = "研发应用工程师姓名")
    @TableField("EMPLOYEEID_DISPLAY_NAME")
    private String employeeidDisplayName = StringUtils.EMPTY;


    /**
     * 制造供货状态
     */
    @Schema(title = "制造供货状态")
    @TableField("MANUFACTURING_SUPPLY_STATUS")
    private String manufacturingSupplyStatus = StringUtils.EMPTY;


    /**
     * 使用工厂
     */
    @Schema(title = "使用工厂")
    @TableField("USE_PLANT")
    private String usePlant = StringUtils.EMPTY;


    /**
     * 使用车间
     */
    @Schema(title = "使用车间")
    @TableField("USE_WORK_SHOP")
    private String useWorkShop = StringUtils.EMPTY;


    /**
     * 制造工厂
     */
    @Schema(title = "制造工厂")
    @TableField("MANUFACTURING_PLANT")
    private String manufacturingPlant = StringUtils.EMPTY;


    /**
     * 制造车间
     */
    @Schema(title = "制造车间")
    @TableField("MANUFACTURING_WORKSHOP")
    private String manufacturingWorkshop = StringUtils.EMPTY;


    /**
     * 工艺层级
     */
    @Schema(title = "工艺层级")
    @TableField("PROCESS_LEVEL")
    private String processLevel = StringUtils.EMPTY;


    /**
     * 制造BOM工程师姓名
     */
    @Schema(title = "制造BOM工程师姓名")
    @TableField("MBOM_ENGINEER_NAME")
    private String mbomEngineerName = StringUtils.EMPTY;


    /**
     * 制造BOM工程师维护时间
     */
    @Schema(title = "制造BOM工程师维护时间")
    @TableField("MBOM_ENGINEER_UPDATEDATE")
    private String mbomEngineerUpdatedate = StringUtils.EMPTY;


    /**
     * 工艺设计工程师姓名
     */
    @Schema(title = "工艺设计工程师姓名")
    @TableField("TECHNICS_ENGINEER_NAME")
    private String technicsEngineerName = StringUtils.EMPTY;


    /**
     * 工艺设计工程师维护时间
     */
    @Schema(title = "工艺设计工程师维护时间")
    @TableField("TECHNICS_ENGINEER_UPDATEDATE")
    private String technicsEngineerUpdatedate = StringUtils.EMPTY;


    /**
     * 设计通知书号
     */
    @Schema(title = "设计通知书号")
    @TableField("SOURC_ECHANGE_CODE")
    private String sourcEchangeCode = StringUtils.EMPTY;


    /**
     * 制造通知书号
     */
    @Schema(title = "制造通知书号")
    @TableField("CHANGE_CODE")
    private String changeCode = StringUtils.EMPTY;


    /**
     * 切换通知书号
     */
    @Schema(title = "切换通知书号")
    @TableField("BREAK_POINT_CODE")
    private String breakPointCode = StringUtils.EMPTY;


    /**
     * 断点状态
     */
    @Schema(title = "断点状态")
    @TableField("BREAK_POINT_CHANGES_TATUS")
    private String breakPointChangesTatus = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @TableField("EFFECTIVE_FROM")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @TableField("EFFECTIVE_TO")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date effectiveTo = new Date(2099,11,30,23,59,59);


    /**
     * 切换状态
     */
    @Schema(title = "是否切换件")
    @TableField(exist = false)
    private boolean breakPointFlag = false;

    /**
     * 工位编码集合，多个工位之间用英文逗号分割
     */
    @TableField(exist = false)
    private String workstationCodes = StringUtils.EMPTY;

    /**
     * 工位零件数量
     */
    @TableField(exist = false)
    private String workstationMaterialNums = StringUtils.EMPTY;


    /**
     * BOP行号和物料编码中间用|分割
     * @return
     */
    public String getRowNumAndMaterialNo(){
        return rowNum + "|" + materialCode;
    }


}