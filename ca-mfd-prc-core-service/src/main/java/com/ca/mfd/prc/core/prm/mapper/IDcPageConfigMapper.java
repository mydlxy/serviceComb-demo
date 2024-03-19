package com.ca.mfd.prc.core.prm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.dc.dto.DcDetail;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 */
@Mapper
public interface IDcPageConfigMapper extends IBaseMapper<DcPageConfigEntity> {

    /**
     * 获取dc动态页面配置数据
     *
     * @param code
     * @return
     */
    List<DcDetail> getDcDetail(String code);
}
