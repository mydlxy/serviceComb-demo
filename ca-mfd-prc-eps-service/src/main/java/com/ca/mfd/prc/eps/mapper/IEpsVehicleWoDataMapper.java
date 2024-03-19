package com.ca.mfd.prc.eps.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.eps.dto.EpsVehicleWoDataTrcInfo;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工艺数据
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface IEpsVehicleWoDataMapper extends IBaseMapper<EpsVehicleWoDataEntity> {
    /**
     * 分页信息
     *
     * @param page
     * @param pms
     * @return Page<EpsVehicleWoEntity>
     */
    Page<EpsVehicleWoDataEntity> getPageVehicleDatas(Page<EpsVehicleWoDataEntity> page, @Param("pms") Map<String, Object> pms);


    /**
     * 分页信息
     *
     * @param dataTableName   PRC_EPS_VEHICLE_EQUMENT_DATA
     * @param vehicleWoDataId
     * @return List<EpsVehicleEqumentDataEntity>
     */
    List<EpsVehicleEqumentDataEntity> getCurrentData(String dataTableName, Long vehicleWoDataId);


    /**
     * 根据vin号查询工艺数据
     *
     * @param vin
     * @return
     */
    List<EpsVehicleWoDataEntity> getVehicleDataByVin(String vin);


    /**
     * 分页信息（获取追溯）
     *
     * @param page
     * @param pms
     * @return Page<EpsVehicleWoDataTrcInfo>
     */
    Page<EpsVehicleWoDataTrcInfo> getPageTrcVehicleDatas(Page<EpsVehicleWoDataTrcInfo> page, @Param("pms") Map<String, Object> pms);


}