package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsHkskLightCheckDataEntity;
import com.ca.mfd.prc.eps.service.IEpsHkskLightCheckDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 海克斯康光学检验数据Controller
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@RestController
@RequestMapping("epshksklightcheckdata")
@Tag(name = "海克斯康光学检验数据服务", description = "海克斯康光学检验数据")
public class EpsHkskLightCheckDataController extends BaseController<EpsHkskLightCheckDataEntity> {

    private IEpsHkskLightCheckDataService epsHkskLightCheckDataService;

    @Autowired
    public EpsHkskLightCheckDataController(IEpsHkskLightCheckDataService epsHkskLightCheckDataService) {
        this.crudService = epsHkskLightCheckDataService;
        this.epsHkskLightCheckDataService = epsHkskLightCheckDataService;
    }

}