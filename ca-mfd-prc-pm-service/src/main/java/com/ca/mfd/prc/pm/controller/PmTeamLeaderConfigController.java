package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmTeamLeaderConfigEntity;
import com.ca.mfd.prc.pm.service.IPmTeamLeaderConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: 班组长配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmteamleaderconfig")
@Tag(name = "班组长配置")
public class PmTeamLeaderConfigController extends BaseController<PmTeamLeaderConfigEntity> {

    private final IPmTeamLeaderConfigService pmTeamLeaderConfigService;

    @Autowired
    public PmTeamLeaderConfigController(IPmTeamLeaderConfigService pmTeamLeaderConfigService) {
        this.crudService = pmTeamLeaderConfigService;
        this.pmTeamLeaderConfigService = pmTeamLeaderConfigService;
    }

    /**
     * 查询班组长配置
     *
     * @param conditionInfos 条件
     * @return 查询结果
     */
    @PostMapping(value = "/provider/getdata")
    @Operation(summary = "查询班组长配置")
    public ResultVO<List<PmTeamLeaderConfigEntity>> getData(@RequestBody List<ConditionDto> conditionInfos) {
        ResultVO<List<PmTeamLeaderConfigEntity>> result = new ResultVO<>();
        List<PmTeamLeaderConfigEntity> data = pmTeamLeaderConfigService.getData(conditionInfos);
        return result.ok(data);
    }

    @Operation(summary = "根据线体id 查询该线体下的所有班组")
    @Parameter(name = "list", description = "班组绑定")
    @PostMapping("editpmteamleaderconfiglist")
    public ResultVO<String> editPmTeamleaderConfigList(@RequestBody List<PmTeamLeaderConfigEntity> list) {
        ResultVO<String> result = new ResultVO<>();
        List<PmTeamLeaderConfigEntity> inserList = new ArrayList<>();
        list.stream().forEach(item -> {
            if (!item.getId().equals(StringUtils.EMPTY)) {
                pmTeamLeaderConfigService.updateBatchById(list);
            } else {
                inserList.add(item);
            }
        });
        if (inserList.size() > 0) {
            pmTeamLeaderConfigService.insertBatch(inserList);
        }
        pmTeamLeaderConfigService.saveChange();
        return result.ok("操作成功", "操作成功");
    }

}