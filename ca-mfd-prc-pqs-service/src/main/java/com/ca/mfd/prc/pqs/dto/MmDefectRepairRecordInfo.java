package com.ca.mfd.prc.pqs.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 批次件返修记录请求对象
 * @author inkelink
 * @date 2023年11月10日
 * @变更说明 BY inkelink At 2023年11月10日
 */
@Data
public class MmDefectRepairRecordInfo {

    /**
     * 返修单号
     */
    private String repairNo = StringUtils.EMPTY;


    /**
     * 订单类型：批次件： 5：冲压  6：电池上盖
     */
    private Integer orderCategory = 0;


    /**
     * 计划单号
     */
    private String planNo = StringUtils.EMPTY;


    /**
     * 批次号
     */
    private String lotNo = StringUtils.EMPTY;


    /**
     * 物料编码
     */
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    private String materialCn = StringUtils.EMPTY;


    /**
     * 合格数量
     */
    private Integer acceptQty = 0;


    /**
     * 报废数量
     */
    private Integer scrapQty = 0;


    /**
     * 维修耗时
     */
    private BigDecimal workHours = BigDecimal.valueOf(0);


    /**
     * 维修人
     */
    private String repairPerson = StringUtils.EMPTY;


    /**
     * 修复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date repairDt = new Date();


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}