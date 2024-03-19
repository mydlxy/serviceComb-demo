package com.ca.mfd.prc.core.main.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.main.entity.SysServiceOperLogEntity;
import com.ca.mfd.prc.core.main.mapper.ISysServiceOperLogMapper;
import com.ca.mfd.prc.core.main.service.ISysServiceOperLogService;
import org.springframework.stereotype.Service;

/**
 * 服务操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysServiceOperLogServiceImpl extends AbstractCrudServiceImpl<ISysServiceOperLogMapper, SysServiceOperLogEntity> implements ISysServiceOperLogService {

}