package com.ca.mfd.prc.avi.communication.remote.app.eps;

import com.ca.mfd.prc.avi.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCarInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudCheckDto;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.CarCloudDeviceDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author mason
 * @Description: 车云相关接口
 * @date 2023年12月27日
 */
@FeignClient(name = "ca-mfd-prc-eps-service", path = "communication", contextId = "inkelink-eps-carcloud-communication")
public interface IEpsCarCloudCommunicationService {


    /**
     * 获取整车数据
     *
     * @param vinCode
     * @return
     */
    @GetMapping(value = "/midcarcloudcarinfo/provider/carcloudcarinfosend")
    ResultVO<CarCloudCarInfoDto> carCloudCarInfoSend(@RequestParam("vinCode") String vinCode);

    /**
     * 获取设备数据
     *
     * @param vinCode
     * @return
     */
    @GetMapping(value = "/midcarclouddevice/provider/carclouddevicesend")
    ResultVO<CarCloudDeviceDto> carCloudDeviceSend(@RequestParam("vinCode") String vinCode);

    /**
     * 获取终检数据
     *
     * @param vinCode
     * @return
     */
    @GetMapping(value = "/midcarcloudcheck/provider/carcloudchecksend")
    ResultVO<CarCloudCheckDto> carCloudCheckSend(@RequestParam("vinCode") String vinCode);


    /**
     * 获取整车数据-手动触发
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/midcarcloudcarinfo/provider/carcloudcarinfosendtest")
    ResultVO<CarCloudCarInfoDto> carCloudCarInfoSendTest(@RequestBody CheyunTestDto dto);

    /**
     * 获取设备数据-手动触发
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/midcarclouddevice/provider/carclouddevicesendtest")
    ResultVO<CarCloudDeviceDto> carCloudDeviceSendTest(@RequestBody CheyunTestDto dto);

    /**
     * 获取终检数据-手动触发
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/midcarcloudcheck/provider/carcloudchecksendtest")
    ResultVO<CarCloudCheckDto> carCloudCheckSendTest(@RequestBody CheyunTestDto dto);


}
