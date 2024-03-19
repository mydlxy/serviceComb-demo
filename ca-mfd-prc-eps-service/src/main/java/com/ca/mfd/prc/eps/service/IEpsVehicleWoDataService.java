package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.EpsVehicleWoDataTrcInfo;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 工艺数据
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsVehicleWoDataService extends ICrudService<EpsVehicleWoDataEntity> {


    /**
     * 查询
     *
     * @param conditions
     * @param sorts
     * @param i
     * @param i1
     * @return
     */
    PageData<EpsVehicleWoDataEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int i, int i1);

    /**
     * 获取自定义采集数据
     *
     * @param vehicleWoDataId
     * @return
     */
    List<EpsVehicleEqumentDataEntity> getCurrentData(Long vehicleWoDataId);

    /**
     * 获取
     *
     * @param sn
     * @param decviceName
     * @return
     */
    EpsVehicleWoDataEntity getBySnAndDecviceName(String sn, String decviceName);

    /**
     * 获取
     *
     * @param epsVehicleWoId
     * @return
     */
    List<EpsVehicleWoDataEntity> getByEpsVehicleWoId(Long epsVehicleWoId);

    /**
     * 分页信息（获取追溯）
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return Page<EpsVehicleWoDataTrcInfo>
     */
    PageData<EpsVehicleWoDataTrcInfo> getPageTrcVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize);

    /**
     * 导出
     *
     * */
    void exportTrcVehicleDatas(Map<String, String> fieldParam, List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException;
}