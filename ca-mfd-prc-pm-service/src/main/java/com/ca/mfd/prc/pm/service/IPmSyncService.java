package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmSyncEntity;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 数据同步缓存表服务
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
public interface IPmSyncService extends ICrudService<PmSyncEntity> {

     Map<Long,Long> getPrcIdAndCmcIdMapping(List<String> prcIds);

     Map<Long,Long> getPrcIdAndCmcIdMapping();
     List<PmSyncEntity> getPmSyncsByCmcIds(List<String> cmcIds);

}