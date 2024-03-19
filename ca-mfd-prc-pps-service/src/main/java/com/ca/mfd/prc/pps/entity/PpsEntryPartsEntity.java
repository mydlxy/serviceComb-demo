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
 * @Description: 工单-零部件实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "工单-零部件")
@TableName("PRC_PPS_ENTRY_PARTS")
public class PpsEntryPartsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ENTRY_PARTS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 订单大类;3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单大类;3：压铸  4：机加   5：冲压  6：电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 整车编号
     */
    @Schema(title = "整车编号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 是否冻结
     */
    @Schema(title = "是否冻结")
    @TableField("IS_FREEZE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFreeze = false;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;


    /**
     * 工序编号
     */
    @Schema(title = "工序编号")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;


    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 预计上线时间
     */
    @Schema(title = "预计上线时间")
    @TableField("ESTIMATED_START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date estimatedStartDt = new Date();


    /**
     * 预计下线时间
     */
    @Schema(title = "预计下线时间")
    @TableField("ESTIMATED_END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date estimatedEndDt = new Date();


    /**
     * 工单计划数量
     */
    @Schema(title = "工单计划数量")
    @TableField("PLAN_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planQuantity = 0;


    /**
     * 合格数量
     */
    @Schema(title = "合格数量")
    @TableField("QUALIFIED_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qualifiedQuantity = 0;


    /**
     * 前工序不合格数量
     */
    @Schema(title = "前工序不合格数量")
    @TableField("SUBTRACT_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer subtractQuantity = 0;


    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    @TableField("SCRAP_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer scrapQuantity = 0;

    /**
     * 已下发数量
     */
    @Schema(title = "已下发数量")
    @TableField("DOWN_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer downQuantity = 0;

    /**
     * 状态;1 未生产  2 备料锁定 3、已锁定  10 开始生产  20 已经完成 90 关闭
     */
    @Schema(title = "状态;1 未生产  2 备料锁定 3、已锁定  10 开始生产  20 已经完成  90 关闭")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 是否发送给三方系统
     */
    @Schema(title = "是否发送给三方系统")
    @TableField("IS_SEND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSend = false;


    /**
     * 实际上线时间
     */
    @Schema(title = "实际上线时间")
    @TableField("ACTUAL_START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualStartDt;


    /**
     * 实际下下时间
     */
    @Schema(title = "实际下下时间")
    @TableField("ACTUAL_END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualEndDt;


}