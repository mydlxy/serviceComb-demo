package com.ca.mfd.prc.rc.rcavi.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.rc.rcavi.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaAttachEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块服务
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
public interface IRcAviRouteAreaAttachService extends ICrudService<RcAviRouteAreaAttachEntity> {
    /**
     * 查询所有附加的列表
     *
     * @return 查询列表
     */
    List<RcRouteAreaAttachShowVO> getAttachShowList();
}