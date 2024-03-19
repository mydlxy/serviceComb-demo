package com.ca.mfd.prc.pm.service.impl;


import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.mapper.IPmBopMapper;
import com.ca.mfd.prc.pm.service.IPmBopParentService;
import org.springframework.stereotype.Service;


/**
 * @author inkelink
 * @Description: AVI站点(定义父类 为了解决项目中循环依赖的问题 ， 项目规范中不能使用循环依赖))
 * 所有在pmService里要用的方法都在这里面定义
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service("pmBopParentService")
public class PmBopParentServiceImpl extends AbstractCrudServiceImpl<IPmBopMapper, PmBopEntity> implements IPmBopParentService {

}