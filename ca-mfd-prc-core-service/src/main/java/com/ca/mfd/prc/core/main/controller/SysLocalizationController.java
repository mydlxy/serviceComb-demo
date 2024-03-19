package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysLocalizationEntity;
import com.ca.mfd.prc.core.main.service.ISysLocalizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 国际化
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syslocalization")
@Tag(name = "国际化")
public class SysLocalizationController extends BaseController<SysLocalizationEntity> {

    private final ISysLocalizationService sysLocalizationService;
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    public SysLocalizationController(ISysLocalizationService sysLocalizationService) {
        this.crudService = sysLocalizationService;
        this.sysLocalizationService = sysLocalizationService;
    }

    @GetMapping(value = "/getalldatas")
    @Operation(summary = "获取所有数据")
    public ResultVO<String> getalldatas() {
        String lang = identityHelper.getLoginUser().getLanguage();
        return getLangDatas(lang);
    }

    @GetMapping(value = "/getscriptdatas")
    @Operation(summary = "获取所有数据")
    public ResultVO<String> getScriptDatas() {
        return getalldatas();
    }

    @GetMapping(value = "/getlangdatas")
    @Operation(summary = "获取所有数据")
    public ResultVO<String> getLangDatas(String lang) {
        identityHelper.getLoginUser().setLanguage(lang.toUpperCase());
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        List<SysLocalizationEntity> list = sysLocalizationService.getAllDatas();
        Map res = new HashMap<>(list.size());
        list.stream().filter(c -> StringUtils.isEquals(c.getLang(), lang.toUpperCase()))
                .forEach(c -> {
                    res.put(c.getCn(), c.getEn());
                });
        return result.ok(JsonUtils.toJsonString(res));
    }

}