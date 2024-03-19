package com.ca.mfd.prc.eps.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 熔化炉配置实体
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "熔化炉配置")
@TableName("PRC_EPS_FURNACE_CONFIG")
public class EpsFurnaceConfigEntity extends BaseEntity {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @TableId(value = "PRC_EPS_FURNACE_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 熔炉编号
     */
    @Schema(title = "熔炉编号")
    @TableField("FURNACE_NO")
    private String furnaceNo = StringUtils.EMPTY;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;


    /**
     * 新料比例(%)
     */
    @Schema(title = "新料比例(%)")
    @TableField("NEW_RATE")
    private BigDecimal newRate = BigDecimal.valueOf(0);


    /**
     * 回炉料比例(%)
     */
    @Schema(title = "回炉料比例(%)")
    @TableField("REUSE_RATE")
    private BigDecimal reuseRate = BigDecimal.valueOf(0);


    /**
     * 补充说明
     */
    @Schema(title = "补充说明")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}