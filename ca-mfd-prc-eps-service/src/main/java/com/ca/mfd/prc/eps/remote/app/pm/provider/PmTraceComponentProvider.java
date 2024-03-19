package com.ca.mfd.prc.eps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.IPmTraceComponentService;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmTraceComponentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * PmTraceComponentProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmTraceComponentProvider {

    @Autowired
    private IPmTraceComponentService pmTraceComponentService;

    /**
     * 获取
     *
     * @return
     */
    public List<PmTraceComponentEntity> getDataCache() {
        ResultVO<List<PmTraceComponentEntity>> result = pmTraceComponentService.getDataCache();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmtracecomponent调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String saveByBom(String materialNo, String materialCn) {
        ResultVO<String> result = pmTraceComponentService.saveByBom(materialNo, materialCn);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmtracecomponent调用失败" + result.getMessage());
        }
        return result.getData();
    }

}