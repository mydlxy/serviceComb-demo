package com.ca.mfd.prc.pqs.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.dto.GetDefectAnomalyRequest;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyReponse;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author inkelink
 * @Description: 零部件缺陷记录Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsMmDefectAnomalyMapper extends IBaseMapper<PqsMmDefectAnomalyEntity> {

    /**
     * 获取
     *
     * @param page
     * @param para
     * @return
     */
    Page<ProductDefectAnomalyReponse> getVehicleMmDefectAnomalyList(Page<ProductDefectAnomalyReponse> page, @Param("para") GetDefectAnomalyRequest para);

}