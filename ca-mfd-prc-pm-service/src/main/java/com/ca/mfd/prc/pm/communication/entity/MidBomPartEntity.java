package com.ca.mfd.prc.pm.communication.entity;

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
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 零件BOM中间表实体
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "零件BOM中间表")
@TableName("PRC_MID_BOM_PART")
public class MidBomPartEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_BOM_PART_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 父级物料编码
     */
    @Schema(title = "父级物料编码")
    @TableField("PARENTMATERIAL_CODE")
    private String parentmaterialCode = StringUtils.EMPTY;


    /**
     * 用量
     */
    @Schema(title = "用量")
    @TableField("QUANTITY")
    private String quantity = StringUtils.EMPTY;


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
     * 使用工艺类型
     */
    @Schema(title = "使用工艺类型")
    @TableField("USE_PROCESS_TYPE")
    private String useProcessType = StringUtils.EMPTY;


    /**
     * 制造工厂
     */
    @Schema(title = "制造工厂")
    @TableField("MANUFACTURING_PLANT")
    private String manufacturingPlant = StringUtils.EMPTY;


    /**
     * 制造工艺类型
     */
    @Schema(title = "制造工艺类型")
    @TableField("MANUFACTURING_PROCESS_TYPE")
    private String manufacturingProcessType = StringUtils.EMPTY;


    /**
     * 颜色件
     */
    @Schema(title = "颜色件")
    @TableField("COLOR_PART_CODE")
    private String colorPartCode = StringUtils.EMPTY;


    /**
     * 颜色代码
     */
    @Schema(title = "颜色代码")
    @TableField("COLOR_CODE")
    private String colorCode = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @TableField("EFFECTIVE_FROM")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @TableField("EFFECTIVE_TO")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();


    /**
     * 关重件特性
     */
    @Schema(title = "关重件特性")
    @TableField("IMPT_PARTS_SPECIAL")
    private String imptPartsSpecial = StringUtils.EMPTY;


    /**
     * 零部件型号
     */
    @Schema(title = "零部件型号")
    @TableField("PART_MODEL")
    private String partModel = StringUtils.EMPTY;


    /**
     * 3C件类型
     */
    @Schema(title = "3C件类型")
    @TableField("THREE_C_PART_TYPE")
    private String threeCPartType = StringUtils.EMPTY;


    /**
     * 环保件
     */
    @Schema(title = "环保件")
    @TableField("ENVIR_PROTECTION_PART")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean envirProtectionPart = false;


    /**
     * 是否公告件
     */
    @Schema(title = "是否公告件")
    @TableField("IS_ANNOUNCEMENT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isAnnouncement = false;


    /**
     * 证书编号
     */
    @Schema(title = "证书编号")
    @TableField("CERTIFICATE_NUMBER")
    private String certificateNumber = StringUtils.EMPTY;


    /**
     * 认证工厂代码
     */
    @Schema(title = "认证工厂代码")
    @TableField("CERTF_FACTORY_CODE")
    private String certfFactoryCode = StringUtils.EMPTY;


    /**
     * 生产企业名称
     */
    @Schema(title = "生产企业名称")
    @TableField("LIFE_ENTERPRISE_NAME")
    private String lifeEnterpriseName = StringUtils.EMPTY;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @TableField("CHECK_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    @TableField("CHECK_RESULT_DESC")
    private String checkResultDesc = StringUtils.EMPTY;


}