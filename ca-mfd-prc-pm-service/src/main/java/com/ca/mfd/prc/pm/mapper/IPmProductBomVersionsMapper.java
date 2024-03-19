package com.ca.mfd.prc.pm.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: BOM版本
 * @date 2023-08-29
 */
@Mapper
public interface IPmProductBomVersionsMapper extends IBaseMapper<PmProductBomVersionsEntity> {

    /**
     * 根据零件号获取整车物料版本信息
     *
     * @param materialNo 物料编号
     * @return 返回
     */
    List<PmProductBomVersionsEntity> getVersionByMaterialNo(@Param("materialNo") String materialNo, @Param("orderCategory") String orderCategory);

}