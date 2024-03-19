package com.ca.mfd.prc.core.main.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.main.entity.SysServiceHostManagerEntity;
import com.ca.mfd.prc.core.main.mapper.ISysServiceHostManagerMapper;
import com.ca.mfd.prc.core.main.service.ISysServiceHostManagerBaseService;
import org.springframework.stereotype.Service;

/**
 * 服务寄宿管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service("sysServiceHostManagerBaseService")
public class SysServiceHostManagerBaseImpl extends AbstractCrudServiceImpl<ISysServiceHostManagerMapper, SysServiceHostManagerEntity> implements ISysServiceHostManagerBaseService {


}