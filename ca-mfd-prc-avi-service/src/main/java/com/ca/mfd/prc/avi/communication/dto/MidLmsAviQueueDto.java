package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inkelink
 * @Description: Lms车辆队列实体
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@Schema(description = "Lms车辆过点AviQueue")
public class MidLmsAviQueueDto implements Serializable {
    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    private String orgCode = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 制造ID
     */
    @Schema(title = "vid")
    private String vid;

    /**
     * vin码
     */
    @Schema(title = "vin码")
    private String vin;

    /**
     * 产品类型 1-整车；2-自制件
     */
    @Schema(title = "1-整车；2-自制件")
    private String productType;

    /**
     * 产品编码(整车对应整车物料号、自制件成品对应成品物料号)
     */
    private String productCode;

    /**
     * 产品唯一编码
     */
    private String oneId;

    /**
     * 管理员
     */
    private String manager;

    /**
     * 过点时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passTime;

    /**
     * AVI站点
     */
    private String aviCode;

    /**
     * 定编
     */
    private String orderSign;

    /**
     * 唯一码
     */
    private Long uniqueCode;
}
