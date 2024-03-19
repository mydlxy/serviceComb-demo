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
 * @Description: 唯一码配置
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "唯一码配置")
@TableName("PRC_SYS_SN_CONFIG")
public class SysSnConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SN_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 类别
     */
    @Schema(title = "类别")
    @TableField("CATEGORY")
    private String category = StringUtils.EMPTY;

    /**
     * 类型
     */
    @Schema(title = "类型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    /**
     * 长度
     */
    @Schema(title = "长度")
    @TableField("LENGTH")
    private Integer length = 0;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;

    /**
     * 参数1
     */
    @Schema(title = "参数1")
    @TableField("PARAM1")
    private String param1 = StringUtils.EMPTY;

    /**
     * 参数2
     */
    @Schema(title = "参数2")
    @TableField("PARAM2")
    private String param2 = StringUtils.EMPTY;

    /**
     * 参数3
     */
    @Schema(title = "参数3")
    @TableField("PARAM3")
    private String param3 = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
