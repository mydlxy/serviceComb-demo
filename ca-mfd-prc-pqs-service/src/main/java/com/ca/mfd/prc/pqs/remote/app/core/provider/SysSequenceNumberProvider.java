package com.ca.mfd.prc.pqs.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.ISysSequenceNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author edwards.qu
 */
@Service
public class SysSequenceNumberProvider {

    @Autowired
    private ISysSequenceNumberService sysSequenceNumberService;

    public String getSeqNumWithTransaction(String seqType) {
        ResultVO<String> result = sysSequenceNumberService.getSeqNumWithTransaction(seqType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }

}