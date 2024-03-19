package com.ca.mfd.prc.avi.service.impl;

import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.dto.AviStationDTO;
import com.ca.mfd.prc.avi.dto.TpsCodeSanDTO;
import com.ca.mfd.prc.avi.dto.TpsPrintParamDTO;
import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.service.IAviFrontService;
import com.ca.mfd.prc.avi.service.IAviLogicService;
import com.ca.mfd.prc.avi.service.IAviOperationLogService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端处理
 *
 * @author lwb
 * @since 1.0.0 2023-04-11
 */
@Service
public class AviFrontServiceImpl implements IAviFrontService {
    @Autowired
    //IPmVersionService pmVersionService;
    PmVersionProvider pmVersionProvider;

    @Autowired
    //private IPpsLogicService ppsLogicService;
    private PpsEntryProvider ppsEntryProvider;

    @Autowired
    private IAviLogicService aviLogicService;

    @Autowired
    private IAviTrackingRecordService aviTrackingRecordService;

    @Autowired
    private IAviOperationLogService aviOperationLogService;

    private PmAllDTO getObjectedPm() {
        return pmVersionProvider.getObjectedPm();
    }

    /**
     * 获取Avi站点数据
     *
     * @param strIp 参数ip
     * @return 返回一个avi过点实体
     */
    @Override
    public AviStationDTO getAviTemplate(String strIp) {
        return aviLogicService.getAviStationInfo(strIp);
    }

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 已下发未打印焊装上上线列表
     */
    @Override
    public List<BodyVehicleDTO> getNoPrintTpscode() {
        //List<BodyVehicleDTO> rspData = ppsLogicService.getNoPrintTpscode();
        List<BodyVehicleDTO> rspData = ppsEntryProvider.getNoPrintTpscode();
        if (null == rspData) {
            return Lists.newArrayList();
        }
        return rspData;
    }

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    @Override
    public List<BodyVehicleDTO> getPrintTpscode() {
        //List<BodyVehicleDTO> rspData = ppsLogicService.getPrintTpscode();
        List<BodyVehicleDTO> rspData = ppsEntryProvider.getPrintTpscode();
        if (null == rspData) {
            return Lists.newArrayList();
        }
        return rspData;
    }

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    @Override
    public List<BodyVehicleDTO> getDownTpsCode() {
        //List<BodyVehicleDTO> rspData = ppsLogicService.getDownTpsCode();
        List<BodyVehicleDTO> rspData = ppsEntryProvider.getDownTpsCode();
        if (null == rspData) {
            return Lists.newArrayList();
        }
        return rspData;
    }

