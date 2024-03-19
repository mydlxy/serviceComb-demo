package com.ca.mfd.prc.pm.entity;

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
 * @Description: LES拉取工位物料清单实体
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "LES拉取工位物料清单")
@TableName("PRC_PM_MATERIAL_TO_LES")
public class PmMaterialToLesEntity extends BaseEntity {

    /**
     *  主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_MATERIAL_TO_LES_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    @TableField("PRODUCT_CODE")
    private String productCode = StringUtils.EMPTY;

    /**
     * 是否删除
     */
    @Schema(title = "是否删除")
    @TableField("SIGTRUE_CREATE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean sigtrueCreate = false;

    /**
     * 加密字符串
     */
    @Schema(title = "加密字符串")
    @TableField("SIGTRUE")
    private String sigtrue = StringUtils.EMPTY;

    /**
     * 工位物料清单数据
     */
    @Schema(title = "工位物料清单数据")
    @TableField("MATERIAL_CONTETN")
    private String materialContetn = StringUtils.EMPTY;

    /**
     * 拉取时间
     */
    @Schema(title = "拉取时间")
    @TableField("PULL_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date pullTime = new Date();

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;
}