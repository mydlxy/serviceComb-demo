package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceTimeLogEntity;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceTimeLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 岗位时间日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsworkplacetimelog")
@Tag(name = "岗位时间日志")
public class EpsWorkplaceTimeLogController extends BaseController<EpsWorkplaceTimeLogEntity> {

    private final IEpsWorkplaceTimeLogService epsWorkplaceTimeLogService;

    @Autowired
    public EpsWorkplaceTimeLogController(IEpsWorkplaceTimeLogService epsWorkplaceTimeLogService) {
        this.crudService = epsWorkplaceTimeLogService;
        this.epsWorkplaceTimeLogService = epsWorkplaceTimeLogService;
    }

    @GetMapping("/provider/getbysn")
    public ResultVO<List<EpsWorkplaceTimeLogEntity>> getBySn(String workstationCode, String sn) {
        ResultVO<List<EpsWorkplaceTimeLogEntity>> result = new ResultVO<>();
        List<EpsWorkplaceTimeLogEntity> list = epsWorkplaceTimeLogService.getBySn(workstationCode, sn);
        return result.ok(list);
    }


}