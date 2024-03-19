package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.entity.MiddleResponse;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysRequestConfigEntity;

import java.util.List;

/**
 * 系统内部站点访问
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysRequestConfigService extends ICrudService<SysRequestConfigEntity> {

    /**
     * 获取所有的 系统内部站点访问
     *
     * @return
     */
    List<SysRequestConfigEntity> getAllDatas();

    /**
     * 内部接口http默认请求方式
     *
     * @param key
     * @param content
     * @return
     */
    MiddleResponse defaultRequestClinet(String key, Object content);
}