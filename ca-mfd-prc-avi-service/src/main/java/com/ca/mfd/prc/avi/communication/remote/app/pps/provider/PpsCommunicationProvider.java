package com.ca.mfd.prc.avi.communication.remote.app.pps.provider;

import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsCommunicationService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * PmOrgProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PpsCommunicationProvider {

    @Autowired
    private IPpsCommunicationService ppsCommunicationService;

    /**
     * 根据vin号组装整车信息
     *
     * @return
     */
    public CarInfoDto getCarInfoByVin(String vin) {
        ResultVO<CarInfoDto> result = ppsCommunicationService.getCarInfoByVin(vin);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据vin号组装软件信信息
     *
     * @return
     */
    public EcuCarInfoDto getCharacteristicsVersion(String vin) {
        ResultVO<EcuCarInfoDto> result = ppsCommunicationService.getEcuCarInfoByVin(vin);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据vin号组装过点信息
     *
     * @return
     */
    public SiteInfoDto getBomPartVersion(String vin) {
        ResultVO<SiteInfoDto> result = ppsCommunicationService.getSiteInfoByVin(vin);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }


    /**
     * 组装软件信息-手动
     *
     * @return
     */
    public EcuCarInfoDto getEcuCarInfoByVinTest(MidDjTestSendEntity dto) {
        ResultVO<EcuCarInfoDto> result = ppsCommunicationService.getEcuCarInfoByVinTest(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 组装软件信息-手动
     *
     * @return
     */
    public CarInfoDto getCarInfoByVinTest(MidDjTestSendEntity dto) {
        ResultVO<CarInfoDto> result = ppsCommunicationService.getCarInfoByVinTest(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }


}