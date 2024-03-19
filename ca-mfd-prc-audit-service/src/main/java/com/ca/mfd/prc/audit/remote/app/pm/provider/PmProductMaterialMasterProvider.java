package com.ca.mfd.prc.audit.remote.app.pm.provider;

import com.ca.mfd.prc.audit.remote.app.pm.IPmProductMaterialMasterService;
import com.ca.mfd.prc.audit.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
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
