package com.ca.mfd.prc.pqs.entity;

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
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 电池等离子清洗效果检测实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池等离子清洗效果检测")
@TableName("PRC_PQS_INSPECT_ONLINE_SAMPLING_RECORDS")
public class PqsInspectOnlineSamplingRecordsEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_ONLINE_SAMPLING_RECORDS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("SAMPLING_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date samplingDate;


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("WORKSHOP_SHIFT")
    private String workshopShift = StringUtils.EMPTY;


    /**
     * 抽件序号
     */
    @Schema(title = "抽件序号")
    @TableField("SAMPLE_NUMBER")
    private Integer sampleNumber = 0;


    /**
     * 抽检结果（≥54）
     */
    @Schema(title = "抽检结果（≥54）")
    @TableField("SAMPLING_RESULT")
    private String samplingResult = StringUtils.EMPTY;


    /**
     * 抽检人
     */
    @Schema(title = "抽检人")
    @TableField("SAMPLING_PERSON")
    private String samplingPerson = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}