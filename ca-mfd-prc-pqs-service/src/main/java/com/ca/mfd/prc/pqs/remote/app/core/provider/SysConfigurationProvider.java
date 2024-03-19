package com.ca.mfd.prc.pqs.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.core.ISysConfigurationService;
import com.ca.mfd.prc.pqs.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class SysConfigurationProvider {

    @Autowired
    private ISysConfigurationService sysConfigurationService;

    public List<SysConfigurationEntity> getAllDatas() {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }


    public List<ComboInfoDTO> getComboDatas(String category) {
        ResultVO<List<ComboInfoDTO>> result = sysConfigurationService.getComboDatas(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<SysConfigurationEntity> getSysConfigurations(String category) {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getSysConfigurations(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String getConfiguration(String key, String category) {
        ResultVO<String> result = sysConfigurationService.getConfiguration(key, category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }
}