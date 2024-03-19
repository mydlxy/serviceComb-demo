package com.ca.mfd.prc.pps.entity;

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

import java.util.Date;

/**
 *
 * @Description: 零件报废实体
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "零件报废")
@TableName("PRC_PPS_PARTS_SCRAP")
public class PpsPartsScrapEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PARTS_SCRAP_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 作废单号
     */
    @Schema(title = "作废单号")
    @TableField("SCRAP_NO")
    private String scrapNo = StringUtils.EMPTY;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 产品条码
     */
    @Schema(title = "产品条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 报废数量
     */
    @Schema(title = "报废数量")
    @TableField("QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qty = 0;


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
     * 订单大类： 3：压铸  4：机加   5：冲压  6：电池上盖 8 预程组
     */
    @Schema(title = "订单大类： 3：压铸  4：机加   5：冲压  6：电池上盖 8 预程组")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 是否确认
     */
    @Schema(title = "是否确认")
    @TableField("IS_CONFIRM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isConfirm = false;


    /**
     * 是否发送
     */
    @Schema(title = "是否发送")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


}