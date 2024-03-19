package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.pm.entity.PmPfmeaEntity;
import com.ca.mfd.prc.pm.service.IPmPfmeaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @Description: PFMEA配置Controller
 * @author inkelink
 * @date 2023年11月22日
 * @变更说明 BY inkelink At 2023年11月22日
 */
@RestController
@RequestMapping("pmpfmea")
@Tag(name = "PFMEA配置服务", description = "PFMEA配置")
public class PmPfmeaController extends PmBaseController<PmPfmeaEntity> {

    private IPmPfmeaService pmPfmeaService;

    @Autowired
    public PmPfmeaController(IPmPfmeaService pmPfmeaService) {
        this.crudService = pmPfmeaService;
        this.pmPfmeaService = pmPfmeaService;
    }

}