package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.pqs.entity.PqsQualityGateBlankEntity;
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
public class ShowQgGateDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 图片
     */
    private String image;

    /**
     * 色块集合
     */
    private List<PqsQualityGateBlankEntity> gateBlanks;
}
