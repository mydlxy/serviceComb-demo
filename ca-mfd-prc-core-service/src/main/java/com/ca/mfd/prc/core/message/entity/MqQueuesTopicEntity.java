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

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay.he
 * @Description: 队列主题
 * @date 2023年09月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "队列主题")
@TableName("PRC_MQ_QUEUES_TOPIC")
public class MqQueuesTopicEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MQ_QUEUES_TOPIC_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

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
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 通道列表
     */
    @Schema(title = "通道列表")
    @TableField(exist = false)
    private List<MqQueuesNotesEntity> children = new ArrayList<>();


}