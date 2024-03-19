package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 作业
 * @date 2023-08-29
 */
@Mapper
public interface IPmToolJobMapper extends IBaseMapper<PmToolJobEntity> {

    /**
     * 查询一个列表
     *
     * @param shopId
     * @param version
     * @return 返回一个列表
     */
    List<PmToolJobEntity> getSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);
}