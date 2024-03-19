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
@TableName("PRC_PPS_MAIN_UP_LINE_CONFIG")
public class PpsMainUpLineConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MAIN_UP_LINE_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 配置编码（唯一）
     */
    @Schema(title = "配置编码（唯一）")
    @TableField("CONFIG_CODE")
    private String configCode;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 组件编码
     */
    @Schema(title = "组件编码")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    @TableField("COMPONENT_DES")
    private String componentDes;


    /**
     * PLC地址
     */
    @Schema(title = "PLC地址")
    @TableField("PLC_ADDRESS")
    private String plcAddress = StringUtils.EMPTY;


    /**
     * DB块
     */
    @Schema(title = "DB块")
    @TableField("PLC_DB")
    private String plcDb = StringUtils.EMPTY;


}