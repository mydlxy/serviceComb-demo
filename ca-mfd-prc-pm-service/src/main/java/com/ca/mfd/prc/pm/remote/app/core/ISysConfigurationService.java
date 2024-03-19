package com.ca.mfd.prc.pm.remote.app.core;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "ca-mfd-prc-core-service", path = "main/sysconfiguration", contextId = "inkelink-core-sysconfiguration")
public interface ISysConfigurationService {
    /**
     * 获取Combo列表
     *
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping({"/provider/getalldatas"})
    ResultVO<List<SysConfigurationEntity>> getAllDatas();

    /**
     * 获取Combo列表
     *
     * @param category 分类名称
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatas")
    ResultVO<List<ComboInfoDTO>> getComboDatas(@RequestParam("category") String category);

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping(value = "/provider/getsysconfigurations")
    ResultVO<List<SysConfigurationEntity>> getSysConfigurations(@RequestParam("category") String category);

    /**
     * 根据参数类型获取参数值
     *
     * @param key      关键字
     * @param category 类型
     * @return 返回text
     */
    @GetMapping(value = "/provider/getconfiguration")
    ResultVO<String> getConfiguration(@RequestParam("key") String key, @RequestParam("category") String category);

    /**
     * 编辑修改
     * @param sysConfigurationEntity
     * @return
     */
    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResultVO edit(@RequestBody SysConfigurationEntity sysConfigurationEntity);

    /**
     *
     * @param value
     * @param category
     * @param text
     * @return
     */
    @PostMapping(value = "/provider/updatebycategory")
    ResultVO updateBycategory(@RequestParam("value") String value, @RequestParam("category") String category, @RequestParam("text") String text);
}
