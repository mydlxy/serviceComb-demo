package com.ca.mfd.prc.pmc.remote.app.cms.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.PmEquipmentVO;
import com.ca.mfd.prc.pmc.remote.app.cms.ICmcPmFactoryService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmcPmFactoryProvider {
    private static final Log log = LogFactory.getLog(CmcPmFactoryProvider.class);

    @Autowired
    private ICmcPmFactoryService cmcPmFactoryService;

    public List<PmEquipmentVO> getEquipmentData(DataDto model) {
        ResultVO<List<PmEquipmentVO>> result = cmcPmFactoryService.getEquipmentPage(model);
        if (!result.getSuccess()) {
            log.error("IOT获取设备信息异常,错误信息:" + result.getMessage());
            throw new InkelinkException("获取Bop时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }
        return result.getData();
    }
}
