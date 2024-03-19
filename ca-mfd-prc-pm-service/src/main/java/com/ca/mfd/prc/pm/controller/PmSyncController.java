package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmSyncEntity;
import com.ca.mfd.prc.pm.service.IPmSyncService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 数据同步缓存表Controller
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@RestController
@RequestMapping("pmsync")
@Tag(name = "数据同步缓存表服务", description = "数据同步缓存表")
public class PmSyncController extends BaseController<PmSyncEntity> {

    private final IPmSyncService pmSyncService;

    @Autowired
    public PmSyncController(IPmSyncService pmSyncService) {
        this.crudService = pmSyncService;
        this.pmSyncService = pmSyncService;
    }

}