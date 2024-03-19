package com.ca.mfd.prc.pm.communication.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阳波
 * @ClassName LJParamWoDTO
 * @description: 拧紧所需工艺参数
 * @date 2023年12月27日
 * @version: 1.0
 */
@Data
public class LjParamWoDTO{
    /**
     *工位编号
     */
   private String stationCode = StringUtils.EMPTY;
    /**
     * 工具编码
     */
   private String toolId =  StringUtils.EMPTY;

    /**
     * job号
     */
    private Integer jobId = 0;

    /**
     * job名
     */
    private String jobName =  StringUtils.EMPTY;
    /**
     * 计费模式：0=OK/1=ALL
     */
    private String jobBatchMode;
    /**
     * 作业结束是否锁枪，0=No, 1=Yes
     */
    private String lockdone;
    /**
     * 重复循环，0=No, 1=Yes
     */
    private String repeatJob;

    private List<LjParamWoScrDTO> jobList = new ArrayList<>();






}
