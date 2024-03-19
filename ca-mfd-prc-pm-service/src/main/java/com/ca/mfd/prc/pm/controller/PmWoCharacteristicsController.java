package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmWoCharacteristicsEntity;
import com.ca.mfd.prc.pm.service.IPmWoCharacteristicsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @Description: 工艺特征数据Controller
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@RestController
@RequestMapping("pmwocharacteristics")
@Tag(name = "工艺特征数据服务", description = "工艺特征数据")
public class PmWoCharacteristicsController extends BaseController<PmWoCharacteristicsEntity> {

    private IPmWoCharacteristicsService pmWoCharacteristicsService;

    @Autowired
    public PmWoCharacteristicsController(IPmWoCharacteristicsService pmWoCharacteristicsService) {
        this.crudService = pmWoCharacteristicsService;
        this.pmWoCharacteristicsService = pmWoCharacteristicsService;
    }

}