package com.ca.mfd.prc.pps.entity;

import com.ca.mfd.prc.pps.dto.ModuleStructDto;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: 电池结构配置实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池结构配置")
@TableName("PRC_PPS_MODULE_SPLIT_STRATEGY")
public class PpsModuleSplitStrategyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_MODULE_SPLIT_STRATEGY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 策略标记
     */
    @Schema(title = "策略标记")
    @TableField("STRATEGY_SIGN")
    private String strategySign = StringUtils.EMPTY;

    /**
     * 电池类型
     */
    @Schema(title = "电池类型")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;

    /**
     * 生产区域
     */
    @Schema(title = "生产区域")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;

    /**
     * 模组数量
     */
    @Schema(title = "模组数量")
    @TableField("MODULE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer moduleNum = 0;

    /**
     * 结果内容
     */
    @Schema(title = "结果内容")
    @TableField("STRUCT_CONTENT")
    private String structContent = StringUtils.EMPTY;

    /**
     * 策略描述
     */
    @Schema(title = "策略描述")
    @TableField("STRATEGY_DESCRIPTION")
    private String strategyDescription = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnable = false;

    /**
     * 模组类型数量
     */
    @Schema(title = "模组类型数量")
    @TableField("MODULE_TYPE_NUM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer moduleTypeNum = 0;

    /**
     * 模组
     */
    @Schema(title = "模组")
    @TableField(exist = false)
    private List<ModuleStructDto> modules = new ArrayList();

}