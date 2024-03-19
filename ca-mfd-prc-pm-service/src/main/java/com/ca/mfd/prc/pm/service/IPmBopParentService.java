package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmBopEntity;

/**
 * @author inkelink ${email}
 * @Description: AVI站点(为了解决项目中循环依赖的问题 ， 项目规范中不能使用循环依赖)
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmBopParentService extends ICrudService<PmBopEntity> {

}