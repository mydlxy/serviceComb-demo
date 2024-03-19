package com.ca.mfd.prc.avi.remote.app.pqs.provider;

import com.ca.mfd.prc.avi.remote.app.pqs.IPqsdefectanomalyService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pqs.entity.PqsDefectAnomalyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PqsdefectanomalyProvider {
    @Autowired
    private IPqsdefectanomalyService pqsdefectanomalyService;

    /**
     * 根据缺陷代码查询缺陷
     *
     * @param code 缺陷编码
     * @return 缺陷实体
     */
    public ResultVO<PqsDefectAnomalyEntity> getEntityByCode(String code) {
        return pqsdefectanomalyService.getEntityByCode(code);
    }
}
