package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataItemEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleWoDataItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 模组工艺数据项Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epsmodulewodataitem")
@Tag(name = "模组工艺数据项服务", description = "模组工艺数据项")
public class EpsModuleWoDataItemController extends BaseController<EpsModuleWoDataItemEntity> {

    private IEpsModuleWoDataItemService epsModuleWoDataItemService;

    @Autowired
    public EpsModuleWoDataItemController(IEpsModuleWoDataItemService epsModuleWoDataItemService) {
        this.crudService = epsModuleWoDataItemService;
        this.epsModuleWoDataItemService = epsModuleWoDataItemService;
    }

}