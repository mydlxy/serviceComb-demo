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
 * @author inkelink ${email}
 * @Description: 工单订阅配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "PPS_ENTRY对列表")
@TableName("PRC_PPS_ENTRY_CONFIG")
public class PpsEntryConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ENTRY_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 订阅对象（默认都是PLC,后期扩展第三方对象）
     */
    @Schema(title = "订阅对象（默认都是PLC,后期扩展第三方对象）")
    @TableField("SUB_OBJECT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer subObject = 0;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 订阅代码(A-Z)
     */
    @Schema(title = "订阅代码(A-Z)")
    @TableField("SUB_CODE")
    private String subCode = StringUtils.EMPTY;

    /**
     * PLC链接地址
     */
    @Schema(title = "PLC链接地址")
    @TableField("ENTRY_OPC_CONNECT")
    private String entryOpcConnect = StringUtils.EMPTY;

    /**
     * 工单DB
     */
    @Schema(title = "工单DB")
    @TableField("ENTRY_DB")
    private String entryDb = StringUtils.EMPTY;

    /**
     * 生成方式(1 订单拆分 2 AVI过点生成)
     */
    @Schema(title = "生成方式")
    @TableField("GENERATE_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer generateType = 1;

    /**
     * 产品类型
     */
    @Schema(title = "产品类型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 分线过点AVI 代码
     */
    @Schema(title = "车间代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * 分线过点AVI 名称
     */
    @Schema(title = "分线过点AVI 名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

}
