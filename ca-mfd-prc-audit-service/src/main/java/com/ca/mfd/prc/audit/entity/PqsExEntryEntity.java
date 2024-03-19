package com.ca.mfd.prc.audit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 精致工艺评审单实体
 * @date 2024年01月30日
 * @变更说明 BY inkelink At 2024年01月30日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "精致工艺评审单")
@TableName("PRC_PQS_EX_ENTRY")
public class PqsExEntryEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_EX_ENTRY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 类别;1、整车  5 冲压
     */
    @Schema(title = "类别;1、整车  5 冲压")
    @TableField("CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 0;


    /**
     * 评审单号
     */
    @Schema(title = "评审单号")
    @TableField("RECORD_NO")
    private String recordNo = StringUtils.EMPTY;


    /**
     * 车型代码
     */
    @Schema(title = "车型代码")
    @TableField("MODEL_CODE")
    private String modelCode = StringUtils.EMPTY;


    /**
     * 车型名称
     */
    @Schema(title = "车型名称")
    @TableField("MODEL_NAME")
    private String modelName = StringUtils.EMPTY;


    /**
     * 车身颜色;车身颜色，整车才有
     */
    @Schema(title = "车身颜色;车身颜色，整车才有")
    @TableField("CHARACTERISTIC2")
    private String characteristic2 = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 缺陷总数
     */
    @Schema(title = "缺陷总数")
    @TableField("DEFECT_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer defectCount = 0;


    /**
     * 总扣分
     */
    @Schema(title = "总扣分")
    @TableField("TOTAL_SCORE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer totalScore = 0;


    /**
     * 产品条码;除零件外，其他为VIN
     */
    @Schema(title = "产品条码;除零件外，其他为VIN")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * VIN
     */
    @Schema(title = "VIN")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 制造日期;yyyy-MM-dd  零部件手动选择，其他：各车间上线时间
     */
    @Schema(title = "制造日期;yyyy-MM-dd  零部件手动选择，其他：各车间上线时间")
    @TableField("MANUFACTURE_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date manufactureDt = new Date();


    /**
     * 评价模式代码
     */
    @Schema(title = "评价模式代码")
    @TableField("EVALUATION_MODE_CODE")
    private String evaluationModeCode;


    /**
     * 评价模式名称
     */
    @Schema(title = "评价模式名称")
    @TableField("EVALUATION_MODE_NAME")
    private String evaluationModeName;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 状态;1、未开始 2、进行中、20暂存 90完成
     */
    @Schema(title = "状态;1、未开始 2、进行中、20暂存 90完成")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 数量;用于离散，整车没有
     */
    @Schema(title = "数量;用于离散，整车没有")
    @TableField("AMOUNTS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer amounts = 0;


}