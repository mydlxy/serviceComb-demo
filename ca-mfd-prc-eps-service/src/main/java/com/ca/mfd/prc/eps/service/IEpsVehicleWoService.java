package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;

import java.util.List;

/**
 * 车辆操作信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsVehicleWoService extends ICrudService<EpsVehicleWoEntity> {

    /**
     * 生成工单工艺
     *
     * @param barCode
     * @param shopCode
     * @return
     */
    void createEntryWo(String barCode, String shopCode);

    /**
     * 查询
     *
     * @param conditions
     * @param sorts
     * @param i
     * @param i1
     * @return
     */
    PageData<EpsVehicleWoEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int i, int i1);

    /**
     * 根据sn查询工艺
     *
     * @param sn
     * @return
     */
    List<EpsVehicleWoEntity> getBySn(String sn);
}