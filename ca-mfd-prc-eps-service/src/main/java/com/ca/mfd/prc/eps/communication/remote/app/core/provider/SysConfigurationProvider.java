package com.ca.mfd.prc.eps.communication.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.core.ISysConfigurationService;
import com.ca.mfd.prc.eps.remote.app.core.entity.SysConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 获取
     *
     * @return
     */
    public List<SysConfigurationEntity> getAllDatas() {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getAllDatas();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param category
     * @return
     */
    public List<SysConfigurationEntity> getSysConfigurations(String category) {
        ResultVO<List<SysConfigurationEntity>> result = sysConfigurationService.getSysConfigurations(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param category
     * @param key
     * @return
     */
    public String getConfiguration(String key, String category) {
        ResultVO<String> result = sysConfigurationService.getConfiguration(key, category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param category
     * @return
     */
    public List<ComboInfoDTO> getComboDatas(String category) {
        ResultVO<List<ComboInfoDTO>> result = sysConfigurationService.getComboDatas(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-sysconfiguration调用失败" + result.getMessage());
        }
        return result.getData();
    }


}