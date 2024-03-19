package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmProductMaterialMasterService;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductMaterialMasterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmProductMaterialMasterProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmProductMaterialMasterProvider {

    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;

    public List<PmProductMaterialMasterEntity> getAllDatas() {
        ResultVO<List<PmProductMaterialMasterEntity>> result = pmProductMaterialMasterService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductmaterialmaster调用失败" + result.getMessage());
        }
        return result.getData();
    }
}