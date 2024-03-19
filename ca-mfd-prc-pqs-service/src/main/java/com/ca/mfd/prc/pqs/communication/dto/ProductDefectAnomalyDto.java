package com.ca.mfd.prc.pqs.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 产品缺陷记录DTO
 * @date 2024年02月19日
 * @变更说明
 */
@Data
@Schema(description = "产品缺陷记录DTO")
public class ProductDefectAnomalyDto {

    /**
     * 业务id
     */
    @Schema(title = "mom业务数据id")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcPqsProductDefectAnomalyId = Constant.DEFAULT_ID;

    /**
     * 产品唯一编码
     */
    @Schema(title = "产品唯一编码")
    private String sn = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    private String model = StringUtils.EMPTY;


    /**
     * 车间
     */
    @Schema(title = "车间")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    private String workshopName = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 缺陷等级代码(分类标准)
     */
    @Schema(title = "缺陷等级代码")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 缺陷等级描述(分类标准)
     */
    @Schema(title = "缺陷等级描述")
    private String gradeName = StringUtils.EMPTY;


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
     * 组合缺陷代码
     */
    @Schema(title = "组合缺陷代码")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 激活时间
     */
    @Schema(title = "激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date activateTime;
}