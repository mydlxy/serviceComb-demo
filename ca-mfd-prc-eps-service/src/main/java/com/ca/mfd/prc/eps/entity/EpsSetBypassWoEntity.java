package com.ca.mfd.prc.eps.entity;

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
 * @Description: 设置进工位BYPASS工艺实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "设置进工位BYPASS工艺")
@TableName("PRC_EPS_SET_BYPASS_WO")
public class EpsSetBypassWoEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_SET_BYPASS_WO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工艺名称
     */
    @Schema(title = "工艺名称")
    @TableField("WO_NAME")
    private String woName = StringUtils.EMPTY;


    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;


    /**
     * 对应的工位组
     */
    @Schema(title = "对应的工位组")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


}