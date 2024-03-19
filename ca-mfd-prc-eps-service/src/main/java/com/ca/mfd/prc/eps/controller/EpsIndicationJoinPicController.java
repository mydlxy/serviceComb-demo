package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsIndicationJoinPicEntity;
import com.ca.mfd.prc.eps.service.IEpsIndicationJoinPicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 作业指示关联图片Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsindicationjoinpic")
@Tag(name = "作业指示关联图片服务", description = "作业指示关联图片")
public class EpsIndicationJoinPicController extends BaseController<EpsIndicationJoinPicEntity> {


    @Autowired
    public EpsIndicationJoinPicController(IEpsIndicationJoinPicService epsIndicationJoinPicService) {
        this.crudService = epsIndicationJoinPicService;
    }

}