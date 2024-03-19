package com.ca.mfd.prc.core.message.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.message.entity.MsgEmailConfigEntity;
import com.ca.mfd.prc.core.message.mapper.IMsgEmailConfigMapper;
import com.ca.mfd.prc.core.message.service.IMsgEmailConfigService;
import org.springframework.stereotype.Service;

/**
 * @author jay.he
 * @Description: 邮箱维护账号
 */
@Service
public class MsgEmailConfigServiceImpl extends AbstractCrudServiceImpl<IMsgEmailConfigMapper, MsgEmailConfigEntity> implements IMsgEmailConfigService {


}