package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.eps.dto.AnewPrintEntryReportParaDTO;
import com.ca.mfd.prc.eps.dto.SaveTrcBarcodeParaDTO;
import com.ca.mfd.prc.eps.dto.SaveTrcBarcodeResultDTO;
import com.ca.mfd.prc.eps.dto.SystemSaveTrcBarCodeDTO;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.remote.app.pps.dto.CloseStampingEntryInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 岗位相关
 *
 * @author inkelink Eric。zhou
 * @since 1.0.0 2023-04-12
 */
public interface IWoLogicService {

    /**
     * 管理端修改工艺状态
     *
     * @param woId
     * @param status
     */
    void systemSaveWo(Serializable woId, Integer status);

    /**
     * 提交工艺
     *
     * @param woId
     * @param status
     */
    void saveWo(Serializable woId, int status);

    /**
     * 提交工艺
     *
     * @param woId
     * @param status
     * @param woData
     */
    void saveWo(Serializable woId, int status, String woData);


    /**
     * 上传生产数据（有工艺）
     *
     * @param woId
     * @param woData
     */
    void uploadingData(String woId, String woData);


    /**
     * 后台补录条码追溯工艺
     *
     * @param info
     * @return SaveTrcBarcodeResultDTO
     */
    SaveTrcBarcodeResultDTO systemSaveTrcBarCode(SystemSaveTrcBarCodeDTO info);


    /**
     * 提交追溯工艺
     *
     * @param info
     * @param isConstraint
     * @return SaveTrcBarcodeResultDTO
     */
    SaveTrcBarcodeResultDTO saveTrcBarcode(SaveTrcBarcodeParaDTO info, Boolean isConstraint);

    /**
     * 修改共享工艺所属岗位
     *
     * @param wokplaceName
     * @param workplaceId
     * @param productCode
     * @return List<EpsVehicleWoEntity>
     */
    List<EpsVehicleWoEntity> updateCommunalWo(String wokplaceName, Serializable workplaceId, String productCode);

    /**
     * 关闭冲压工单
     *
     * @param para
     */
    void closeStampingEntry(CloseStampingEntryInfo para);

    /**
     * 冲压重新报工打印
     *
     * @param para
     */
    void anewPrintEntryReport(AnewPrintEntryReportParaDTO para);

    /**
     * 冲压报工
     *
     * @param para
     */
    void newStampingEntryReport(CloseStampingEntryInfo para);
}