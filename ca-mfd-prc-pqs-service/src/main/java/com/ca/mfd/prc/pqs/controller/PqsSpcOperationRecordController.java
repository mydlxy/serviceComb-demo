package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.dto.ControlChartsConstantsDto;
import com.ca.mfd.prc.pqs.entity.PqsSpcOperationRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsSpcOperationRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.convolution.Convolution;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.BooleanIndexing;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.indexing.conditions.Conditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: SPC模块_操作记录表Controller
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@RestController
@RequestMapping("pqsspcoperationrecord")
@Tag(name = "SPC模块_操作记录表服务", description = "SPC模块_操作记录表")
public class PqsSpcOperationRecordController extends BaseController<PqsSpcOperationRecordEntity> {

    private IPqsSpcOperationRecordService pqsSpcOperationRecordService;

    @Autowired
    public PqsSpcOperationRecordController(IPqsSpcOperationRecordService pqsSpcOperationRecordService) {
        this.crudService = pqsSpcOperationRecordService;
        this.pqsSpcOperationRecordService = pqsSpcOperationRecordService;
    }



}