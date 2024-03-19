package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 参数预警配置实体
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"policyNo"})
@Schema(description= "参数预警配置")
@TableName("PRC_PQS_TECHNOLOGY_ALARM_POLICY")
public class PqsTechnologyAlarmPolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_TECHNOLOGY_ALARM_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 策略编号
     */
    @Schema(title = "策略编号")
    @TableField("POLICY_NO")
    private String policyNo = StringUtils.EMPTY;


    /**
     * 控制特征
     */
    @Schema(title = "控制特征")
    @TableField("CONTROL_PROPERTY")
    private String controlProperty = StringUtils.EMPTY;


    /**
     * 区域
     */
    @Schema(title = "区域")
    @TableField("AERA")
    private String aera = StringUtils.EMPTY;


    /**
     * 点位代码
     */
    @Schema(title = "点位代码")
    @TableField("POINT_CODE")
    private String pointCode = StringUtils.EMPTY;


    /**
     * 严重度（S)
     */
    @Schema(title = "严重度（S)")
    @TableField("YZD")
    private String yzd = StringUtils.EMPTY;


    /**
     * 频度预估（O）
     */
    @Schema(title = "频度预估（O）")
    @TableField("PDYG")
    private String pdyg = StringUtils.EMPTY;


    /**
     * 探测度（D）
     */
    @Schema(title = "探测度（D）")
    @TableField("TCD")
    private String tcd = StringUtils.EMPTY;


    /**
     * RPN
     */
    @Schema(title = "RPN")
    @TableField("RPN")
    private String rpn = StringUtils.EMPTY;


    /**
     * 超差预警
     */
    @Schema(title = "超差预警")
    @TableField("OVERPROOF_ALARM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean overproofAlarm = false;


    /**
     * 是否停机
     */
    @Schema(title = "是否停机")
    @TableField("IS_STOP")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isStop = false;


    /**
     * 计数
     */
    @Schema(title = "计数")
    @TableField("COUNTER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer counter = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled = false;


    /**
     * 处理方式
     */
    @Schema(title = "处理方式")
    @TableField("HANDLE_METHOD")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean handleMethod = false;


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