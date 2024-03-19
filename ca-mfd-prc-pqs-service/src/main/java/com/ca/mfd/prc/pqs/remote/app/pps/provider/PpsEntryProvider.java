package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsEntryService;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PpsEntryProvider {

    @Autowired
    private IPpsEntryService ppsEntryService;

    public List<PpsEntryEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PpsEntryEntity>> result = ppsEntryService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

}