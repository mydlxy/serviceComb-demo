package com.ca.mfd.prc.mq.rabbitmq.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "ca-mfd-prc-core-service",
        path = "main/sysconfiguration",
        contextId = "inkelink-core-rabbitmq-sysconfiguration")
public interface IRabbitMqSysConfigurationService {

    /**
     * 获取Combo列表
     *
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping({"/provider/getalldatas"})
    ResultVO<List<SysConfigurationEntity>> getAllDatas();

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping(value = "/provider/getsysconfigurations")
    ResultVO<List<SysConfigurationEntity>> getSysConfigurations(@RequestParam("category") String category);

}
