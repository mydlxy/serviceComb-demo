package com.ca.mfd.prc.pps.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSnConfigEntity;
import com.ca.mfd.prc.pps.remote.app.core.ISysSnConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysSnConfigProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class SysSnConfigProvider {

    @Autowired
    private ISysSnConfigService sysSnConfigService;

    public String createSn(String category) {
        ResultVO<String> result = sysSnConfigService.createSn(category);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
        String sn = result.getData();
        if (StringUtils.isBlank(sn)) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败,生成序号失败");
        }
        return sn;
    }

    public String createSnBypara(String category, Map<String, String> para) {
        ResultVO<String> result = sysSnConfigService.createSnBypara(category, para);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 添加编号规则
     *
     * @param seqDatas
     * @return
     */
    public void addSeqConfig(List<SysSnConfigEntity> seqDatas) {
        ResultVO<String> result = sysSnConfigService.addSeqConfig(seqDatas);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param categorys
     * @return
     */
    public void deleteByCategory(List<String> categorys) {
        ResultVO<String> result = sysSnConfigService.deleteByCategory(categorys);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssnconfig调用失败" + result.getMessage());
        }
    }
}