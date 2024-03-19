package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 监控-尺寸监控要求实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "监控-尺寸监控要求")
@TableName("PRC_PQS_INSPECT_DIME_REQU")
public class PqsInspectDimeRequEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_DIME_REQU_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 类别
     */
    @Schema(title = "类别")
    @TableField("TYPE")
    private String type = StringUtils.EMPTY;


    /**
     * 零件名称
     */
    @Schema(title = "零件名称")
    @TableField("MATERINAL_NAME")
    private String materinalName = StringUtils.EMPTY;


    /**
     * 零件件号
     */
    @Schema(title = "零件件号")
    @TableField("MATERINAL_NO")
    private String materinalNo = StringUtils.EMPTY;


    /**
     * 供应商
     */
    @Schema(title = "供应商")
    @TableField("SUPPLIER")
    private String supplier = StringUtils.EMPTY;


    /**
     * 数据类型
     */
    @Schema(title = "数据类型")
    @TableField("DATA_TYPE")
    private String dataType = StringUtils.EMPTY;


    /**
     * 测量方式
     */
    @Schema(title = "测量方式")
    @TableField("TESTING_MODE")
    private String testingMode = StringUtils.EMPTY;


}