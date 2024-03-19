package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

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

/**
 * @author inkelink ${email}
 * @Description: 生产报废订单
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生产报废订单")
@TableName("PRC_PPS_ORDER_SCRAP")
public class PpsOrderScrapEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ORDER_SCRAP_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 作废单号
     */
    @Schema(title = "作废单号")
    @TableField("SCRAP_NO")
    private String scrapNo = StringUtils.EMPTY;

    /**
     * 报废订单
     */
    @Schema(title = "报废订单")
    @TableField("ORDER_NO")
    private String orderNo = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;

    /**
     * 报废数量
     */
    @Schema(title = "报废数量")
    @TableField("QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qty = 1;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 确认备注
     */
    @Schema(title = "确认备注")
    @TableField("CONFIRM_REMARK")
    private String confirmRemark = StringUtils.EMPTY;

    /**
     * 订单来源
     */
    @Schema(title = "订单来源")
    @TableField("ORDER_SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderSource = 0;

    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 是否确认
     */
    @Schema(title = "是否确认")
    @TableField("IS_CONFIRM")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isConfirm = false;

    /**
     * 是否发送
     */
    @Schema(title = "是否发送")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isSend = false;

}
