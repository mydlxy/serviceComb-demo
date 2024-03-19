package com.ca.mfd.prc.avi.communication.remote.app.eps.provider;

import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 电检查EP数据
 *
 * @author inkelink mason
 * @since 1.0.0 2023-12-27
 */
@Service
public class EpsCommunicationProvider {

    @Autowired
    private IEpsCommunicationService epsCommunicationService;

    /**
     * 根据vin号组装电检EP信息
     *
     * @return
     */
    public List<EpInfoDto> getEpInfoByVin(String vin) {
        ResultVO<List<EpInfoDto>> result = epsCommunicationService.getEpInfoByVin(vin);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }
}