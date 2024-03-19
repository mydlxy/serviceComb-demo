package com.ca.mfd.prc.pm.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pm.dto.VcurrentWorkStationInfo;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink ${email}
 * @Description: 岗位
 * @date 2023-08-29
 */
@Mapper
public interface IPmWorkStationMapper extends IBaseMapper<PmWorkStationEntity> {

    /**
     * 查询一个列表
     *
     * @param shopId  shopId
     * @param version version
     * @return 返回一个列表
     */
    List<PmWorkStationEntity> getEnableSnapshotByShopIdAndVersion(@Param("shopId") String shopId, @Param("version") int version);

    /**
     * 获取当前岗位列表
     *
     * @param page 分页
     * @param pms  参数
     * @return 当前岗位信息
     */
    Page<VcurrentWorkStationInfo> getCurrentWorkplaceList(Page<VcurrentWorkStationInfo> page, @Param("pms") Map<String, Object> pms);
}