    /**
     * 产品识别码打印
     */
    @Override
    public void tpsCodePrint(TpsPrintParamDTO tpsPrintParamInfo) {
        if (tpsPrintParamInfo == null) {
            throw new InkelinkException("未获取到请求参数");
        }
        String tpsCode = tpsPrintParamInfo.getTpsCode();
        String aviCode = tpsPrintParamInfo.getAviCode();
        PmAllDTO pmAllist = getObjectedPm();
        List<PmAviEntity> avisList = pmAllist.getAvis();
        if (avisList == null || avisList.size() == 0) {
            throw new InkelinkException("未获得avi集合");
        }
        PmAviEntity avisinfo = avisList.stream().filter(c -> c.getAviCode().equals(aviCode))
                .findFirst().orElse(null);
        if (avisinfo == null) {
            throw new InkelinkException("Avi不存在" + aviCode);
        }
        List<PmWorkShopEntity> shopsList = pmAllist.getShops();
        if (shopsList == null || shopsList.size() == 0) {
            throw new InkelinkException("未获得车间集合");
        }
        PmWorkShopEntity shopsinfo = shopsList.stream().filter(c -> c.getId().equals(avisinfo.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (shopsinfo == null) {
            throw new InkelinkException("未获得车间信息");
        }
        //aviLogicService.tpsCodePrint(tpsCode, shopsinfo.getCode());
        aviLogicService.tpsCodePrint(tpsCode, shopsinfo.getWorkshopCode());
    }

    /**
     * 焊装上线点扫描
     *
     * @param tpsCodeSanDTO 条件
     */
    @Override
    public void tpsCodeScan(TpsCodeSanDTO tpsCodeSanDTO) {
        aviLogicService.tpsCodeScan(tpsCodeSanDTO);
    }

    /**
     * @return 返回车间列表
     */
    @Override
    public List<PmWorkShopEntity> getShopInfos() {
        PmAllDTO pmAllist = getObjectedPm();
        if (pmAllist == null) {
            throw new InkelinkException("基础信息查询异常");
        }
        List<PmWorkShopEntity> list = pmAllist.getShops();
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        list = list.stream().sorted(Comparator.comparing(PmWorkShopEntity::getDisplayNo)).collect(Collectors.toList());
        return list;
    }

    /**
     * 根据车间获取线体
     *
     * @param pmShopId 车间ID
     * @return 返回线体列表
     */
    @Override
    public List<PmLineEntity> getAreaInfos(Long pmShopId) {
        //        if (StringUtils.isBlank(pmShopId)) {
        //            throw new InkelinkException("车间ID不能为空");
        //        }
        if (pmShopId <= 0) {
            throw new InkelinkException("车间ID不能为空");
        }
        PmAllDTO pmAllist = getObjectedPm();
        if (pmAllist == null) {
            throw new InkelinkException("基础信息查询异常");
        }
        //List<PmLineEntity> list = pmAllist.getAreas();
        List<PmLineEntity> list = pmAllist.getLines();
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        //list = list.stream().filter(c -> c.getPmShopId().equals(pmShopId)).collect(Collectors.toList());
        list = list.stream().filter(c -> c.getPrcPmWorkshopId().equals(pmShopId)).collect(Collectors.toList());
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        list = list.stream().sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
        return list;
    }

    /**
     * 根据线体ID查询avi 列表
     *
     * @param pmAreaId 线体ID
     * @return 返回线体列表
     */
    @Override
    public List<PmAviEntity> getAviInfos(String pmAreaId) {
        long lineId = ConvertUtils.stringToLong(pmAreaId);
        if (StringUtils.isBlank(pmAreaId) || lineId <= 0) {
            throw new InkelinkException("线体ID不能为空");
        }
        PmAllDTO pmAllist = getObjectedPm();
        if (pmAllist == null) {
            throw new InkelinkException("基础信息查询异常");
        }
        List<PmAviEntity> list = pmAllist.getAvis();
        if (list == null) {
            return new ArrayList<>();
        }
        //list = list.stream().filter(c -> c.getPmAreaId().equals(pmAreaId)).collect(Collectors.toList());
        list = list.stream().filter(c -> c.getPrcPmLineId().equals(lineId)).collect(Collectors.toList());
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        //list = list.stream().sorted(Comparator.comparing(PmAviEntity::getCode)).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(PmAviEntity::getAviCode)).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取过点记录
     *
     * @param model 分页参数
     * @return 返回一个分页列表
     */
    @Override
    public PageData<AviTrackingRecordEntity> getTrackingRec(PageDataDto model) {
        PageData<AviTrackingRecordEntity> pageData = new PageData<>(new ArrayList<>(), 0);
        pageData.setPageIndex(model.getPageIndex());
        pageData.setPageSize(model.getPageSize());
        aviTrackingRecordService.getAviTrackingRecordInfo(pageData, model.getConditions(), model.getSorts());
        return pageData;
    }

    /**
     * 获取过点数据
     *
     * @param aviCode 参数 aviCode
     * @return 返回一个获取过点数据列表
     */
    @Override
    public List<AviPassedRecordDTO> getAviPassedRecord(String aviCode) {
        return aviTrackingRecordService.getAviPassedRecord(aviCode);
    }

    /**
     * 获取信息履历
     *
     * @param model 分页参数
     * @return 分页列表
     */
    @Override
    public PageData<AviOperationLogEntity> getAviLogOperation(PageDataDto model) {
        return aviOperationLogService.page(model);
    }
}
