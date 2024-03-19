package com.ca.mfd.prc.avi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.dto.AviOperDto;
import com.ca.mfd.prc.avi.dto.AviQueryDto;
import com.ca.mfd.prc.avi.entity.AviOperEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmAviServiceProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviOperService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordOperService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 关键点行为配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avioper")
@Tag(name = "关键点行为配置")
public class AviOperController extends BaseController<AviOperEntity> {

    private final IAviOperService aviOperService;

    @Autowired
    private PmAviServiceProvider pmAviServiceProvider;

    @Autowired
    PmVersionProvider pmVersionProvider;
    @Autowired
    SysConfigurationProvider configurationProvider;
    @Autowired
    PpsOrderProvider ppsOrderProvider;

    @Autowired
    IAviTrackingRecordOperService aviTrackingRecordOperService;

    @Autowired
    public AviOperController(IAviOperService aviOperService) {
        this.crudService = aviOperService;
        this.aviOperService = aviOperService;
    }

    @Operation(summary = "获取AVI站点")
    @PostMapping("getaviinfos")
    public ResultVO<List<AviInfoDTO>> getAviInfos() {
        ResultVO<List<AviInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取AVI站点成功");
        List<AviInfoDTO> avisData = pmAviServiceProvider.getAviInfos();
        return result.ok(avisData);
    }

    @Operation(summary = "后台手动获取需要执行的行为")
    @PostMapping("/getAviOperInfo")
    public ResultVO<List<AviOperDto>> getAviOperInfo(@RequestBody AviQueryDto aviQueryDto) {
        PpsOrderEntity orderInfo = ppsOrderProvider.getPpsOrderInfo(aviQueryDto.getSn());
        ResultVO<List<AviOperDto>> result = new ResultVO<>();
        result.setCode(-1);
        result.setMessage("操作失败");
        if (orderInfo == null) {
            result.setCode(-1);
            result.setMessage("产品信息不存在");
            return result;
        }

        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis()
                .stream().filter(s -> s.getAviCode().equals(aviQueryDto.getAviCode()))
                .findFirst().orElse(null);

        if (aviInfo == null) {
            result.setCode(-1);
            result.setMessage("站点信息不存在");
            return result;
        }

        try {
            List<ComboInfoDTO> comboInfoDTOS = configurationProvider.getComboDatas("aviOperation");
            comboInfoDTOS.add(new ComboInfoDTO("订单状态更改", "ChangeEntryStatus"));
            comboInfoDTOS.add(new ComboInfoDTO("生成过点队列", "OrderQueueRelease"));
            List<AviOperEntity> aviOperEntityList = this.aviOperService.getAviOperEntityList(aviQueryDto.getAviType(), aviQueryDto.getAviCode());
            List<AviOperDto> aviOperDtoList = new ArrayList<>();
            for (AviOperEntity oper : aviOperEntityList) {
                String actionName = comboInfoDTOS.stream()
                        .filter(b -> b.getValue().equals(oper.getAction()))
                        .findFirst()
                        .map(ComboInfoDTO::getText)
                        .orElse(oper.getAction());
                AviOperDto dto = new AviOperDto();
                dto.setPrcAviOperId(oper.getId());
                dto.setAction(oper.getAction());
                dto.setAviName(actionName);
                dto.setAviCode(oper.getAviCode());
                dto.setAviName(oper.getAviName());
                dto.setIsEnabled(oper.getIsEnabled());
                dto.setIsRepeat(oper.getIsRepeat());
                aviOperDtoList.add(dto);

            }

            String[] aviOperaction = aviOperEntityList.stream().map(s -> s.getAction()).toArray(String[]::new);

            List<AviTrackingRecordOperEntity> recorList = aviTrackingRecordOperService.getData(orderInfo.getSn(), aviQueryDto.getAviCode()).stream()
                    .filter(s -> java.util.Arrays.asList(aviOperaction).contains(s.getAction()))
                    .collect(Collectors.toList());

            for (AviOperDto aviOper : aviOperDtoList) {
                String repeat = aviOper.getIsRepeat() ? "允许重复执行行为" : "警告重复执行可能会导致数据错乱";
                AviTrackingRecordOperEntity model = recorList.stream().filter(s -> s.getAction().equals(aviOper.getAction()))
                        .findFirst().orElse(null);

                if (model != null) {
                    aviOper.setIsProcess(true);
                    aviOper.setMessage(String.format("产品%s在%s时间点以拆分，执行状态%s\n%s", model.getSn(), model.getCreationDate(), model.getIsProcess(), repeat));
                } else {
                    aviOper.setIsProcess(false);
                    aviOper.setMessage(String.format("产品%s还未产生行为\n%s", aviQueryDto.getSn(), repeat));
                }
            }
            result.ok(aviOperDtoList, "");
        } catch (Exception exe) {
            throw new InkelinkException(exe.getMessage());
        }
        return result;
    }

}