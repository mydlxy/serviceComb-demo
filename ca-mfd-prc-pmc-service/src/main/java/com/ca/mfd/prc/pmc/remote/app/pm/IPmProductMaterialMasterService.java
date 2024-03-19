package com.ca.mfd.prc.pmc.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmProductMaterialMasterEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author mason
 */
@FeignClient(
        name = "ca-mfd-prc-pm-service",
        path = "pmproductmaterialmaster",
        contextId = "inkelink-pm-pmproductmaterialmaster")
public interface IPmProductMaterialMasterService {

    /**
     * 获取所有的数据
     *
     * @return List<PmProductMaterialMasterEntity>
     */
    @GetMapping("/provider/getalldatas")
    ResultVO<List<PmProductMaterialMasterEntity>> getAllDatas();
}
