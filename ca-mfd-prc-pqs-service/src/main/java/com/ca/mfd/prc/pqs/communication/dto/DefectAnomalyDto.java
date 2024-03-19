package com.ca.mfd.prc.pqs.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 组合缺陷库实体
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "组合缺陷库")
public class DefectAnomalyDto {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 组合代码
     */
    @Schema(title = "组合代码")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    private String responsibleDeptCode = StringUtils.EMPTY;


    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    private String responsibleDeptName = StringUtils.EMPTY;


    /**
     * 缺陷等级代码
     */
    @Schema(title = "缺陷等级代码")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    private String componentDescription = StringUtils.EMPTY;


    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    private String defectCodeCode = StringUtils.EMPTY;


    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    private String defectCodeDescription = StringUtils.EMPTY;


    /**
     * 位置代码
     */
    @Schema(title = "位置代码")
    private String positionCode = StringUtils.EMPTY;


    /**
     * 位置描述
     */
    @Schema(title = "位置描述")
    private String positionDescription = StringUtils.EMPTY;


    /**
     * 数据来源;0:MES 其他：其他系统
     */
    @Schema(title = "数据来源;0:MES 其他：其他系统")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;


    /**
     * 拼音简码
     */
    @Schema(title = "拼音简码")
    private String shortCode = StringUtils.EMPTY;
}