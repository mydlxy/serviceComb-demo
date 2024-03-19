package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * AnomalyActivity
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "AnomalyActivity")
public class AnomalyDto {
    /**
     * 来源 0 正常缺陷录入 1百格图缺陷录入 2 检查项图片
     */
    @Schema(title = "来源")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;
    /**
     * 缺陷ID
     */
    @Schema(title = "缺陷ID")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long anomalyId = Constant.DEFAULT_ID;
    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long woId = Constant.DEFAULT_ID;
    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    private String gradeCode = StringUtils.EMPTY;
    /**
     * 责任区域
     */
    @Schema(title = "责任区域")
    private String responsibleDeptCode = StringUtils.EMPTY;
    /**
     * 缺陷代码 组件-方位-代码
     */
    @Schema(title = "缺陷代码 组件-方位-代码")
    private String defectAnomalyCode = StringUtils.EMPTY;
    /**
     * 描述 组件-方位-代码
     */
    @Schema(title = "描述 组件-方位-代码")
    private String defectAnomalyDescription = StringUtils.EMPTY;
    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer level = 0;
    /**
     * 责任区域
     */
    @Schema(title = "责任区域")
    private String dutyArea = StringUtils.EMPTY;
    /**
     * 图片
     */
    @Schema(title = "图片")
    private String img = StringUtils.EMPTY;
    /**
     * 缺陷代码 组件-方位-代码
     */
    @Schema(title = "缺陷代码")
    private String code = StringUtils.EMPTY;
    /**
     * 描述 组件-方位-代码
     */
    @Schema(title = "描述")
    private String description = StringUtils.EMPTY;
    /**
     * json数据
     */
    @Schema(title = "json数据")
    private String jsonData = StringUtils.EMPTY;

}
