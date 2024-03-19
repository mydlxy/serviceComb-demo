package com.ca.mfd.prc.pqs.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.ISysSnConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    /**
     * 调用服务inkelink-core-syssnconfig
     *
     * @param category
     * @param para
     * @return
     */
    public String createSnBypara(String category, Map<String, String> para) {
        ResultVO<String> result = sysSnConfigService.createSnBypara(category, para);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
