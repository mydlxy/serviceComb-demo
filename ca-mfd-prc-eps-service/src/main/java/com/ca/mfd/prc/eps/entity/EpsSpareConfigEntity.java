package com.ca.mfd.prc.eps.entity;

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
 * @Description: 区域对应备件配置实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "区域对应备件配置")
@TableName("PRC_EPS_SPARE_CONFIG")
public class EpsSpareConfigEntity extends BaseEntity {


    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_SPARE_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 备件物料名称
     */
    @Schema(title = "备件物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 备件物料编号
     */
    @Schema(title = "备件物料编号")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * AVI队列订阅码
     */
    @Schema(title = "AVI队列订阅码")
    @TableField("QUEUE_CODE")
    private String queueCode = StringUtils.EMPTY;


}