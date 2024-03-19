package com.ca.mfd.prc.pqs.remote.app.otweb.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.otweb.IOtWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class OtWebProvider {

    @Autowired
    private IOtWebService otWebService;

    /**
     * 调用服务ca-mfd-mom-gateway
     *
     * @param woId
     * @param status
     * @return
     */
    public void systemSaveWoStatus(Long woId, Integer status) {
        ResultVO result = otWebService.systemSaveWoStatus(woId, status);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务ca-mfd-mom-gateway调用失败" + result.getMessage());
        }
    }

}
