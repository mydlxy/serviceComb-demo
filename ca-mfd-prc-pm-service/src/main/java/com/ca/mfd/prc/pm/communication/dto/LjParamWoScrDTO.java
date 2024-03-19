package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName LJParamWoDTO
 * @description: 拧紧所需工艺参数具体项值
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamWoScrDTO {
    /**
     * 顺序号
     */
    private String channelID = StringUtils.EMPTY;
    /**
     * pset程序号
     */
    private String psetID = StringUtils.EMPTY;

    /**
     * pset程序号
     */
    private String jobId = StringUtils.EMPTY;
    /**
     * 批次数
     */
    private String batchSize = StringUtils.EMPTY;
    /**
     * 扭力上限
     */
    private String torqueMax = StringUtils.EMPTY;
    /**
     * 扭力下限
     */
    private String torqueMin = StringUtils.EMPTY;
    /**
     * 目标扭矩
     */
    private String torqueTarget = StringUtils.EMPTY;
    /**
     * 角度上限
     */
    private String angleMax = StringUtils.EMPTY;
    /**
     * 角度下限
     */
    private String angleMin = StringUtils.EMPTY;
    /**
     * 目标角度
     */
    private String angleTarget = StringUtils.EMPTY;
    /**
     * 旋转方向
     */
    private String direction = StringUtils.EMPTY;
}
