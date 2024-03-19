package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.core.communication.remote.app.pm.entity.PmProductCharacteristicsVersionsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmProductCharacteristicsVersionsProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmProductCharacteristicsVersionsProvider {

    @Autowired
    private IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;

    public PmProductCharacteristicsVersionsEntity getByMaterialNoVersions(String productMaterialNo, String versions) {
        ResultVO<PmProductCharacteristicsVersionsEntity> result = pmProductCharacteristicsVersionsService.getByMaterialNoVersions(productMaterialNo, versions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<PmProductCharacteristicsEntity> getCharacteristicsData(String productMaterialNo, String versions) {
        ResultVO<List<PmProductCharacteristicsEntity>> result = pmProductCharacteristicsVersionsService.getCharacteristicsData(productMaterialNo, versions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String maintainCharacteristics(MaintainCharacteristicsInfo data) {
        ResultVO<String> result = pmProductCharacteristicsVersionsService.maintainCharacteristics(data);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public PmProductCharacteristicsVersionsEntity getByCharacteristicsVersions(String characteristicsVersions) {
        ResultVO<PmProductCharacteristicsVersionsEntity> result = pmProductCharacteristicsVersionsService.getByCharacteristicsVersions(characteristicsVersions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String getCharacteristicsVersions(String productMaterialNo) {
        ResultVO<String> result = pmProductCharacteristicsVersionsService.getCharacteristicsVersions(productMaterialNo);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductbomversions调用失败" + result.getMessage());
        }
        return result.getData();
    }

}