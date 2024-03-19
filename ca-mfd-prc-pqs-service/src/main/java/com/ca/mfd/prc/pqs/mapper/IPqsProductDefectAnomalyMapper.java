package com.ca.mfd.prc.pqs.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.communication.dto.ProductDefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.GetDefectAnomalyRequest;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyReponse;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 产品缺陷记录Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsProductDefectAnomalyMapper extends IBaseMapper<PqsProductDefectAnomalyEntity> {

    /**
     * 获取
     *
     * @param page
     * @param para
     * @return
     */
    Page<ProductDefectAnomalyReponse> getVehicleDefectAnomalyList(Page<ProductDefectAnomalyReponse> page, @Param("para") GetDefectAnomalyRequest para);

    /**
     * 获取
     *
     * @param page
     * @param pms
     * @return
     */
    Page<PqsProductDefectAnomalyEntity> getPageVehicleDatas(Page<PqsProductDefectAnomalyEntity> page, @Param("pms") Map<String, Object> pms);

    /**
     * QMS获取产品缺陷数据
     *
     * @return
     */
    List<ProductDefectAnomalyDto> getProductDefectAnomaly();
}