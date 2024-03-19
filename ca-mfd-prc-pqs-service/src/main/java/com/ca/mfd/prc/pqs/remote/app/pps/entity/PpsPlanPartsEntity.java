package com.ca.mfd.prc.pqs.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 生产计划-零部件实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "生产计划-零部件")
@TableName("PRC_PPS_PLAN_PARTS")
public class PpsPlanPartsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PLAN_PARTS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;

    /**
     * 工厂
     */
    @Schema(title = "工厂")
    @TableField("PLANT")
    private String plant = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    @TableField("PRODUCT_CODE")
    private String productCode = StringUtils.EMPTY;

    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

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
     * 计划生产数量
     */
    @Schema(title = "计划生产数量")
    @TableField("PLAN_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planQty = 0;

    /**
     * 计划锁定数量
     */
    @Schema(title = "计划锁定数量")
    @TableField("LOCK_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer lockQty = 0;

    /**
     * 是否冻结
     */
    @Schema(title = "是否冻结")
    @TableField("IS_FREEZE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFreeze = false;

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
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * BOM版本号
     */
    @Schema(title = "BOM版本号")
    @TableField("BOM_VERSION")
    private String bomVersion = StringUtils.EMPTY;

    /**
     * 错误信息
     */
    @Schema(title = "错误信息")
    @TableField("ERROR_MES")
    private String errorMes = StringUtils.EMPTY;

    /**
     * 计划状态;1、待处理 2、已排产 10、生产开始 20、生产完成
     */
    @Schema(title = "计划状态;1、待处理 2、已排产 10、生产开始 20、生产完成")
    @TableField("PLAN_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planStatus = 0;

    /**
     * 计划来源
     */
    @Schema(title = "计划来源")
    @TableField("PLAN_SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planSource = 0;

    /**
     * 实际上线时间
     */
    @Schema(title = "实际上线时间")
    @TableField("ACTUAL_START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualStartDt;

    /**
     * 实际下线时间
     */
    @Schema(title = "实际下线时间")
    @TableField("ACTUAL_END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date actualEndDt;

    /**
     * 订单大类;3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单大类;3：压铸  4：机加   5：冲压  6：电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;

    /**
     * 特征1
     */
    @Schema(title = "特征1")
    @TableField("CHARACTERISTIC1")
    private String characteristic1 = StringUtils.EMPTY;

    /**
     * 特征2
     */
    @Schema(title = "特征2")
    @TableField("CHARACTERISTIC2")
    private String characteristic2 = StringUtils.EMPTY;

    /**
     * 特征3
     */
    @Schema(title = "特征3")
    @TableField("CHARACTERISTIC3")
    private String characteristic3 = StringUtils.EMPTY;

    /**
     * 特征4
     */
    @Schema(title = "特征4")
    @TableField("CHARACTERISTIC4")
    private String characteristic4 = StringUtils.EMPTY;

    /**
     * 特征5
     */
    @Schema(title = "特征5")
    @TableField("CHARACTERISTIC5")
    private String characteristic5 = StringUtils.EMPTY;

    /**
     * 特征6（基础车型）
     */
    @Schema(title = "特征6（基础车型）")
    @TableField("CHARACTERISTIC6")
    private String characteristic6 = StringUtils.EMPTY;

    /**
     * 特征7(天窗状态码)(N-无，S-小天窗，A-全景)
     */
    @Schema(title = "特征7(天窗状态码)")
    @TableField("CHARACTERISTIC7")
    private String characteristic7 = StringUtils.EMPTY;

    /**
     * 特征8(双色车标识)(0-单色，1-双色)
     */
    @Schema(title = "特征8(双色车标识)")
    @TableField("CHARACTERISTIC8")
    private String characteristic8 = StringUtils.EMPTY;

    /**
     * 特征9
     */
    @Schema(title = "特征9")
    @TableField("CHARACTERISTIC9")
    private String characteristic9 = StringUtils.EMPTY;

    /**
     * 特征10
     */
    @Schema(title = "特征10")
    @TableField("CHARACTERISTIC10")
    private String characteristic10 = StringUtils.EMPTY;

    /**
     * 开始AVI
     */
    @Schema(title = "开始AVI")
    @TableField("START_AVI")
    private String startAvi = StringUtils.EMPTY;

    /**
     * 结束AVI
     */
    @Schema(title = "结束AVI")
    @TableField("END_AVI")
    private String endAvi = StringUtils.EMPTY;

}