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
 * @Description: 工序关联配置实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "工序关联配置")
@TableName("PRC_PPS_PROCESS_RELATION")
public class PpsProcessRelationEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PROCESS_RELATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 订单大类;1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7、备件、8电池上盖
     */
    @Schema(title = "订单大类;1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7、备件、8电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;

    /**
     * 工序代码
     */
    @Schema(title = "工序代码")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;

    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("SHOP_CODE")
    private String shopCode = StringUtils.EMPTY;

    /**
     * 线体代码;1、普通、2、设备、3、模具
     */
    @Schema(title = "线体代码;1、普通、2、设备、3、模具")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体名称;1、普通、2、设备、3、模具
     */
    @Schema(title = "线体名称;1、普通、2、设备、3、模具")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 工艺类型;0、过程工艺 1、首道工艺 2末道工艺
     */
    @Schema(title = "工艺类型;0、过程工艺 1、首道工艺 2末道工艺")
    @TableField("PROCESS_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer processType = 0;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled = false;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}