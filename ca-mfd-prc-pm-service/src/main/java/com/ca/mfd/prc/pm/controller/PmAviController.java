package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.service.IPmAviService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author inkelink
 * @Description: AVI站点
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmavi")
@Tag(name = "AVI站点")
public class PmAviController extends PmBaseController<PmAviEntity> {

    private final IPmAviService pmAviService;

    private final IPmVersionService pmVersionService;

    @Autowired
    public PmAviController(IPmAviService pmAviService,
                           IPmVersionService pmVersionService) {
        this.crudService = pmAviService;
        this.pmAviService = pmAviService;
        this.pmVersionService = pmVersionService;
    }

    /**
     * @return AVI点列表
     */
    @GetMapping(value = "/provider/getaviinfos")
    @Operation(summary = "AVI点列表")
    public ResultVO getAviInfos() {
        return new ResultVO().ok(pmAviService.getAviInfos());
    }

    @Operation(summary = "获取当前AVI")
    @PostMapping(value = "getcurrentavi")
    public ResultVO getCurrentAvi() {
        ResultVO<List<AviInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        List<PmAviEntity> pmAviEntityList = this.pmVersionService.getObjectedPm().getAvis();
        if (pmAviEntityList.isEmpty()) {
            return result.ok(Collections.emptyList());
        }
        List<AviInfoDTO> resultData = new ArrayList<>(pmAviEntityList.size());
        for (PmAviEntity pmAviEntity : pmAviEntityList) {
            AviInfoDTO aviInfoDTO = new AviInfoDTO();
            aviInfoDTO.setPmAviId(pmAviEntity.getId());
            aviInfoDTO.setPmAviCode(pmAviEntity.getAviCode());
            aviInfoDTO.setPmAviName(pmAviEntity.getAviName());
            resultData.add(aviInfoDTO);
        }
        return result.ok(resultData);
    }

}