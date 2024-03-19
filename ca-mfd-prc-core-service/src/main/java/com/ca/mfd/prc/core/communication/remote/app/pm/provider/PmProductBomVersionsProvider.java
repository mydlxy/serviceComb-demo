package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmProductBomVersionsService;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductBomVersionsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmProductBomVersionsProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmProductBomVersionsProvider {

    @Autowired
    private IPmProductBomVersionsService pmProductBomVersionsService;

    public PmProductBomVersionsEntity getById(String id) {
        ResultVO<PmProductBomVersionsEntity> result = pmProductBomVersionsService.getById(id);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<PmProductBomEntity> getBomData(String productMaterialNo, String bomVersions) {
        ResultVO<List<PmProductBomEntity>> result = pmProductBomVersionsService.getBomData(productMaterialNo, bomVersions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String getBomVersions(String productMaterialNo) {
        ResultVO<String> result = pmProductBomVersionsService.getBomVersions(productMaterialNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public PmProductBomVersionsEntity getByProductMaterialNoBomVerson(String productMaterialNo, String bomVersions) {
        ResultVO<PmProductBomVersionsEntity> result = pmProductBomVersionsService.getByProductMaterialNoBomVerson(productMaterialNo, bomVersions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }


    public String maintainBom(MaintainBomDTO bomData) {
        ResultVO<String> result = pmProductBomVersionsService.maintainBom(bomData);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }
}