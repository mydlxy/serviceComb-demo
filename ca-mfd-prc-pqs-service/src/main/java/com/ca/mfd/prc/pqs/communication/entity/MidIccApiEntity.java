package com.ca.mfd.prc.pqs.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @Description: ICC接口中间表实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "ICC接口中间表")
@TableName("PRC_MID_ICC_API")
public class MidIccApiEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_ICC_API_ID", type = IdType.INPUT)
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
     * VRT代码
     */
    @Schema(title = "VRT代码")
    @TableField("ICC_VRT_CODE")
    private String iccVrtCode = StringUtils.EMPTY;


    /**
     * VRT名称
     */
    @Schema(title = "VRT名称")
    @TableField("ICC_VRT_NAME")
    private String iccVrtName = StringUtils.EMPTY;


    /**
     * VFG代码
     */
    @Schema(title = "VFG代码")
    @TableField("ICC_VFG_CODE")
    private String iccVfgCode = StringUtils.EMPTY;


    /**
     * VFG名称
     */
    @Schema(title = "VFG名称")
    @TableField("ICC_VFG_NAME")
    private String iccVfgName = StringUtils.EMPTY;


    /**
     * CCC代码
     */
    @Schema(title = "CCC代码")
    @TableField("ICC_CCC_CODE")
    private String iccCccCode = StringUtils.EMPTY;


    /**
     * CCC名称
     */
    @Schema(title = "CCC名称")
    @TableField("ICC_CCC_NAME")
    private String iccCccName = StringUtils.EMPTY;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @TableField("ICC_PARTS")
    private String iccParts = StringUtils.EMPTY;


    /**
     * 模式
     */
    @Schema(title = "模式")
    @TableField("ICC_MODEL")
    private String iccModel = StringUtils.EMPTY;


    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    @TableField("ICC_CODE")
    private String iccCode = StringUtils.EMPTY;


    /**
     * 故障名称
     */
    @Schema(title = "故障名称")
    @TableField("ICC_FAULT_NAME")
    private String iccFaultName = StringUtils.EMPTY;


    /**
     * 分类标准编码
     */
    @Schema(title = "分类标准编码")
    @TableField("ICC_TAXONOMY_CODE")
    private String iccTaxonomyCode = StringUtils.EMPTY;


    /**
     * 分类标准名称
     */
    @Schema(title = "分类标准名称")
    @TableField("ICC_TAXONOMY_NAME")
    private String iccTaxonomyName = StringUtils.EMPTY;


    /**
     * 等级
     */
    @Schema(title = "等级")
    @TableField("ICC_GRADE")
    private String iccGrade = StringUtils.EMPTY;


    /**
     * 分值
     */
    @Schema(title = "分值")
    @TableField("ICC_SCORE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer iccScore = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("ICC_REMARK")
    private String iccRemark = StringUtils.EMPTY;


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


    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @TableField("SUB_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @TableField("OP_CODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer opCode = 0;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
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
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date exeTime = new Date();

}