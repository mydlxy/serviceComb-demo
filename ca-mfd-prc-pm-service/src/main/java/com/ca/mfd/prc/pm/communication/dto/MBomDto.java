package com.ca.mfd.prc.pm.communication.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 *
 * @Description: 制造域BOM实体
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@Data
@Schema(description= "制造域DTO")
public class MBomDto  {


    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;
    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialCode = StringUtils.EMPTY;
    /**
     * 物料编码
     */
    @Schema(title = "物料名称")
    private String materialNameCh = StringUtils.EMPTY;


    /**
     * 父级物料编码
     */
    @Schema(title = "父级物料编码")
    private String parentMaterialCode = StringUtils.EMPTY;


    /**
     * 用量
     */
    @Schema(title = "用量")
    private String quantity = StringUtils.EMPTY;


    /**
     * 计量单位
     */
    @Schema(title = "计量单位")
    private String measureUnit = StringUtils.EMPTY;


    /**
     * 物料类型
     */
    @Schema(title = "物料类型")
    private String materialType = StringUtils.EMPTY;


    /**
     * 研发供货状态
     */
    @Schema(title = "研发供货状态")
    private String rdSupplyStatus = StringUtils.EMPTY;


    /**
     * 使用规则
     */
    @Schema(title = "使用规则")
    private String usageValue = StringUtils.EMPTY;


    /**
     * 规则描述
     */
    @Schema(title = "规则描述")
    private String usageDesc = StringUtils.EMPTY;


    /**
     * 模块编码
     */
    @Schema(title = "模块编码")
    private String moduleCode = StringUtils.EMPTY;


    /**
     * 模块名称
     */
    @Schema(title = "模块名称")
    private String moduleName = StringUtils.EMPTY;


    /**
     * 合件分组号
     */
    @Schema(title = "合件分组号")
    private String componentGroupNumber = StringUtils.EMPTY;


    /**
     * 替代组
     */
    @Schema(title = "替代组")
    private String replaceGroup = StringUtils.EMPTY;


    /**
     * 配套组
     */
    @Schema(title = "配套组")
    private String supportGroup = StringUtils.EMPTY;


    /**
     * 研发应用工程师姓名
     */
    @Schema(title = "研发应用工程师姓名")
    private String employeeIdDisplayName = StringUtils.EMPTY;


    /**
     * 制造供货状态
     */
    @Schema(title = "制造供货状态")
    private String manufacturingSupplyStatus = StringUtils.EMPTY;


    /**
     * 使用工厂
     */
    @Schema(title = "使用工厂")
    private String usePlant = StringUtils.EMPTY;


    /**
     * 使用工艺类型
     */
    @Schema(title = "使用工艺类型")
    private String useProcessType = StringUtils.EMPTY;


    /**
     * 制造工厂
     */
    @Schema(title = "制造工厂")
    private String manufacturingPlant = StringUtils.EMPTY;


    /**
     * 制造工艺类型
     */
    @Schema(title = "制造工艺类型")
    private String manufacturingProcessType = StringUtils.EMPTY;


    /**
     * 工艺层级
     */
    @Schema(title = "工艺层级")
    private String processLevel = StringUtils.EMPTY;


    /**
     * 制造BOM工程师姓名
     */
    @Schema(title = "制造BOM工程师姓名")
    private String mbomEngineerIdDisplayName = StringUtils.EMPTY;


    /**
     * 制造BOM工程师维护时间
     */
    @Schema(title = "制造BOM工程师维护时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date mbomEngineerUpdateDate = new Date();


    /**
     * 工艺设计工程师姓名
     */
    @Schema(title = "工艺设计工程师姓名")
    private String technicsEngineerIdDisplayName = StringUtils.EMPTY;


    /**
     * 工艺设计工程师维护时间
     */
    @Schema(title = "工艺设计工程师维护时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date technicsEngineerUpdateDate = new Date();


    /**
     * 设计通知书号
     */
    @Schema(title = "设计通知书号")
    private String sourceChangeCode = StringUtils.EMPTY;


    /**
     * 制造通知书号
     */
    @Schema(title = "制造通知书号")
    private String changeCode = StringUtils.EMPTY;


    /**
     * 切换通知书号
     */
    @Schema(title = "切换通知书号")
    private String breakPointCode = StringUtils.EMPTY;


    /**
     * 断点状态
     */
    @Schema(title = "断点状态")
    private String breakPointChangeStatus = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();

    /**
     * BOM行号
     */
    @Schema(title = "BOM行号")
    private String lineNumber = StringUtils.EMPTY;
    /**
     * 使用工艺类型编码
     */
    @Schema(title = "使用工艺类型编码")
    private String useProcessTypeCode = StringUtils.EMPTY;
    /**
     * 制造工艺类型编码
     */
    @Schema(title = "制造工艺类型编码")
    private String manufacturingProcessTypeCode = StringUtils.EMPTY;

    /**
     * bom房间号
     */
    @Schema(title = "bom房间号")
    private String bomRoom = StringUtils.EMPTY;


}