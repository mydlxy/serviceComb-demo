package com.ca.mfd.prc.eps.entity;

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

import java.util.Date;

/**
 *
 * @Description: 模组工艺数据项实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "模组工艺数据项")
@TableName("PRC_EPS_MODULE_WO_DATA_ITEM")
public class EpsModuleWoDataItemEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_WO_DATA_ITEM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 数据编号
     */
    @Schema(title = "数据编号")
    @TableField("PRC_EPS_MODULE_WO_DATA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcEpsModuleWoDataId = Constant.DEFAULT_ID;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 参数名称
     */
    @Schema(title = "参数名称")
    @TableField("WO_NAME")
    private String woName = StringUtils.EMPTY;

    /**
     * 参数编码
     */
    @Schema(title = "参数编码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 上限值
     */
    @Schema(title = "上限值")
    @TableField("WO_UPLIMIT")
    private String woUplimit = StringUtils.EMPTY;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    @TableField("WO_DOWNLIMIT")
    private String woDownlimit = StringUtils.EMPTY;

    /**
     * 标准值
     */
    @Schema(title = "标准值")
    @TableField("WO_STANDARD")
    private String woStandard = StringUtils.EMPTY;

    /**
     * 参数值
     */
    @Schema(title = "参数值")
    @TableField("WO_VALUE")
    private String woValue = StringUtils.EMPTY;

    /**
     * 参数结果
     */
    @Schema(title = "参数结果")
    @TableField("WO_RESULT")
    private String woResult = StringUtils.EMPTY;

    /**
     * 参数单位
     */
    @Schema(title = "参数单位")
    @TableField("WO_UNIT")
    private String woUnit = StringUtils.EMPTY;

}