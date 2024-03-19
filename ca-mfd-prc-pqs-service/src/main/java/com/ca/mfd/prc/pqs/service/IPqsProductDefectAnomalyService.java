package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.communication.dto.ProductDefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.dto.UpdateDutyAreaInfo;
import com.ca.mfd.prc.pqs.dto.UpdateDutyTeamNoInfo;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 产品缺陷记录服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsProductDefectAnomalyService extends ICrudService<PqsProductDefectAnomalyEntity> {

    /**
     * 获取所有产品缺陷记录
     *
     * @return
     */
    List<PqsProductDefectAnomalyEntity> getAllDatas();

    /**
     * 获取已激活的缺陷ID列表
     *
     * @param tpsCode tps编码
     * @return 已激活的缺陷ID列表
     */
    List<String> getPqsProductDefectAnomalyList(String tpsCode);

    /**
     * 获取车辆所有缺陷列表
     *
     * @param tpsCode
     * @return
     */
    List<PqsProductDefectAnomalyEntity> getVehicleDefectAnomaly(String tpsCode);

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     */
    void updateVehicleDefectAnomalyDutyArea(UpdateDutyAreaInfo info);

    /**
     * 后台更新缺陷责任班组
     *
     * @param info
     */
    void updateVehicleDefectAnomalyDutyTeamNo(UpdateDutyTeamNoInfo info);

    /**
     * getPageVehicleDatas
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageData<PqsProductDefectAnomalyEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize);

    /**
     * 修改缺陷责任部门-等级
     *
     * @param info
     */
    void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info);


    /**
     * getPageDataEx
     *
     * @param model 分页数据请求参数
     * @return 分页数据
     */
    PageData<PqsProductDefectAnomalyEntity> getPageDataEx(PageDataDto model);

    /**
     * QMS获取产品缺陷数据
     *
     * @return
     */
    List<ProductDefectAnomalyDto> getProductDefectAnomalyList();
}