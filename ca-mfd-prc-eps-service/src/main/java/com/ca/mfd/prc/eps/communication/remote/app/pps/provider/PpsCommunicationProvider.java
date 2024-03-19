package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.communication.remote.app.pps.IPpsCommunicationService;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.BomConfigDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.SoftwareBomListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmOrgProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsCommunicationProvider")
public class PpsCommunicationProvider {

    @Autowired
    private IPpsCommunicationService ppsCommunicationService;

    /**
     * 根据整车物料号获取单车配置字
     *
     * @return
     */
    public List<BomConfigDto> getBomConfig(String materialNo,String effectiveDate) {
        ResultVO<List<BomConfigDto>> result = ppsCommunicationService.getBomConfig(materialNo,effectiveDate);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据整车物料号获取软件清单
     *
     * @return
     */
    public List<SoftwareBomListDto> getSoftBom(String materialNo,String effectiveDate) {
        ResultVO<List<SoftwareBomListDto>> result = ppsCommunicationService.getSoftBom(materialNo,effectiveDate);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }



}