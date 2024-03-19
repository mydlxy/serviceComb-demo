package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author inkelink ${email}
 * @Description: 车间
 * @date 2023-08-29
 */
@Mapper
public interface IPmWorkShopMapper extends IBaseMapper<PmWorkShopEntity> {

    /**
     * 获取一个实体
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个实体
     */
    PmWorkShopEntity getByShopIdAndVersion(@Param("shopId") Long shopId, @Param("version") Integer version);
}