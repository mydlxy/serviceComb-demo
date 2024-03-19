package com.ca.mfd.prc.eps.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.SetModelDTO;
import com.ca.mfd.prc.eps.entity.EpsAssembleDetailEntity;
import com.ca.mfd.prc.eps.service.IEpsAssembleDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 装配单明细
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsassembledetail")
@Tag(name = "装配单明细")
public class EpsAssembleDetailController extends BaseController<EpsAssembleDetailEntity> {

    private final IEpsAssembleDetailService epsAssembleDetailService;

    @Autowired
    public EpsAssembleDetailController(IEpsAssembleDetailService epsAssembleDetailService) {
        this.crudService = epsAssembleDetailService;
        this.epsAssembleDetailService = epsAssembleDetailService;
    }

    @Operation(summary = "获取装配单模板配置")
    @PostMapping("save")
    public ResultVO<List<ComboInfoDTO>> save(@RequestBody SetModelDTO model) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        if (model.getDetails() == null || model.getDetails().isEmpty()) {
            result.setCode(-1);
            result.setMessage("没有数据");
            return result;
        }

        List<Long> ids = model.getDetails().stream().filter(w -> Objects.nonNull(w.getId()) && w.getId() > 0L).map(EpsAssembleDetailEntity::getId).collect(Collectors.toList());
        if (!ids.isEmpty()) {

            UpdateWrapper<EpsAssembleDetailEntity> delWoData = new UpdateWrapper<>();
            delWoData.lambda().eq(EpsAssembleDetailEntity::getPrcReportAssembleSetId, model.getId()).notIn(EpsAssembleDetailEntity::getId, ids);
            epsAssembleDetailService.delete(delWoData);
        }

        int displayNo = 1;
        for (EpsAssembleDetailEntity item : model.getDetails()) {
            if (Objects.nonNull(item.getId()) && item.getId() > 0L) {

                UpdateWrapper<EpsAssembleDetailEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(EpsAssembleDetailEntity::getTitle, item.getTitle())
                        .set(EpsAssembleDetailEntity::getKeyData, item.getKeyData())
                        .set(EpsAssembleDetailEntity::getKeyContent, item.getKeyContent())
                        .set(EpsAssembleDetailEntity::getDisplayNo, item.getDisplayNo())
                        .set(EpsAssembleDetailEntity::getWorkstationName, item.getWorkstationName())
                        .eq(EpsAssembleDetailEntity::getId, item.getId());
                epsAssembleDetailService.update(upset);
            } else {
                item.setPrcReportAssembleSetId(Long.valueOf(model.getId()));
                item.setDisplayNo(displayNo);
                epsAssembleDetailService.insert(item);
            }
            displayNo++;
        }
        epsAssembleDetailService.saveChange();
        return result.ok(null, "获取数据成功");
    }
}