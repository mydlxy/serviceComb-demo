package com.ca.mfd.prc.pm.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class MidBomInfo {

    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 父级物料编码
     */
    @Schema(title = "父级物料编码")
    private String parentMaterialCode = StringUtils.EMPTY;


    /**
     * 用量
     */
    @Schema(title = "用量")
    private String quantity = "0";


    /**
     * 制造供货状态
     */
    @Schema(title = "制造供货状态")
    private String manufacturingSupplyStatus = StringUtils.EMPTY;


    /**
     * 使用工厂
     */
    @Schema(title = "使用工厂")
    private String usePlant = StringUtils.EMPTY;


    /**
     * 使用工艺类型
     */
    @Schema(title = "使用工艺类型")
    private String useProcessType = StringUtils.EMPTY;


    /**
     * 制造工厂
     */
    @Schema(title = "制造工厂")
    private String manufacturingPlant = StringUtils.EMPTY;


    /**
     * 制造工艺类型
     */
    @Schema(title = "制造工艺类型")
    private String manufacturingProcessType = StringUtils.EMPTY;


    /**
     * 替换组
     */
    @Schema(title = "替换组")
    private String replaceGroup = StringUtils.EMPTY;


    /**
     * 配套组
     */
    @Schema(title = "配套组")
    private String supportGroup = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom;


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo;


    /**
     * 合件分组号
     */
    @Schema(title = "合件分组号")
    private String componentGroupNumber = StringUtils.EMPTY;

    /**
     * bom行号
     */
    @Schema(title = "bom行号")
    private String lineNumber = StringUtils.EMPTY;

    /**
     * 使用过程类型编码
     */
    @Schema(title = "使用过程类型编码")
    private String useProcessTypeCode = StringUtils.EMPTY;
    /**
     * 生成过程类型编码
     */
    @Schema(title = "生成过程类型编码")
    private String manufacturingProcessTypeCode = StringUtils.EMPTY;

    /**
     * ecuTypeCode
     */
    @Schema(title = "ecuTypeCode")
    private String ecuTypeCode = StringUtils.EMPTY;






}