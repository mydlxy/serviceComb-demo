package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.dto.AviStationDTO;
import com.ca.mfd.prc.avi.dto.TpsCodeSanDTO;
import com.ca.mfd.prc.avi.dto.TpsPrintParamDTO;
import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;

import java.util.List;


/**
 * AVI 过点相关操作
 *
 * @author banny.luo
 * @since 1.0.0 2023-04-06
 */
public interface IAviFrontService {
    /**
     * 获取Avi站点数据
     *
     * @param strIp 参数ip
     * @return 返回一个avi过点实体
     */
    AviStationDTO getAviTemplate(String strIp);

    /**
     * 获取已下发未打印焊装上线列表
     *
     * @return 返回已下发未打印焊装上线列表
     */
    List<BodyVehicleDTO> getNoPrintTpscode();

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 返回已下发已打印焊装上线列表
     */
    List<BodyVehicleDTO> getPrintTpscode();

    /**
     * 打印条码
     *
     * @param params 参数列表
     */
    //void printProductNo(Map<String, Object> params);

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    List<BodyVehicleDTO> getDownTpsCode();

    /**
     * 产品识别码打印
     *
     * @param tpsPrintParamInfo 参数列表
     */
    void tpsCodePrint(TpsPrintParamDTO tpsPrintParamInfo);

    /**
     * 获取车间信息
     *
     * @return 返回车间列表
     */
    List<PmWorkShopEntity> getShopInfos();

    /**
     * 根据车间获取线体
     *
     * @param pmShopId 车间ID
     * @return 返回线体列表
     */
    List<PmLineEntity> getAreaInfos(Long pmShopId);

    /**
     * 根据线体ID查询avi 列表
     *
     * @param pmAreaId 线体ID
     * @return 返回avi列表
     */
    List<PmAviEntity> getAviInfos(String pmAreaId);

    /**
     * 获取过点记录
     *
     * @param model 分页参数
     * @return 过点记录分页列表
     */
    PageData<AviTrackingRecordEntity> getTrackingRec(PageDataDto model);

    /**
     * 获取过点数据
     *
     * @param aviCode 参数 aviCode
     * @return 返回一个获取过点数据列表
     */
    List<AviPassedRecordDTO> getAviPassedRecord(String aviCode);

    /**
     * 获取信息履历
     *
     * @param model 分页参数
     * @return 信息履历列表
     */
    PageData<AviOperationLogEntity> getAviLogOperation(PageDataDto model);

    /**
     * 焊装上线点扫描
     *
     * @param tpsCodeSanDTO 条件
     */
    void tpsCodeScan(TpsCodeSanDTO tpsCodeSanDTO);
}
