package com.ca.mfd.prc.pps.remote.app.core.sys.entity;

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
 * @author inkelink ${email}
 * @Description: 国际化
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "国际化")
@TableName("PRC_SYS_LOCALIZATION")
public class SysLocalizationEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_LOCALIZATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 内容
     */
    @Schema(title = "内容")
    @TableField("CN")
    private String cn = StringUtils.EMPTY;

    /**
     * 值
     */
    @Schema(title = "值")
    @TableField("EN")
    private String en = StringUtils.EMPTY;

    /**
     * 语言
     */
    @Schema(title = "语言")
    @TableField("LANG")
    private String lang = StringUtils.EMPTY;

}
