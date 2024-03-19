package com.ca.mfd.prc.core.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pps.IPpsProductProcessService;
import com.ca.mfd.prc.core.communication.remote.app.pps.entity.PpsProductProcessEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PpsProductProcessProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PpsProductProcessProvider {

    @Autowired
    private IPpsProductProcessService ppsProductProcessService;

    /**
     * 获取
     *
     * @return
     */
    public List<PpsProductProcessEntity> getAllDatas() {
        ResultVO<List<PpsProductProcessEntity>> result = ppsProductProcessService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsproductprocess调用失败" + result.getMessage());
        }
        return result.getData();
    }

}