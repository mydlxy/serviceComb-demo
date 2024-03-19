package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmBopEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 超级BOP服务
 * @date 2023年08月30日
 * @变更说明 BY inkelink At 2023年08月30日
 */
public interface IPmBopService extends ICrudService<PmBopEntity> {

    /**
     * 根据版本号获取bop
     *
     * @param shopId
     * @param aFalse
     * @return
     */
    List<PmBopEntity> getPmBopEntityByVersion(Long shopId, Boolean aFalse);

    Map<String, String> getExcelHead();

    void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData);
    void export(String shopCode, HttpServletResponse response) throws IOException;

    List<PmBopEntity> getAllDatas();

    List<PmBopEntity> getByWorkStationId(Long stationId);

    Map<String,List<Map<String, String>>> getSeparateList(List<Map<String, String>> listOfEachSheet);
    List<PmBopEntity> getByShopId(Long shopId);

    Map<String, String> getWoBopMapping();
    Map<String, String> getMaterialBopMapping();
    Map<String, String> getToolBopMapping();
    Map<String, String> getJobBopMapping();
    Map<String, String> getOperBookBopMapping();

    PageData<PmBopEntity> pageGroup(PageDataDto model);

    void updateByProcessNo(PmBopEntity pmBopEntity);

    void delByProcessNo(PmBopEntity pmBopEntity);

    void validDataUnique(Serializable id, String columnName, String value, String errMsg, String... parentColunNameAndValue);



}