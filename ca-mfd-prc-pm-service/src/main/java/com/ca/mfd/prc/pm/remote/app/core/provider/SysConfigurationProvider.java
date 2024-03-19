package com.ca.mfd.prc.pm.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.ISysConfigurationService;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    public boolean edit(SysConfigurationEntity sysConfigurationEntity){
        ResultVO result = sysConfigurationService.edit(sysConfigurationEntity);
        return result.getSuccess();
    }

    public boolean updateBycategory(String value, String category, String text){
        ResultVO result = sysConfigurationService.updateBycategory(value,category,text);
        return result.getSuccess();
    }
}
