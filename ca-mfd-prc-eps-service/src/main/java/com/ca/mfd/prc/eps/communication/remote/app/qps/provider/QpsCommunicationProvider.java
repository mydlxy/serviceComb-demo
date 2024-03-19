package com.ca.mfd.prc.eps.communication.remote.app.qps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoDto;
import com.ca.mfd.prc.eps.communication.remote.app.qps.IQpsCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用法规
 *
 * @author inkelink mason
 * @since 1.0.0 2024-01-05
 */
@Service("MidQpsCommunicationProvider")
public class QpsCommunicationProvider {

    @Autowired
    private IQpsCommunicationService qpsCommunicationService;

    /**
     * 整车数据接口(法规相关字段赋值)
     *
     * @param dto
     * @return
     */
    public CarCloudCarInfoDto carInfoSupplement(CarCloudCarInfoDto dto) {
        ResultVO<CarCloudCarInfoDto> result = qpsCommunicationService.carInfoSupplement(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-qps-midcarcloudcarinfo调用失败" + result.getMessage());
        }
        return result.getData();
    }
}