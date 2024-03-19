package com.ca.mfd.prc.pmc.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmProductMaterialMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mason
 */
@Service
public class PmProductMaterialMasterProvider {

    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;

    public ResultVO getAllDatas() {
        ResultVO result = pmProductMaterialMasterService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductmaterialmaster调用失败" + result.getMessage());
        }
        return result;
    }
}
