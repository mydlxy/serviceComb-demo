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
 * @Description: 电池分线计划配置实体
 * @author inkelink
 * @date 2024年01月22日
 * @变更说明 BY inkelink At 2024年01月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池分线计划配置")
@TableName("PRC_PPS_PACK_BRANCHING_PLAN_CONFIG")
public class PpsPackBranchingPlanConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PACK_BRANCHING_PLAN_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 工位组
     */
    @Schema(title = "工位组")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


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


    /**
     * 错误消息
     */
    @Schema(title = "错误消息")
    @TableField("ERROR_MES")
    private String errorMes;

    /**
     * 计划类型：1：整车  2：电池包   7:备件 10：电池盖板分装 11：BDU（电池高压盒） 12：DMS（电池管理器）
     */
    @Schema(title = "计划类型：1：整车  2：电池包   7:备件 10：电池盖板分装 11：BDU（电池高压盒） 12：DMS（电池管理器）")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 产品码长度
     */
    @Schema(title = "产品码长度")
    @TableField("BAR_CODE_LEN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer barCodeLen = 0;

    /**
     * 生产路线ID
     */
    @Schema(title = "生产路线ID")
    @TableField("PRC_PPS_PRODUCT_PROCESS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsProductProcessId = Constant.DEFAULT_ID;

}