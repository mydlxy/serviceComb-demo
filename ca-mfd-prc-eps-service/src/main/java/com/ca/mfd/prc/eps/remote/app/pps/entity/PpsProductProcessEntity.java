package com.ca.mfd.prc.eps.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
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

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 工艺路径设置
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = " 工艺路径设置")
@TableName("PRC_PPS_PRODUCT_PROCESS")
public class PpsProductProcessEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PRODUCT_PROCESS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工序编号
     */
    @Schema(title = "工序编号")
    @TableField("PROCESS_NO")
    private String processNo = StringUtils.EMPTY;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 版本
     */
    @Schema(title = "版本")
    @TableField("VERSION")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer version = 0;


    /**
     * 订单标记
     */
    @Schema(title = "订单标记")
    @TableField("ORDER_SIGN")
    private String orderSign = StringUtils.EMPTY;

    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 是否默认
     */
    @Schema(title = "是否默认")
    @TableField("IS_DEFAULT")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isDefault = false;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENBLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnble = true;

    /**
     * 过点信息
     */
    @Schema(title = "过点信息")
    @TableField(exist = false)
    private List<PpsProductProcessAviEntity> ppsProductProcessAviInfos;
}
