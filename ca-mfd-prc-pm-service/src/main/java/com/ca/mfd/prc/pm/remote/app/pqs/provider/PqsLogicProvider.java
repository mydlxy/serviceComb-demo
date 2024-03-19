package com.ca.mfd.prc.pm.remote.app.pqs.provider;


import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;

import com.ca.mfd.prc.pm.remote.app.pqs.IPqsLogicService;
import com.ca.mfd.prc.pm.remote.app.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pm.remote.app.pqs.dto.DefectAnomalyParaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PqsLogicProvider {
    @Autowired
    private IPqsLogicService pqsLogicService;

    /**
     * 获取缺陷数据
     * @return
     */
    public List<DefectAnomalyDto> getWorkPlaceList() {
        ResultVO<PageData<DefectAnomalyDto>>result = pqsLogicService.getWorkPlaceList(new DefectAnomalyParaInfo());
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pqs-pqslogic调用失败" + result.getMessage());
        }
        PageData<DefectAnomalyDto> page = result.getData();
        if(page != null){
            return page.getDatas();
        }
        return Collections.emptyList();
    }

}
