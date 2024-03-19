package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsHkskVisuallyCheckDataEntity;
import com.ca.mfd.prc.eps.service.IEpsHkskVisuallyCheckDataService;
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
 * @Description: 海克斯康视觉检测数据Controller
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@RestController
@RequestMapping("epshkskvisuallycheckdata")
@Tag(name = "海克斯康视觉检测数据服务", description = "海克斯康视觉检测数据")
public class EpsHkskVisuallyCheckDataController extends BaseController<EpsHkskVisuallyCheckDataEntity> {

    private IEpsHkskVisuallyCheckDataService epsHkskVisuallyCheckDataService;

    @Autowired
    public EpsHkskVisuallyCheckDataController(IEpsHkskVisuallyCheckDataService epsHkskVisuallyCheckDataService) {
        this.crudService = epsHkskVisuallyCheckDataService;
        this.epsHkskVisuallyCheckDataService = epsHkskVisuallyCheckDataService;
    }

}