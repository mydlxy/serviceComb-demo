package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.service.IPmShcBreakService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 工厂排班
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("pmshcshift")
@Tag(name = "工厂排班")
public class PmShcShiftController extends BaseController<PmShcShiftEntity> {

    private final IPmShcShiftService pmShcShiftService;
    @Autowired
    private IPmShcBreakService pmShcBreakService;

    @Autowired
    public PmShcShiftController(IPmShcShiftService pmShcShiftService) {
        this.crudService = pmShcShiftService;
        this.pmShcShiftService = pmShcShiftService;
    }

    @Operation(summary = "根据ID获取数据")
    @GetMapping("getbyid")
    @Override
    public ResultVO getById(@RequestParam(value = "id") String id) {
        ResultVO result = new ResultVO<>();
        result.setMessage("获取数据成功");
        PmShcShiftEntity data = pmShcShiftService.get(id);
        data.setPmShcBreakInfos(pmShcBreakService.getPmShcBreakInfos(data.getId()));
        return result.ok(data);
    }

    @Operation(summary = "获取班次下的休息列表")
    @GetMapping("getpmshcbreak")
    public ResultVO getPmShcBreak(Long pmShcShiftId) {
        ResultVO result = new ResultVO<>();
        List<PmShcBreakEntity> data = pmShcBreakService.getPmShcBreakInfos(pmShcShiftId);
        return result.ok(data);
    }


    @Override
    @Operation(summary = "数据保存")
    @PostMapping("edit")
    public ResultVO edit(@RequestBody PmShcShiftEntity pmShcShiftInfo) {
        ResultVO result = new ResultVO<>();

        if (pmShcShiftInfo.getId() == null || pmShcShiftInfo.getId().equals(Constant.DEFAULT_ID)) {
            pmShcShiftService.insert(pmShcShiftInfo);
        } else {
            pmShcShiftService.update(pmShcShiftInfo);
        }
        List<PmShcBreakEntity> breakOldData = pmShcBreakService.getPmShcBreakInfos(pmShcShiftInfo.getId());
        if (breakOldData != null && !breakOldData.isEmpty()) {
            deletePmShcBreak(pmShcShiftInfo, breakOldData);
            /*if (pmShcShiftInfo.getPmShcBreakInfos() != null) {
                for (PmShcBreakEntity oldData : breakOldData) {
                    if (pmShcShiftInfo.getPmShcBreakInfos().stream().noneMatch(o -> o.getId().equals(oldData.getId()))) {
                        breakids.add(oldData.getId());
                    }
                }
                if (!breakids.isEmpty()) {
                    //  pmShcBreakService.delete(breakids.toArray(new String[breakids.size()]));
                    pmShcBreakService.delete(breakids.toArray(new Long[0]));
                }
            } else {
                List<Long> ids = breakOldData.stream().map(PmShcBreakEntity::getId).collect(Collectors.toList());
                // pmShcBreakService.delete(ids.toArray(new String[ids.size()]));
                pmShcBreakService.delete(ids.toArray(new Long[0]));
            }*/
        }
        if (pmShcShiftInfo.getPmShcBreakInfos() != null) {
            for (PmShcBreakEntity data : pmShcShiftInfo.getPmShcBreakInfos()) {
                if (data.getPrcPmShcShiftId() == null || data.getPrcPmShcShiftId().equals(Constant.DEFAULT_ID)) {
                    data.setPrcPmShcShiftId(pmShcShiftInfo.getId());
                }
                if (data.getId() == null || data.getId().equals(Constant.DEFAULT_ID)) {
                    data.setId(IdGenerator.getId());
                    pmShcBreakService.insert(data);
                } else {
                    pmShcBreakService.update(data);
                }
            }
        }
        pmShcShiftService.saveChange();
        return result.ok("", "操作成功");
    }

    private void deletePmShcBreak(PmShcShiftEntity pmShcShiftInfo, List<PmShcBreakEntity> breakOldData) {
        if (pmShcShiftInfo.getPmShcBreakInfos() != null) {
            List<Long> breakids = new ArrayList<>();
            for (PmShcBreakEntity oldData : breakOldData) {
                if (pmShcShiftInfo.getPmShcBreakInfos().stream().noneMatch(o -> o.getId().equals(oldData.getId()))) {
                    breakids.add(oldData.getId());
                }
            }
            if (!breakids.isEmpty()) {
                //  pmShcBreakService.delete(breakids.toArray(new String[breakids.size()]));
                pmShcBreakService.delete(breakids.toArray(new Long[0]));
            }
        } else {
            List<Long> ids = breakOldData.stream().map(PmShcBreakEntity::getId).collect(Collectors.toList());
            // pmShcBreakService.delete(ids.toArray(new String[ids.size()]));
            pmShcBreakService.delete(ids.toArray(new Long[0]));
        }
    }

}