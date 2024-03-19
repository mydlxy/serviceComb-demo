package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PmOrgProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmOrgProvider {

    @Autowired
    private IPmOrgService pmOrgService;

    public String getCurrentOrgCode() {
        ResultVO<String> result = pmOrgService.getCurrentOrgCode();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmplant调用失败" + result.getMessage());
        }
        return result.getData();
    }
}