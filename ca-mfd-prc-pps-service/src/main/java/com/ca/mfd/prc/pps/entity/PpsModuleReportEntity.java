package com.ca.mfd.prc.pps.entity;

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
 * @Description: 预成组报工单实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "预成组报工单")
@TableName("PRC_PPS_MODULE_REPORT")
public class PpsModuleReportEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_REPORT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划号
     */
    @Schema(title = "计划号")
    @TableField("PPS_PLAN_NO")
    private String ppsPlanNo = StringUtils.EMPTY;


    /**
     * 工单编号
     */
    @Schema(title = "工单编号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 分组号
     */
    @Schema(title = "分组号")
    @TableField("GROUP_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer groupNo = 1;


    /**
     * 生产区域
     */
    @Schema(title = "生产区域")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 生产线体
     */
    @Schema(title = "生产线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 模组编码
     */
    @Schema(title = "模组编码")
    @TableField("MODULE_CODE")
    private String moduleCode = StringUtils.EMPTY;


    /**
     * 模组条码（报工上传）
     */
    @Schema(title = "模组条码（报工上传）")
    @TableField("MODULE_BARCODE")
    private String moduleBarcode = StringUtils.EMPTY;


    /**
     * 0 未报工 1 合格 2 不合格
     */
    @Schema(title = "0 未报工 1 合格 2 不合格")
    @TableField("REPORT_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportStatus = 0;


}