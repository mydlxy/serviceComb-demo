package com.ca.mfd.prc.pps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.IPmProductMaterialMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public PmProductMaterialMasterEntity getByMaterialNo(String materialNo) {
        ResultVO<PmProductMaterialMasterEntity> result = pmProductMaterialMasterService.getByMaterialNo(materialNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductmaterialmaster调用失败" + result.getMessage());
        }
        return result.getData();
    }
}