package com.ca.mfd.prc.avi.communication.remote.app.eps.provider;

import com.ca.mfd.prc.avi.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCarCloudCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCarInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCheckDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudDeviceDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车云下发相关接口
 *
 * @author inkelink mason
 * @since 1.0.0 2024-01-04
 */
@Service
public class EpsCarCloudCommunicationProvider {

    @Autowired
    private IEpsCarCloudCommunicationService epsCarCloudCommunicationService;

    /**
     * 获取整车数据
     * @param vinCode
     * @return
     */
    public CarCloudCarInfoDto carCloudCarInfoSend(String vinCode) {
        ResultVO<CarCloudCarInfoDto> result = epsCarCloudCommunicationService.carCloudCarInfoSend(vinCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取设备数据
     * @param vinCode
     * @return
     */
    public CarCloudDeviceDto carCloudDeviceSend(String vinCode) {
        ResultVO<CarCloudDeviceDto> result = epsCarCloudCommunicationService.carCloudDeviceSend(vinCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取终检数据
     * @param vinCode
     * @return
     */
    public CarCloudCheckDto carCloudCheckSend(String vinCode) {
        ResultVO<CarCloudCheckDto> result = epsCarCloudCommunicationService.carCloudCheckSend(vinCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取整车数据-手动触发
     * @param dto
     * @return
     */
    public CarCloudCarInfoDto carCloudCarInfoSendTest(CheyunTestDto dto) {
        ResultVO<CarCloudCarInfoDto> result = epsCarCloudCommunicationService.carCloudCarInfoSendTest(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取设备数据-手动触发
     * @param dto
     * @return
     */
    public CarCloudDeviceDto carCloudDeviceSendTest(CheyunTestDto dto) {
        ResultVO<CarCloudDeviceDto> result = epsCarCloudCommunicationService.carCloudDeviceSendTest(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取终检数据-手动触发
     * @param dto
     * @return
     */
    public CarCloudCheckDto carCloudCheckSendTest(CheyunTestDto dto) {
        ResultVO<CarCloudCheckDto> result = epsCarCloudCommunicationService.carCloudCheckSendTest(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-carcloud-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }
}