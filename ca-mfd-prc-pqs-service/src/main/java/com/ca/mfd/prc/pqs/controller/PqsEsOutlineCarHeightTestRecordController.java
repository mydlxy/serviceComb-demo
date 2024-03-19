package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsOutlineCarHeightTestRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsOutlineCarHeightTestRecordService;
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
 * @Description: 外廓车高测试记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesoutlinecarheighttestrecord")
@Tag(name = "外廓车高测试记录服务", description = "外廓车高测试记录")
public class PqsEsOutlineCarHeightTestRecordController extends BaseWithDefValController<PqsEsOutlineCarHeightTestRecordEntity> {

    private IPqsEsOutlineCarHeightTestRecordService pqsEsOutlineCarHeightTestRecordService;

    @Autowired
    public PqsEsOutlineCarHeightTestRecordController(IPqsEsOutlineCarHeightTestRecordService pqsEsOutlineCarHeightTestRecordService) {
        this.crudService = pqsEsOutlineCarHeightTestRecordService;
        this.pqsEsOutlineCarHeightTestRecordService = pqsEsOutlineCarHeightTestRecordService;
    }

}