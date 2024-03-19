package com.ca.mfd.prc.rc.rcavi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 滑橇实体
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "滑橇")
@TableName("PRC_RC_AVI_SKID")
public class RcAviSkidEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_SKID_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 滑橇代码
     */
    @Schema(title = "滑橇代码")
    @TableField("SKID_CODE")
    private String skidCode = StringUtils.EMPTY;


    /**
     * 滑橇类型
     */
    @Schema(title = "滑橇类型")
    @TableField("SKID_TYPE")
    private Integer skidType = 0;


    /**
     * 使用次数
     */
    @Schema(title = "使用次数")
    @TableField("USE_QTY")
    private Integer useQty = 0;


    /**
     * 是否清洗
     */
    @Schema(title = "是否清洗")
    @TableField("IS_CLEAN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isClean = false;


}