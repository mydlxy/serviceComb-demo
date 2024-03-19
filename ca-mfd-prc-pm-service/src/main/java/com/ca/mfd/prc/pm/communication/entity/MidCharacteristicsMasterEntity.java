package com.ca.mfd.prc.pm.communication.entity;

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
 * @Description: 特征主数据中间表实体
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "特征主数据中间表")
@TableName("PRC_MID_CHARACTERISTICS_MASTER")
public class MidCharacteristicsMasterEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_CHARACTERISTICS_MASTER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 特征组编码
     */
    @Schema(title = "特征组编码")
    @TableField("FEATURE_GROUP_CODE")
    private String featureGroupCode = StringUtils.EMPTY;


    /**
     * 特征组名称
     */
    @Schema(title = "特征组名称")
    @TableField("FEATURE_GROUP_NAME")
    private String featureGroupName = StringUtils.EMPTY;


    /**
     * 特征族编码
     */
    @Schema(title = "特征族编码")
    @TableField("FAMILY_CODE")
    private String familyCode = StringUtils.EMPTY;


    /**
     * 特征族名称
     */
    @Schema(title = "特征族名称")
    @TableField("FAMILY_NAME")
    private String familyName = StringUtils.EMPTY;


    /**
     * 特征族英文名称
     */
    @Schema(title = "特征族英文名称")
    @TableField("FAMILY_NAME_EN")
    private String familyNameEn = StringUtils.EMPTY;


    /**
     * 特征值编码
     */
    @Schema(title = "特征值编码")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;


    /**
     * 特征值名称
     */
    @Schema(title = "特征值名称")
    @TableField("FEATURE_NAME")
    private String featureName = StringUtils.EMPTY;


    /**
     * 特征值英文名称
     */
    @Schema(title = "特征值英文名称")
    @TableField("FEATURE_NAME_EN")
    private String featureNameEn = StringUtils.EMPTY;


    /**
     * 特征描述
     */
    @Schema(title = "特征描述")
    @TableField("FEATURE_DESC")
    private String featureDesc = StringUtils.EMPTY;


    /**
     * 是否选装包
     */
    @Schema(title = "是否选装包")
    @TableField("IS_OPTIONAL")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOptional = false;


    /**
     * 归属专业
     */
    @Schema(title = "归属专业")
    @TableField("BELONG_MAJOR")
    private String belongMajor = StringUtils.EMPTY;


    /**
     * 特征类别 N2_ENGINEERING：工程特征 N3_SALE：销售特征 N4_MARKET：市场特征
     */
    @Schema(title = "特征类别 N2_ENGINEERING：工程特征 N3_SALE：销售特征 N4_MARKET：市场特征")
    @TableField("FEATURE_CATEGORY")
    private String featureCategory = StringUtils.EMPTY;


    /**
     * 是否强制
     */
    @Schema(title = "是否强制")
    @TableField("IS_FORCED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isForced = false;


    /**
     * 销售分类 APPEARANCE：外观 JOKER：百搭 SAFETY：安全
     */
    @Schema(title = "销售分类 APPEARANCE：外观 JOKER：百搭 SAFETY：安全")
    @TableField("SALES_GROUP")
    private String salesGroup = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @TableField("EFFECTIVE_FROM")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 失效日期
     */
    @Schema(title = "失效日期")
    @TableField("EFFECTIVE_TO")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


    /**
     * CANCEL：取消 CONTROL：受控 CURRENT：有效 DRAFT：草稿 HISTORY：历史
     */
    @Schema(title = "CANCEL：取消 CONTROL：受控 CURRENT：有效 DRAFT：草稿 HISTORY：历史")
    @TableField("STATUS")
    private String status  = StringUtils.EMPTY;


    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @TableField("CHECK_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    @TableField("CHECK_RESULT_DESC")
    private String checkResultDesc = StringUtils.EMPTY;


}