package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 检验策略实体
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"triggerPolicyDesc"})
@Schema(description= "检验策略")
@TableName("PRC_PQS_INSPECT_POLICY")
public class PqsInspectPolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_INSPECT_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 抽检策略策略
     */
    @Schema(title = "抽检策略")
    @TableField("TRIGGER_POLICY_DESC")
    private String triggerPolicyDesc = StringUtils.EMPTY;


    /**
     * 工序代码
     */
    @Schema(title = "工序代码")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;


    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 抽检项目代码
     */
    @Schema(title = "抽检项目代码")
    @TableField("PROJECT_CODE")
    private String projectCode = StringUtils.EMPTY;


    /**
     * 抽检项目
     */
    @Schema(title = "抽检项目")
    @TableField("PROJECT_NAME")
    private String projectName = StringUtils.EMPTY;


    /**
     * 触发类型;1、普通、2、设备、3、模具
     */
    @Schema(title = "触发类型;1、普通、2、设备、3、模具")
    @TableField("TRIGGER_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer triggerType = 0;


    /**
     * 周期类型;0、分钟、1、小时、2、日、3、周、4、月、5、季度
     */
    @Schema(title = "周期类型;0、分钟、1、小时、2、日、3、周、4、月、5、季度")
    @TableField("PERIOD_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer periodType = 0;


    /**
     * 周期间隔
     */
    @Schema(title = "周期间隔")
    @TableField("PERIOD_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer periodQty = 0;


    /**
     * 生效时间
     */
    @Schema(title = "生效时间")
    @TableField("START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startDt = new Date();


    /**
     * 检验要求
     */
    @Schema(title = "检验要求")
    @TableField("CHECK_REQUEST")
    private String checkRequest = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 数据校验
     */
    @TableField(exist = false)
    private boolean dataCheck = true;
}