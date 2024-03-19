package com.ca.mfd.prc.bdc.mapper;

import com.ca.mfd.prc.bdc.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaAttachEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块Mapper
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Mapper
public interface IRcBdcRouteAreaAttachMapper extends IBaseMapper<RcBdcRouteAreaAttachEntity> {
    /**
     * 查询所有附加的列表
     *
     * @return 附加列表
     */
    List<RcRouteAreaAttachShowVO> getAttachShowList();
}