package com.ca.mfd.prc.pqs.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pm.IPmVersionService;
import com.ca.mfd.prc.pqs.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pqs.remote.app.pm.entity.PmWorkStationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PmVersionProvider {

    @Autowired
    private IPmVersionService pmVersionService;

    public PmAllDTO getObjectedPm() {
        ResultVO<PmAllDTO> result = pmVersionService.getObjectedPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<String> getRelevanceQgWorkplaceByStationId(String stationId) {
        ResultVO<List<String>> result = pmVersionService.getRelevanceQgWorkplaceByStationId(stationId);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<PmWorkStationEntity> getrelevanceworkplacebystation(String workstationCode) {
        ResultVO<List<PmWorkStationEntity>> result = pmVersionService.getrelevanceworkplacebystation(workstationCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List getAllElements(Class className) {
        ResultVO<List> result = pmVersionService.getAllElements(className);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<String> getWorkplaceComboPqs(String shopCode, String workplaceType) {
        ResultVO<List<String>> result = pmVersionService.getWorkplaceComboPqs(shopCode, workplaceType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }
}