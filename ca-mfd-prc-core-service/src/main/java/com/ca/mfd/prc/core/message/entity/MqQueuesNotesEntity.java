package com.ca.mfd.prc.core.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jay.he
 * @Description: 队列通道
 * @date 2023年09月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "队列通道")
@TableName("PRC_MQ_QUEUES_NOTES")
public class MqQueuesNotesEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MQ_QUEUES_NOTES_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 主题
     */
    @Schema(title = "主题")
    @TableField("TOPIC")
    private String topic = StringUtils.EMPTY;

    /**
     * 组名（主题）
     */
    @Schema(title = "组名（主题）")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 模块唯一标识
     */
    @Schema(title = "模块唯一标识")
    @TableField("APP_ID")
    private String appId = StringUtils.EMPTY;

    /**
     * 参数JSON
     */
    @Schema(title = "参数JSON")
    @TableField("PARAMS_JSON")
    private String paramsJson = StringUtils.EMPTY;

    /**
     * 通道名称
     */
    @Schema(title = "通道名称")
    @TableField("CHANNEL_NAME")
    private String channelName = StringUtils.EMPTY;

    /**
     * 状态 1-开启。2-禁用
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;

    /**
     * 队列数量
     */
    @Schema(title = "队列数量")
    @TableField("NUMBER")
    private Integer number = 0;

    /**
     * 类名
     */
    @Schema(title = "类名")
    @TableField("CLASS_NAME")
    private String className = StringUtils.EMPTY;

    /**
     * 命名空间
     */
    @Schema(title = "命名空间")
    @TableField("NAMESPACE_NAME")
    private String namespaceName = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}