package com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.mq.rabbitmq.entity.RetryTimeEnum;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitMqSysConfigurationService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.RabbitmqRetryTimeDTO;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: jay.he
 * @Description:
 */
@Service
public class RabbitMqSysConfigurationProvider {
    @Autowired
    private IRabbitMqSysConfigurationService sysConfigurationService;

    public List<SysConfigurationEntity> getAllDatas() {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<RabbitmqRetryTimeDTO> getRabbitmqRetryTime() {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getSysConfigurations("rabbitmqRetryTime");
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-rabbitmq-sysconfiguration调用失败" + result.getMessage());
        }
        List<SysConfigurationEntity> list = result.getData();

        List<RabbitmqRetryTimeDTO> retryTimeDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (SysConfigurationEntity entity : list) {
                RabbitmqRetryTimeDTO time = new RabbitmqRetryTimeDTO();
                long second = 0l;
                try {
                    String code = entity.getValue().substring(entity.getValue().length() - 1);
                    RetryTimeEnum retryTimeEnum = RetryTimeEnum.findByCode(code);
                    if (retryTimeEnum != null) {
                        second = Long.parseLong(entity.getValue().substring(0, entity.getValue().length() - 1)) * retryTimeEnum.value();
                    }
                } catch (Exception ex) {

                }
                time.setTime(second);
                retryTimeDTOList.add(time);
            }
        }
        retryTimeDTOList = retryTimeDTOList.stream().sorted(Comparator.comparing(RabbitmqRetryTimeDTO::getTime)).collect(Collectors.toList());

        return retryTimeDTOList;
    }

}