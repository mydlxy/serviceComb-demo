package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class QgMatrikConfigurationInfo {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id;

    /**
     * 代码
     */
    private String itemCode;

    /**
     * 名称
     */
    private String itemName;

    /**
     * 图片
     */
    private String image;

    /**
     * 行
     */
    private Integer rowCount;

    /**
     * 列
     */
    private Integer columnCount;

    /**
     * 型号
     */
    private String modelCodes;

    /**
     * 备注
     */
    private String remark;

    /**
     * 顺序号
     */
    private Integer displayNo;

    /**
     * 工位列表
     */
    private String workstationCodes;

    /**
     * 缺陷列表
     */
    private List<DefectAnomalyDto> defectAnomalyList;
}
