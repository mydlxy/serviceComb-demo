package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 业务编码配规则配置
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "业务编码配规则配置")
@TableName("PRC_SYS_SEQUENCE_NUMBER")
public class SysSequenceNumberEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SEQUENCE_NUMBER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 流水号类型
     */
    @Schema(title = "流水号类型")
    @TableField("SEQUENCE_TYPE")
    private String sequenceType = StringUtils.EMPTY;

    /**
     * 流水号长度
     */
    @Schema(title = "流水号长度")
    @TableField("SEQUENCENUMBER_LEN")
    private Integer sequencenumberLen = 0;

    /**
     * 归一类型
     */
    @Schema(title = "归一类型")
    @TableField("RESET_TYPE")
    private String resetType = StringUtils.EMPTY;

    /**
     * 流水号各部分分隔符
     */
    @Schema(title = "流水号各部分分隔符")
    @TableField("`SEPARATOR`")
    private String separator = StringUtils.EMPTY;

    /**
     * 前缀
     */
    @Schema(title = "前缀")
    @TableField("PREFIX")
    private String prefix = StringUtils.EMPTY;

    /**
     * 日期模板年份 可以是:yyyy,yy,
     */
    @Schema(title = "日期模板年份可以是:yyyy,yy,")
    @TableField("PART_YEAR_FMT")
    private String partYearFmt = StringUtils.EMPTY;

    /**
     * 日期模板月份 可以是:mm,m等等
     */
    @Schema(title = "日期模板月份可以是:mm,m等等")
    @TableField("PART_MONTH_FMT")
    private String partMonthFmt = StringUtils.EMPTY;

    /**
     * 日期模板日期 可以是:dd,d等等
     */
    @Schema(title = "日期模板日期可以是:dd,d等等")
    @TableField("PART_DAY_FMT")
    private String partDayFmt = StringUtils.EMPTY;

    /**
     * 中缀
     */
    @Schema(title = "中缀")
    @TableField("MIDFIX")
    private String midfix = StringUtils.EMPTY;

    /**
     * 当前最大流水号值
     */
    @Schema(title = "当前最大流水号值")
    @TableField("CUR_MAX_SEQUENCE_NUMBER")
    private Integer curMaxSequenceNumber = 0;

    /**
     * 当前最大日期
     */
    @Schema(title = "当前最大日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("CUR_MAX_DATE")
    private Date curMaxDate;

    /**
     * 后缀
     */
    @Schema(title = "后缀")
    @TableField("SUFFIX")
    private String suffix = StringUtils.EMPTY;

    /**
     * 最小值
     */
    @Schema(title = "最小值")
    @TableField("MIN_VALUE")
    private Integer minValue = 0;

    /**
     * 最大值
     */
    @Schema(title = "最大值")
    @TableField("MAX_VALUE")
    private Integer maxValue = 0;

    /**
     * 取值规则
     */
    @Schema(title = "取值规则")
    @TableField("RULES_VALUE")
    private String rulesValue = StringUtils.EMPTY;

}
