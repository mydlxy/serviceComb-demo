package com.ca.mfd.prc.rc.rcps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 路由车道实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由车道")
@TableName("PRC_RC_PS_ROUTE_LANE")
public class RcPsRouteLaneEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_PS_ROUTE_LANE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由区")
    @TableField("PRC_RC_PS_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcPsRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("LANE_CODE")
    private Integer laneCode = 0;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("LANE_NAME")
    private String laneName = StringUtils.EMPTY;


    /**
     * 车道属性 1正常车道  2 返回道 3直通道 4维修车道
     */
    @Schema(title = "车道属性 1正常车道  2 返回道 3直通道 4维修车道")
    @TableField("LANE_TYPE")
    private Integer laneType = 0;


    /**
     * 最大容量
     */
    @Schema(title = "最大容量")
    @TableField("MAX_VALUE")
    private Integer maxValue = 0;


    /**
     * 计算最大层级
     */
    @Schema(title = "计算最大层级")
    @TableField("MAX_LEVEL")
    private Integer maxLevel = 0;


    /**
     * 入车控制
     */
    @Schema(title = "入车控制")
    @TableField("ALLOW_IN")
    private Boolean allowIn = false;


    /**
     * 出车控制
     */
    @Schema(title = "出车控制")
    @TableField("ALLOW_OUT")
    private Boolean allowOut = false;


    /**
     * 扩展字段
     */
    @Schema(title = "扩展字段")
    @TableField("EXT1")
    private String ext1 = StringUtils.EMPTY;


    /**
     * 扩展字段2
     */
    @Schema(title = "扩展字段2")
    @TableField("EXT2")
    private String ext2 = StringUtils.EMPTY;


    /**
     * 扩展字段3
     */
    @Schema(title = "扩展字段3")
    @TableField("EXT3")
    private String ext3 = StringUtils.EMPTY;


    /**
     * 入车按钮控制
     */
    @Schema(title = "入车按钮控制")
    @TableField("BUTTON_IN")
    private Boolean buttonIn = false;

    /**
     * 出车按钮控制
     */
    @Schema(title = "出车按钮控制")
    @TableField("BUTTON_OUT")
    private Boolean buttonOut = false;

    /**
     * 排序
     */
    @Schema(title = "排序")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;
}