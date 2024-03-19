package com.ca.mfd.prc.eps.communication.remote.app.pm;

import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * IPmProductMaterialMasterService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmproductmaterialmaster", contextId = "inkelink-pm-midpmproductmaterialmaster")
public interface IPmProductMaterialMasterService {

    @PostMapping(value = "getdata")
    @Operation(summary = "获取所有数据")
    ResultVO<List<PmProductMaterialMasterEntity>> getdata(@RequestBody DataDto model);

    @PostMapping(value = "getpagedata")
    @Operation(summary = "获取分页数据")
    ResultVO<PageData<PmProductMaterialMasterEntity>> getPageData(@RequestBody PageDataDto model);
}
