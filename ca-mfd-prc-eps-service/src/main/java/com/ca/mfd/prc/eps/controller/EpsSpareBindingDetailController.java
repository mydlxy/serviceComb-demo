package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsSpareBindingDetailEntity;
import com.ca.mfd.prc.eps.service.IEpsSpareBindingDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 备件绑定明细Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epssparebindingdetail")
@Tag(name = "备件绑定明细服务", description = "备件绑定明细")
public class EpsSpareBindingDetailController extends BaseController<EpsSpareBindingDetailEntity> {

    private final IEpsSpareBindingDetailService epsSpareBindingDetailService;

    @Autowired
    public EpsSpareBindingDetailController(IEpsSpareBindingDetailService epsSpareBindingDetailService) {
        this.crudService = epsSpareBindingDetailService;
        this.epsSpareBindingDetailService = epsSpareBindingDetailService;
    }

    /**
     * 备件过点 查询虚拟VIN号
     *
     * @param id 主键
     * @return 虚拟VIN号集合
     */
    @GetMapping("/provider/getpartvirtualvinbyparttrackid")
    @Operation(summary = "备件过点 查询虚拟VIN号")
    public ResultVO<List<String>> getPartVirtualVinByPartTrackId(String id) {
        List<String> sns = epsSpareBindingDetailService.getPartVirtualVinByPartTrackId(id);
        return new ResultVO<List<String>>().ok(sns, "成功");
    }

}