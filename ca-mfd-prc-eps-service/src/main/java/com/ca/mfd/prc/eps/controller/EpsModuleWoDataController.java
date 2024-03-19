package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.CollectModuleWoPara;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleWoDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 模组工艺数据Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulewodata")
@Tag(name = "模组工艺数据服务", description = "模组工艺数据")
public class EpsModuleWoDataController extends BaseController<EpsModuleWoDataEntity> {

    private IEpsModuleWoDataService epsModuleWoDataService;

    @Autowired
    public EpsModuleWoDataController(IEpsModuleWoDataService epsModuleWoDataService) {
        this.crudService = epsModuleWoDataService;
        this.epsModuleWoDataService = epsModuleWoDataService;
    }

    @Operation(summary = "收集预成组检测数据")
    @PostMapping("collectmodulewo")
    public ResultVO<String> collectModuleWo(@RequestBody CollectModuleWoPara para) {
        epsModuleWoDataService.collectModuleWo(para);
        epsModuleWoDataService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "获取检测数据")
    @GetMapping("getmodulewobysn")
    public ResultVO<List<EpsModuleWoDataEntity>> GetModuleWoBySn(String sn) {
        List<EpsModuleWoDataEntity> data = epsModuleWoDataService.getModuleWoBySn(sn);
        return new ResultVO<List<EpsModuleWoDataEntity>>().ok(data, "操作成功");
    }

}