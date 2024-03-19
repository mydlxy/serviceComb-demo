package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsWorkplaceConfigEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.service.IEpsWorkplaceConfigService;
import com.google.common.base.Strings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 开工检查项配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsworkplaceconfig")
@Tag(name = "开工检查项配置")
public class EpsWorkplaceConfigController extends BaseController<EpsWorkplaceConfigEntity> {

    private final IEpsWorkplaceConfigService epsWorkplaceConfigService;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    public EpsWorkplaceConfigController(IEpsWorkplaceConfigService epsWorkplaceConfigService) {
        this.crudService = epsWorkplaceConfigService;
        this.epsWorkplaceConfigService = epsWorkplaceConfigService;
    }


    @Operation(summary = "获取岗位检测列表")
    @GetMapping("getchecklist")
    public ResultVO getCheckList(String id, String detection) {

        PmWorkStationEntity workstationInfo = pmVersionProvider.getObjectedPm().getStations().stream().filter(c -> Objects.equals(c.getId(), ConvertUtils.stringToLong(id)))
                .findFirst().orElse(null);
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto(MpSqlUtils.getColumnName(EpsWorkplaceConfigEntity::getWorkstationCode), workstationInfo.getWorkstationCode(), ConditionOper.Equal));

        List<SortDto> sortInfos = new ArrayList<>();
        sortInfos.add(new SortDto(MpSqlUtils.getColumnName(EpsWorkplaceConfigEntity::getLastUpdateDate), ConditionDirection.DESC));
        List<EpsWorkplaceConfigEntity> data = epsWorkplaceConfigService.getData(conditionDtos, sortInfos, false);
        if (!Strings.isNullOrEmpty(detection)) {
            data = data.stream().filter(s -> s.getDetectionName().contains(detection)).collect(Collectors.toList());
        }
        return new ResultVO<List<EpsWorkplaceConfigEntity>>().ok(data, "获取成功");
    }


    @Operation(summary = "确认检测项")
    @PostMapping("setconfirmlist")
    public ResultVO setConfirmList(@RequestBody List<EpsWorkplaceConfigEntity> list) {
        ResultVO result = new ResultVO<>();
        if (list.size() > 0) {
            result.setCode(ErrorCode.SUCCESS);
            result.setMessage("确认成功");
            epsWorkplaceConfigService.setConfirmList(list);
        } else {
            result.setCode(-1);
            result.setMessage("列表为空");
        }
        return result;
    }
}