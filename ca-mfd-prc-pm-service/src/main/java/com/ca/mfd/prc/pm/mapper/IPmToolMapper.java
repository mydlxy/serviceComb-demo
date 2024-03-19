package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 工具
 * @date 2023-08-29
 */
@Mapper
public interface IPmToolMapper extends IBaseMapper<PmToolEntity> {

    /**
     * 查询一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个列表
     */

    List<PmToolEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);
}