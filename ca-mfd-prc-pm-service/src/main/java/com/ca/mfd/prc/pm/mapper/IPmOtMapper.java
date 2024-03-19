package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 操作终端
 * @date 2023-08-29
 */
@Mapper
public interface IPmOtMapper extends IBaseMapper<PmOtEntity> {

    /**
     * 获取一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个列表
     */
    List<PmOtEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);
}