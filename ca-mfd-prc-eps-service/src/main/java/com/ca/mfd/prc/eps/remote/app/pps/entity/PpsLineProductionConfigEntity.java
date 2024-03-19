package com.ca.mfd.prc.eps.remote.app.pps.entity;

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
 * @Description: 线体生产配置实体
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "线体生产配置")
@TableName("PRC_PPS_LINE_PRODUCTION_CONFIG")
public class PpsLineProductionConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_LINE_PRODUCTION_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 线体编号
     */
    @Schema(title = "线体编号")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7:备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7:备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;


}