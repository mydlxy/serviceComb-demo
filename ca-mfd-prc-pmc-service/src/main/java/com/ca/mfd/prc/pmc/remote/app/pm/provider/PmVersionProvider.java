package com.ca.mfd.prc.pmc.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mason
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