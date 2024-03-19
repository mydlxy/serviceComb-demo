package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviDefectAnomalyAviEntity;
import com.ca.mfd.prc.avi.service.IAviDefectAnomalyAviService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: AVI缺陷阻塞配置[作废]Controller
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@RestController
@RequestMapping("avidefectanomalyavi")
@Tag(name = "AVI缺陷阻塞配置服务", description = "AVI缺陷阻塞配置")
public class AviDefectAnomalyAviController extends BaseController<AviDefectAnomalyAviEntity> {

    private final IAviDefectAnomalyAviService aviDefectAnomalyAviService;

    @Autowired
    public AviDefectAnomalyAviController(IAviDefectAnomalyAviService aviDefectAnomalyAviService) {
        this.crudService = aviDefectAnomalyAviService;
        this.aviDefectAnomalyAviService = aviDefectAnomalyAviService;
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviDefectAnomalyAviEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<AviDefectAnomalyAviEntity> page = crudService.page(model);
        if (page != null && page.getDatas() != null && page.getDatas().size() > 0) {
            for (AviDefectAnomalyAviEntity item : page.getDatas()) {
                if (StringUtils.equals(item.getPqsDefectAnomalyCode(), "111111")) {
                    item.setIsRisk(Boolean.TRUE);
                }
            }
        }
        page.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        return new ResultVO<PageData<AviDefectAnomalyAviEntity>>().ok(page, "获取数据成功");
    }

    @GetMapping(value = "getbyid", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "根据ID获取AVI缺陷阻塞配置")
    @Override
    public ResultVO<AviDefectAnomalyAviEntity> getById(@RequestParam(value = "id") String id) {
        AviDefectAnomalyAviEntity data = (AviDefectAnomalyAviEntity) crudService.get(ConvertUtils.stringToLong(id));
        if (data != null && StringUtils.equals(data.getPqsDefectAnomalyCode(), "111111")) {
            data.setIsRisk(Boolean.TRUE);
        }
        return new ResultVO<AviDefectAnomalyAviEntity>().ok(data);
    }
}