package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentConfigEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author inkelink
 * @Description: 追溯设备工艺配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsVehicleEqumentConfigService extends ICrudService<EpsVehicleEqumentConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsVehicleEqumentConfigEntity> getAllDatas();

    /**
     * 导出eswich地址
     *
     * @param conditions
     * @param sorts
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exprotEswitch(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException;
}