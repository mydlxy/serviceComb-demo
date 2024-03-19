package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: AVI站点
 * @date 2023-08-29
 */
@Mapper
public interface IPmAviMapper extends IBaseMapper<PmAviEntity> {

    /**
     * 获取一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个列表
     */
    List<PmAviEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);

    /**
     * 获取一个列表
     *
     * @param areaId areaId
     * @return 返回一个列表
     */
    List<PmAviEntity> getListByAreaId(@Param("areaId") String areaId);

}