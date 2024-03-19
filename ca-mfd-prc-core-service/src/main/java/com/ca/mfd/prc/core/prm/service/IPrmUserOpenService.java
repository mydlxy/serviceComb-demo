package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmUserOpenEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户第三方登录信息
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmUserOpenService extends ICrudService<PrmUserOpenEntity> {
    List<PrmUserOpenEntity> getTokens(Serializable userId);
}