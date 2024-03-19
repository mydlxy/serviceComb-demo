package com.ca.mfd.prc.audit.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

/**
 * @author inkelink
 * @Description: 工厂实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工厂")
@TableName("PRC_PM_ORGANIZATION")
public class PmOrganizationEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_ORGANIZATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("ORGANIZATION_CODE")
    @NotBlank(message = "工厂编码不能为空")
    private String organizationCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ORGANIZATION_NAME")
    @NotBlank(message = "工厂名称不能为空")
    private String organizationName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 地址
     */
    @Schema(title = "地址")
    @TableField("ADDRESS")
    private String address = StringUtils.EMPTY;


}