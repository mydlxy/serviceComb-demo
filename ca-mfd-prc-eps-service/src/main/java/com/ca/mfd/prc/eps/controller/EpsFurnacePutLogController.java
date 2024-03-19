package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.MaterialPutPara;
import com.ca.mfd.prc.eps.entity.EpsFurnacePutLogEntity;
import com.ca.mfd.prc.eps.service.IEpsFurnacePutLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 熔化炉投料记录Controller
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@RestController
@RequestMapping("epsfurnaceputlog")
@Tag(name = "熔化炉投料记录服务", description = "熔化炉投料记录")
public class EpsFurnacePutLogController extends BaseController<EpsFurnacePutLogEntity> {

    private IEpsFurnacePutLogService epsFurnacePutLogService;

    @Autowired
    public EpsFurnacePutLogController(IEpsFurnacePutLogService epsFurnacePutLogService) {
        this.crudService = epsFurnacePutLogService;
        this.epsFurnacePutLogService = epsFurnacePutLogService;
    }

    @Operation(summary = "后台上传工艺数据")
    @PostMapping("putmaterial")
    public ResultVO<String> putMaterial(@RequestBody MaterialPutPara request) {
        epsFurnacePutLogService.putMaterial(request);
        epsFurnacePutLogService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}