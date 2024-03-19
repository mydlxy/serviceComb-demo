package com.ca.mfd.prc.core.dc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.prm.entity.DcInstanceEntity;
import com.ca.mfd.prc.core.dc.service.IDcInstanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: DC配置实例Controller
 * @author inkelink
 * @date 2023年11月10日
 * @变更说明 BY inkelink At 2023年11月10日
 */
@RestController
@RequestMapping("dc/dcinstance")
@Tag(name = "DC配置实例服务", description = "DC配置实例")
public class DcInstanceController extends BaseController<DcInstanceEntity> {

    private IDcInstanceService dcInstanceService;

    @Autowired
    public DcInstanceController(IDcInstanceService dcInstanceService) {
        this.crudService = dcInstanceService;
        this.dcInstanceService = dcInstanceService;
    }

}