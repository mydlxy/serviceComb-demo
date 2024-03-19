package com.ca.mfd.prc.pm.entity;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 *
 * @Description: 操作指导书配置实体
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "操作指导书配置")
@TableName("PRC_PM_WORKSTATION_OPER_BOOK")
public class PmWorkstationOperBookEntity extends BaseEntity {

    /**
     * 
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_WORKSTATION_OPER_BOOK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间ID
     */
    @Schema(title = "车间ID")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("PRC_PM_LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 生产线
     */
    @Schema(title = "生产线")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;



    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * BOP工序号
     */
    @Schema(title = "BOP工序号")
    @TableField("PRC_PM_PROCESS_NO")
    private Integer prcPmProcessNo = 0;

    /**
     * 作业步骤
     */
    @Schema(title = "作业步骤")
    @TableField("PROCESS_STEP")
    private String processStep = StringUtils.EMPTY;


    /**
     * 操作名称
     */
    @Schema(title = "操作名称")
    @TableField("WO_BOOK_NAME")
    @NotBlank(message = "操作名称不能为空")
    private String woBookName = StringUtils.EMPTY;


    /**
     * 文件地址
     */
    @Schema(title = "文件地址")
    @TableField("WO_BOOK_PATH")
    @NotBlank(message = "文件地址不能为空")
    private String woBookPath = StringUtils.EMPTY;


    /**
     * 图册类型;1，操作图册，2，零件图册,3 PFMEA文件
     */
    @Schema(title = "图册类型;1，操作图册，2，零件图册,3 PFMEA文件")
    @TableField("WO_BOOK_TYPE")
    @NotNull(message = "图册类型不能为空")
    private Integer woBookType = 0;


    /**
     * 物料名称
     */
    @Schema(title = "物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    @TableField("FEATURE_CODE")
    @NotBlank(message = "特征表达式不能为空")
    private String featureCode = StringUtils.EMPTY;

    /**
     * 特征表达式
     */
    @Schema(title = "顺序")
    @TableField("DISPLAY_NO")
    @NotNull(message = "顺序")
    private Integer displayNo = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    @TableField(exist = false)
    @JsonIgnore
    private String actionImage = StringUtils.EMPTY;

    @TableField(exist = false)
    @JsonIgnore
    private String materialImage = StringUtils.EMPTY;


    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;



}