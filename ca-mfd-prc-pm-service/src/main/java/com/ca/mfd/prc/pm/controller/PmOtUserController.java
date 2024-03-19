package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmOtUserEntity;
import com.ca.mfd.prc.pm.service.IPmOtUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 操作终端可操作用户
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmotuser")
@Tag(name = "操作终端可操作用户")
public class PmOtUserController extends BaseController<PmOtUserEntity> {

    private final IPmOtUserService pmOtUserService;

    @Autowired
    public PmOtUserController(IPmOtUserService pmOtUserService) {
        this.crudService = pmOtUserService;
        this.pmOtUserService = pmOtUserService;
    }

    @Operation(summary = "获取所有数据")
    @GetMapping("/provider/getalldatas")
    public ResultVO<List<PmOtUserEntity>> getAllDatas() {
        return new ResultVO<List<PmOtUserEntity>>().ok(pmOtUserService.getAllDatas());
    }

    public ResultVO<Object> check(String workStationName, String userName) {
        boolean otUser = pmOtUserService.getWorkStationByUser(workStationName, userName);
        ResultVO result = new ResultVO();
        return otUser ? result.ok(null, "成功") : result.error(-1, "失败");
    }

}