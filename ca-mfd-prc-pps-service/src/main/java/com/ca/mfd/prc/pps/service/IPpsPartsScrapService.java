package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.PartsScrapPara;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.entity.PpsPartsScrapEntity;

/**
 *
 * @Description: 零件报废服务
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
public interface IPpsPartsScrapService extends ICrudService<PpsPartsScrapEntity> {

    /**
     * 报废
     *
     * @param request
     */
    void scrap(PartsScrapPara request);

    /**
     * 报废确认
     *
     * @param para
     */
    void scrapAffirm(ScrapAffirmPara para);
}