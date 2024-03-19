package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pmc.entity.PmcCameraImageEntity;
import com.ca.mfd.prc.pmc.service.IPmcCameraImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 摄像头抓拍图片;Controller
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("equality.mes.pmc/{version}/pmccameraimage")
@Tag(name = "摄像头抓拍图片;服务", description = "摄像头抓拍图片;")
public class PmcCameraImageController extends BaseController<PmcCameraImageEntity> {

    private IPmcCameraImageService pmcCameraImageService;

    @Autowired
    public PmcCameraImageController(IPmcCameraImageService pmcCameraImageService) {
        this.crudService = pmcCameraImageService;
        this.pmcCameraImageService = pmcCameraImageService;
    }

}