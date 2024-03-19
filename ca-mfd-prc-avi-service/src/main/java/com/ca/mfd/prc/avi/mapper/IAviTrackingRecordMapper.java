package com.ca.mfd.prc.avi.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品过点信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Mapper
public interface IAviTrackingRecordMapper extends IBaseMapper<AviTrackingRecordEntity> {

    /**
     * 产品过点信息
     *
     * @param page
     * @param pms
     * @return Page<AviTrackingRecordEntity>
     */
    Page<AviTrackingRecordEntity> getVehiceOrderPageDatas(Page<AviTrackingRecordEntity> page, @Param("pms") Map<String, Object> pms);

    /**
     * 产品过点信息
     *
     * @param page
     * @param pms
     * @return Page<AviTrackingRecordEntity>
     */
    Page<AviTrackingRecordEntity> getAviTrackingRecordInfo(Page<AviTrackingRecordEntity> page, @Param("pms") Map<String, Object> pms);

    /**
     * 产品过点信息
     *
     * @param aviCode
     * @return Page<AviPassedRecordDTO>
     */
    List<AviPassedRecordDTO> getAviPassedRecord(String aviCode);
}