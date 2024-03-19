package com.ca.mfd.prc.pm.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class MidBomPartInfo {

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
     * 颜色件
     */
    @Schema(title = "颜色件")
    private String colorPartCode = StringUtils.EMPTY;


    /**
     * 颜色代码
     */
    @Schema(title = "颜色代码")
    private String colorCode = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();


    /**
     * 关重件特性
     */
    @Schema(title = "关重件特性")
    private String imptPartsSpecial = StringUtils.EMPTY;

    /**
     * 零部件型号
     */
    @Schema(title = "零部件型号")
    private String partModel = StringUtils.EMPTY;

    /**
     * 3C件类型
     */
    @Schema(title = "3C件类型")
    private String threeCPartType = StringUtils.EMPTY;

    /**
     * 环保件
     */
    @Schema(title = "环保件")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean envirProtectionPart = false;
    /**
     * 环保件
     */
    @Schema(title = "是否公告件")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isAnnouncement = false;



    @Schema(title = "证书编号")
    private String certificateNumber = StringUtils.EMPTY;



    @Schema(title = "认证工厂代码")
    private String certfFactoryCode = StringUtils.EMPTY;



    @Schema(title = "生产企业名称")
    private String lifeEnterpriseName = StringUtils.EMPTY;


}