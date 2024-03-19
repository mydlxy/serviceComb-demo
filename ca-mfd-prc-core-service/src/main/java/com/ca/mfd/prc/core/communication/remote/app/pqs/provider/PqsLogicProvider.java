package com.ca.mfd.prc.core.communication.remote.app.pqs.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.dto.IccDto;
import com.ca.mfd.prc.core.communication.remote.app.pqs.IPqsLogicService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PqsLogicProvider {

    @Autowired
    private IPqsLogicService pqsLogicService;

    public ResultVO receiveIccData(@RequestBody List<IccDto> dtos){
        ResultVO result = pqsLogicService.receiveIccData(dtos);
        if (result==null||!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-receiveIccData调用失败" + result);
        }
        return result;
    }
    public List<IccDto> checkIccData(@RequestBody List<IccDto> dtos){
        ResultVO<List<IccDto>> result = pqsLogicService.checkIccData(dtos);
        if (result==null||!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-checkIccData调用失败" + result);
        }
        return result.getData();
    }

}