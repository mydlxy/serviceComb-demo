package com.ca.mfd.prc.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @author inkelink
 * @Description: AUDIT组件代码实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AUDIT组件代码")
@TableName("PRC_PQS_AUDIT_DEFECT_COMPONENT")
public class PqsAuditDefectComponentEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_AUDIT_DEFECT_COMPONENT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 缺陷组件组
     */
    @Schema(title = "缺陷组件组")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;


    /**
     * 缺陷子组件组
     */
    @Schema(title = "缺陷子组件组")
    @TableField("SUB_GROUP_NAME")
    private String subGroupName = StringUtils.EMPTY;


    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    @TableField("DEFECT_COMPONENT_CODE")
    private String defectComponentCode = StringUtils.EMPTY;


    /**
     * 缺陷组件名称
     */
    @Schema(title = "缺陷组件名称")
    @TableField("DEFECT_COMPONENT_DESCRIPTION")
    private String defectComponentDescription = StringUtils.EMPTY;


    /**
     * 拼音简码
     */
    @Schema(title = "拼音简码")
    @TableField("SHORT_CODE")
    private String shortCode = StringUtils.EMPTY;


    /**
     * 数据来源
     */
    @Schema(title = "数据来源")
    @TableField("SOURCE")
    private Integer source = 0;


}