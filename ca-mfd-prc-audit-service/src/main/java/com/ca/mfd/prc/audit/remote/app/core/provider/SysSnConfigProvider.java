package com.ca.mfd.prc.audit.remote.app.core.provider;

import com.ca.mfd.prc.audit.remote.app.core.ISysSnConfigService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class SysSnConfigProvider {

    @Autowired
    private ISysSnConfigService sysSnConfigService;

    /**
     * 调用服务inkelink-core-syssnconfig
     *
     * @param materialNo
     * @param category
     * @return
     */
    public String createSn(String materialNo, String category) {
        ResultVO<String> result = sysSnConfigService.createSn(materialNo, category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String createSn(String category) {
        ResultVO<String> result = sysSnConfigService.createSn(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
