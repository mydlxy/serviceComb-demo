package com.ca.mfd.prc.pps.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsOrderChangeLogEntity;
import com.ca.mfd.prc.pps.mapper.IPpsOrderChangeLogMapper;
import com.ca.mfd.prc.pps.service.IPpsOrderChangeLogService;
import org.springframework.stereotype.Service;

/**
 * 订单替换日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsOrderChangeLogServiceImpl extends AbstractCrudServiceImpl<IPpsOrderChangeLogMapper, PpsOrderChangeLogEntity> implements IPpsOrderChangeLogService {


}