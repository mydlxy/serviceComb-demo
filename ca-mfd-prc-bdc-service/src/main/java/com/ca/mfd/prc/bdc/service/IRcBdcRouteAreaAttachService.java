package com.ca.mfd.prc.bdc.service;

import com.ca.mfd.prc.bdc.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaAttachEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块服务
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
public interface IRcBdcRouteAreaAttachService extends ICrudService<RcBdcRouteAreaAttachEntity> {
    /**
     * 查询所有附加的列表
     *
     * @return 附加列表
     */
    List<RcRouteAreaAttachShowVO> getAttachShowList();
}