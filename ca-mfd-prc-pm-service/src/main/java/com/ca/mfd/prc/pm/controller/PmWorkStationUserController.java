package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmWorkStationUserEntity;
import com.ca.mfd.prc.pm.service.IPmWorkStationUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 岗位工艺关联人员
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmworkstationuser")
@Tag(name = "岗位工艺关联人员")
public class PmWorkStationUserController extends BaseController<PmWorkStationUserEntity> {

    private final IPmWorkStationUserService pmWorkplaceUserService;

    @Autowired
    public PmWorkStationUserController(IPmWorkStationUserService pmWorkplaceUserService) {
        this.crudService = pmWorkplaceUserService;
        this.pmWorkplaceUserService = pmWorkplaceUserService;
    }

}