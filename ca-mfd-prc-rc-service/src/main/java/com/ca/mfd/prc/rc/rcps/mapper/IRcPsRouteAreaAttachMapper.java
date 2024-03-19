package com.ca.mfd.prc.rc.rcps.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.rc.rcps.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteAreaAttachEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块Mapper
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Mapper
public interface IRcPsRouteAreaAttachMapper extends IBaseMapper<RcPsRouteAreaAttachEntity> {
    /**
     * 查询所有附加的列表
     *
     * @return 附加列表
     */
    List<RcRouteAreaAttachShowVO> getAttachShowList();
}