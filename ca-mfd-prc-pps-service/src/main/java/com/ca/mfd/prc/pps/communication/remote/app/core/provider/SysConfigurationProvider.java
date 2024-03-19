package com.ca.mfd.prc.pps.communication.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.remote.app.core.ISysConfigurationService;
import com.ca.mfd.prc.pps.communication.remote.app.core.sys.entity.SysConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysConfigurationProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidSysConfigurationProvider")
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

    public String _insertBath(List<SysConfigurationEntity> entities) {
        ResultVO<String> result = sysConfigurationService._insertBath(entities);
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

    public List<ComboInfoDTO> getComboDatasByEmptyText(String category, String emptyText) {
        ResultVO<List<ComboInfoDTO>> result = sysConfigurationService.getComboDatasByEmptyText(category, emptyText);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<ComboInfoDTO> getComboDatasByIsHide(String category, String emptyText, Boolean isHide) {
        ResultVO<List<ComboInfoDTO>> result = sysConfigurationService.getComboDatasByIsHide(category, emptyText, isHide);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public Map<String, String> getSysConfigurationMaps(String category) {
        ResultVO<Map<String, String>> result = sysConfigurationService.getSysConfigurationMaps(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }


}