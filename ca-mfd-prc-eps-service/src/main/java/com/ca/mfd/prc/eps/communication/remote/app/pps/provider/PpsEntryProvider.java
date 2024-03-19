package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsEntryService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PpsEntryProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsEntryProvider")
public class PpsEntryProvider {

    @Autowired
    private IPpsEntryService ppsEntryService;

    /**
     * 获取
     *
     * @param conditions
     * @return
     */
    public List<PpsEntryEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PpsEntryEntity>> result = ppsEntryService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param sn
     * @param entryType
     * @param shopCode
     * @return
     */
    public PpsEntryEntity getFirstEntryTypeShopCodeSn(String sn, Integer entryType, String shopCode) {
        ResultVO<PpsEntryEntity> result = ppsEntryService.getFirstEntryTypeShopCodeSn(sn, entryType, shopCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

}