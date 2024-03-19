package com.ca.mfd.prc.pqs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description: 物料信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialInfo {

    /**
     * 物料号
     */
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料中文
     */
    private String materialCn = StringUtils.EMPTY;

    /**
     * 物料英文
     */
    private String materialEn = StringUtils.EMPTY;
}
