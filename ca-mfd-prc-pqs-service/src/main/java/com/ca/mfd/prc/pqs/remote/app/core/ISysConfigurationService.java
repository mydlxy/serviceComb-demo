package com.ca.mfd.prc.pqs.remote.app.core;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qu
 */
@FeignClient(
        name = "ca-mfd-prc-core-service",
        path = "main/sysconfiguration",
        contextId = "inkelink-core-sysconfiguration")
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
}
