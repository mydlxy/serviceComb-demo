package com.ca.mfd.prc.eps.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 车辆操作信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Mapper
public interface IEpsVehicleWoMapper extends IBaseMapper<EpsVehicleWoEntity> {
    /**
     * 分页信息
     *
     * @param page
     * @param pms
     * @return Page<EpsVehicleWoEntity>
     */
    Page<EpsVehicleWoEntity> getPageVehicleDatas(Page<EpsVehicleWoEntity> page, @Param("pms") Map<String, Object> pms);
}