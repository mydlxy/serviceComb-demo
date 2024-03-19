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
 * @Description: AUDIT缺陷代码实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AUDIT缺陷代码")
@TableName("PRC_PQS_AUDIT_DEFECT_CODE")
public class PqsAuditDefectCodeEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_AUDIT_DEFECT_CODE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 组
     */
    @Schema(title = "组")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;


    /**
     * 子组
     */
    @Schema(title = "子组")
    @TableField("SUB_GROUP_NAME")
    private String subGroupName = StringUtils.EMPTY;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("DEFECT_CODE_CODE")
    private String defectCodeCode = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("DEFECT_CODE_DESCRIPTION")
    private String defectCodeDescription = StringUtils.EMPTY;


    /**
     * 拼音简码
     */
    @Schema(title = "拼音简码")
    @TableField("SHORT_CODE")
    private String shortCode = StringUtils.EMPTY;


}