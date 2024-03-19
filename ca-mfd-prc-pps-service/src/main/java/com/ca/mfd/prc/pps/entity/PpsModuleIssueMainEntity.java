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
 * @Description: 电池预成组下发主体实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池预成组下发主体")
@TableName("PRC_PPS_MODULE_ISSUE_MAIN")
public class PpsModuleIssueMainEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_ISSUE_MAIN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 策略标记
     */
    @Schema(title = "策略标记")
    @TableField("STRATEGY_SIGN")
    private String strategySign = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 电池型号
     */
    @Schema(title = "电池型号")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


    /**
     * 电池数量
     */
    @Schema(title = "电池数量")
    @TableField("PACK_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer packNum = 0;


    /**
     * 生产区域
     */
    @Schema(title = "生产区域")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 下发数量
     */
    @Schema(title = "下发数量")
    @TableField("ISSUE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer issueNum = 0;


    /**
     * 完成下发的数量
     */
    @Schema(title = "完成下发的数量")
    @TableField("FINISH_ISSUE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer finishIssueNum = 0;


    /**
     * 完成状态 0 未下发 1 下发中 2 完成
     */
    @Schema(title = "完成状态 0 未下发 1 下发中 2 完成")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


}