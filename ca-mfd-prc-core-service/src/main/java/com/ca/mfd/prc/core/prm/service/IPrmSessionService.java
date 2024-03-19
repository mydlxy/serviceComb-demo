package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmSessionEntity;

/**
 * 登录日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmSessionService extends ICrudService<PrmSessionEntity> {
    /**
     * 获取本地会话
     *
     * @param id
     * @return
     */
    PrmSessionEntity getPrmSessionEntityById(String id);

    /**
     * 更新登录日志
     *
     * @param id
     * @param status
     */
    void updateStatusById(String id, int status);

    /**
     * 更新过期时间
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatusByExpireDt(String id, int status);

    /**
     * 获取单条数据
     *
     * @param id
     * @param isExpire
     * @return
     */
    PrmSessionEntity getPrmSessionEntityByIdStatus(String id, Boolean isExpire);

    /**
     * 根据过期时间/状态 查询单条记录
     *
     * @param id 日志ID
     * @return 返回单条实体
     */
    PrmSessionEntity getPrmSessionEntityByExpireDt(String id);
}