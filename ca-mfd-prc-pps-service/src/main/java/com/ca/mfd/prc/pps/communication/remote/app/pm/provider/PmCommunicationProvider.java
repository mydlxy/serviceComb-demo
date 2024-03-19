package com.ca.mfd.prc.pps.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.remote.app.pm.IPmCommunicationService;
import com.ca.mfd.prc.pps.communication.remote.app.pm.entity.MidColorBaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * PmOrgProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmCommunicationProvider {

    @Autowired
    private IPmCommunicationService pmCommunicationService;

    /**
     * 根据整车物料号获取bom版本号
     *
     * @return
     */
    public String getBomVersion(String plantCode,String materialNo,String specifyDate) {
        ResultVO<String> result = pmCommunicationService.getBomVersion(plantCode,materialNo,specifyDate);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据整车物料号获取特征版本号
     *
     * @return
     */
    public String getCharacteristicsVersion(String materialNo) {
        ResultVO<String> result = pmCommunicationService.getCharacteristicsVersion(materialNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据零件物料号获取零件bom版本号
     *
     * @return
     */
    public String getBomPartVersion(String materialNo, String plantCode,String specifyDate) {
        ResultVO<String> result = pmCommunicationService.getBomPartVersion(materialNo,plantCode,specifyDate);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取颜色
     *
     * @return
     */
    public List<MidColorBaseEntity> getByClorCode(String colorCode) {
        ResultVO<List<MidColorBaseEntity>> result = pmCommunicationService.getByClorCode(colorCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-communication调用失败" + result.getMessage());
        }
        return result.getData();
    }

}