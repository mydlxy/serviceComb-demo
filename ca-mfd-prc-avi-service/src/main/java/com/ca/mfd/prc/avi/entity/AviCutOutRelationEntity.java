package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: AVI切出关联AVI实体
 * @date 2024年02月26日
 * @变更说明 BY inkelink At 2024年02月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AVI切出关联AVI")
@TableName("PRC_AVI_CUT_OUT_RELATION")
public class AviCutOutRelationEntity extends BaseEntity {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_CUT_OUT_RELATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 主线AVI_CODE
     */
    @Schema(title = "主线AVI_CODE")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;


    /**
     * 主线AVI_NAME
     */
    @Schema(title = "主线AVI_NAME")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;


    /**
     * 关联AVIid
     */
    @Schema(title = "关联AVIid")
    @TableField("AVI_SUBLINE_AVI_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long aviSublineAviId = Constant.DEFAULT_ID;


    /**
     * 关联AviCode
     */
    @Schema(title = "关联AviCode")
    @TableField("AVI_SUBLINE_AVI_CODE")
    private String aviSublineAviCode = StringUtils.EMPTY;


    /**
     * 关联AviName
     */
    @Schema(title = "关联AviName")
    @TableField("AVI_SUBLINE_AVI_NAME")
    private String aviSublineAviName = StringUtils.EMPTY;
}
