package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.yidiantong.BizContent;

/**
 * 调用伊点通CMC平台
 */
public interface IYiDianTongService {

    /**
     * 工单号下发
     * @return
     */
     ResultVO<Boolean> sendOrderNos(String processCode);


}