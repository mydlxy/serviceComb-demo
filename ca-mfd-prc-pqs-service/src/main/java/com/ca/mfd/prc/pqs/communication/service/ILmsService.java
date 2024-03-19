package com.ca.mfd.prc.pqs.communication.service;

import com.ca.mfd.prc.pqs.communication.dto.LmsSupplierDto;
import java.util.List;

/**
 * @author inkelink
 * @Description: 调用Lms服务
 * @date 2024年02月19日
 */
public interface ILmsService {

    /**
     * 根据物料编码查询供应商信息
     * @param materialCode
     * @return
     */
    List<LmsSupplierDto> querysupplrelbymaterialcode(String materialCode);
}