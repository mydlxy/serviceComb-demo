package com.ca.mfd.prc.pm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @Description: MBOM日志实体
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "MBOM日志")
@TableName("PRC_PM_BOP_BOM_DETAIL")
public class PmBopBomDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_BOP_BOM_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * BOM_ID
     */
    @Schema(title = "BOM_ID")
    @TableField("FROM_MBOM_ID")
    private String fromMbomId = StringUtils.EMPTY;


    /**
     * 返回码
     */
    @Schema(title = "返回码")
    @TableField("CODE")
    private String code = StringUtils.EMPTY;


    /**
     * 错误信息
     */
    @Schema(title = "错误信息")
    @TableField("MSG")
    private String msg = StringUtils.EMPTY;


    /**
     * 返回值
     */
    @Schema(title = "返回值")
    @TableField("DATAS")
    private String datas;


}