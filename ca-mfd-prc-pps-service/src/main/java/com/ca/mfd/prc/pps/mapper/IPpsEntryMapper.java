package com.ca.mfd.prc.pps.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.dto.StampingEntryDTO;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-08-29
 */
@Mapper
public interface IPpsEntryMapper extends IBaseMapper<PpsEntryEntity> {


    /**
     * 车间工单分线监控
     *
     * @param req 参数集合
     * @return 车间工单分线监控列表
     */
    @MapKey("id")
    List<Map> getShopPlanMonitorInfos(@Param("req") Map<String, Object> req);

    /**
     * 车间订单
     *
     * @param page 分页
     * @param pms  参数集合
     * @return 车间订单
     */
    Page<OrderEntryInfo> gestShopOrders(Page<OrderEntryInfo> page, @Param("pms") Map<String, Object> pms);

    /**
     * 获取工单列表
     *
     * @param pms 参数集合
     * @return 工单列表
     */
    List<OrderEntryInfo> getEntryOrder(@Param("pms") Map<String, Object> pms);

    /**
     * 车间工单（外部）
     *
     * @param page 分页
     * @param pms  参数集合
     * @return 车间工单
     */
    Page<PpsEntryEntity> getEntryStampingPageDatas(Page<PpsEntryEntity> page, @Param("pms") Map<String, Object> pms);

    /**
     * 获取冲压生产工单列表（外部）
     *
     * @param areaCode 参数集合
     * @return 冲压工单
     */
    List<StampingEntryDTO> getStampingEntry(String areaCode);

    /**
     * 根据车间返回车间订单打印状态
     *
     * @param workShop  车间code
     * @param isDispose 是否处理
     * @param entryType   工单类型
     * @param status      整车状态
     * @return 返回一个列表
     */
    List<BodyVehicleDTO> getNoPrintTpscode(String workShop, Integer isDispose, Integer entryType, Integer status);
}