package com.ca.mfd.prc.eps.service.impl;

import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsAgvMonitoringMapper;
import com.ca.mfd.prc.eps.service.IEpsAgvMonitoringService;
import com.ca.mfd.prc.eps.entity.EpsAgvMonitoringEntity;
import org.springframework.stereotype.Service;

/**
 * AGV监听
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsAgvMonitoringServiceImpl extends AbstractCrudServiceImpl<IEpsAgvMonitoringMapper, EpsAgvMonitoringEntity> implements IEpsAgvMonitoringService {

}