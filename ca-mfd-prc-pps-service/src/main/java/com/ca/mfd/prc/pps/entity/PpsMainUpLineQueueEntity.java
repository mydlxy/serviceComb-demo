package com.ca.mfd.prc.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 *
 * @Description: 焊装主体上线队列实体
 * @author inkelink
 * @date 2024年01月18日
 * @变更说明 BY inkelink At 2024年01月18日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "焊装主体上线队列")
@TableName("PRC_PPS_MAIN_UP_LINE_QUEUE")
public class PpsMainUpLineQueueEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MAIN_UP_LINE_QUEUE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 配置编码
     */
    @Schema(title = "配置编码")
    @TableField("CONFIG_CODE")
    private String configCode = StringUtils.EMPTY;


    /**
     * 计划号
     */
    @Schema(title = "计划号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 状态 0 未下发 1 已下发
     */
    @Schema(title = "状态 0 未下发 1 已下发")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    public String model = StringUtils.EMPTY;

    /**
     * 车辆状态码
     */
    @Schema(title = "车辆状态码")
    @TableField("CAR_STATUS")
    public String carStatus = StringUtils.EMPTY;
}