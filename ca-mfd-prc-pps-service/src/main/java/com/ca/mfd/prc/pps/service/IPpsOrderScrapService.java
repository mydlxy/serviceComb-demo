package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.entity.PpsOrderScrapEntity;

/**
 * 生产报废订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsOrderScrapService extends ICrudService<PpsOrderScrapEntity> {

    /**
     * 整车、电池订单报废
     *
     * */
    ScrapAffirmPara orderVeScrap(String orderNo);


    /**
     * 发送AS报废
     *
     * @param para
     */
    void sendAsScrapMessage(ScrapAffirmPara para);

    /**
     * 报废确认
     *
     * @param para
     */
    void scrapAffirm(ScrapAffirmPara para);


    /**
     * 订单直接报废(离散报废)
     *
     * @param ppsOrderScrapInfo
     */
    void productScrap(PpsOrderScrapEntity ppsOrderScrapInfo);
}