package com.ca.mfd.prc.pps.communication.dto;

import cn.hutool.db.DaoTemplate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author inkelink
 * @Description: Lms整车计划锁定
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年10月28日
 */

@Data
@Schema(description = "Lms整车计划锁定")
public class LmsLockPlanDto {

    /**
     * 工厂代码
     */
    @Schema(title = "工厂代码")
    private String orgCode;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    private String workshopCode;

    /**
     * 产线编码
     */
    @Schema(title = "产线编码")
    private String lineCode;

    /**
     * VIN码
     */
    @Schema(title = "VIN码")
    private String vin;

    /**
     * 产品类型
     */
    @Schema(title = "产品类型")
    private String productType;

    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    private String productCode;

    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    private String planNo;

    /**
     * 唯一码
     */
    private String oneId;
    /**
     * 管理号
     */
    private String manager;

    /**
     * 过点时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date passTime;

    /**
     * 采集点
     */
    private String aviCode;

    /**
     * 唯一码ID
     */
    private Long uniqueCode;

    /**
     * 分类内部使用
     */
    private String orderCategory;
}
