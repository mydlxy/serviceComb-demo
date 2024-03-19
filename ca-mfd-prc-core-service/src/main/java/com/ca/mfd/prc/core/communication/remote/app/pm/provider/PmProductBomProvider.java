package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.TopDataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmProductBomService;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductBomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmProductBomProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmProductBomProvider {

    @Autowired
    private IPmProductBomService pmProductBomService;

    public List<PmProductBomEntity> getTopDatas(TopDataDto model) {
        ResultVO<List<PmProductBomEntity>> result = pmProductBomService.getTopDatas(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbom调用失败" + result.getMessage());
        }
        return result.getData();
    }
}