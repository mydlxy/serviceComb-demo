package com.ca.mfd.prc.pqs.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class ShowQgMatrikDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    private String itemName = StringUtils.EMPTY;

    /**
     * 代码
     */
    private String itemCode = StringUtils.EMPTY;

    /**
     * 行
     */
    private Integer rowCount = 0;

    /**
     * 列
     */
    private Integer columnCount = 0;

    /**
     * 图片
     */
    private String image = StringUtils.EMPTY;

    /**
     * 备注
     */
    private String remark = StringUtils.EMPTY;

    /**
     * 缺陷列表
     */
    private List<DefectAnomalyDto> defectAnomalyList = Lists.newArrayList();
}
