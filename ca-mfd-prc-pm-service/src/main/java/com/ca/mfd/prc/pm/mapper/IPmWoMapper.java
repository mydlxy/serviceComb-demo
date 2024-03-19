package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 岗位操作
 * @date 2023-08-29
 */
@Mapper
public interface IPmWoMapper extends IBaseMapper<PmWoEntity> {

    /**
     * 查询一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个列表
     */

    List<PmWoEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);
}