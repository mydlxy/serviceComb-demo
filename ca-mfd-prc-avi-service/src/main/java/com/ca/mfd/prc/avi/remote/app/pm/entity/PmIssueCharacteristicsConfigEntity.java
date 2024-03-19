package com.ca.mfd.prc.avi.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 下发特征配置实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "下发特征配置")
@TableName("PRC_PM_ISSUE_CHARACTERISTICS_CONFIG")
public class PmIssueCharacteristicsConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_ISSUE_CHARACTERISTICS_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 关联数据订阅码
     */
    @Schema(title = "关联数据订阅码")
    @TableField("SUB_KEY")
    private String subKey = StringUtils.EMPTY;


    /**
     * 下发特征标识键
     */
    @Schema(title = "下发特征标识键")
    @TableField("FEATURE_KEY")
    private String featureKey = StringUtils.EMPTY;


    /**
     * 关联类型 1 工单 2 队列
     */
    @Schema(title = "关联类型 1 工单 2 队列")
    @TableField("RELEVANCE_TYPE")
    private Integer relevanceType = 0;


    /**
     * 特征项
     */
    @Schema(title = "特征项")
    @TableField("FEATURE_NAME")
    private String featureName = StringUtils.EMPTY;


}