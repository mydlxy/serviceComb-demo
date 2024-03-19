package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;
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
public class QgGateConfigurationInfo {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id;

    /**
     * 顺序号
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

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
     * 型号
     */
    private String modelCodes;

    /**
     * 备注
     */
    private String remark;

    /**
     * 工位列表
     */
    private String workstationCodes;

    /**
     * 色块集合
     */
    private List<PqsQualityGateBlankEntity> gateBlanks;
}
