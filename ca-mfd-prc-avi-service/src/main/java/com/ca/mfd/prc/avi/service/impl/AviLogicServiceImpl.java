package com.ca.mfd.prc.avi.service.impl;

import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.constant.Constant;
import com.ca.mfd.prc.avi.dto.AviMenuDTO;
import com.ca.mfd.prc.avi.dto.AviStationDTO;
import com.ca.mfd.prc.avi.dto.TpsCodeSanDTO;
import com.ca.mfd.prc.avi.service.IAviLogicService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.avi.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理
 *
 * @author lwb
 * @since 1.0.0 2023-04-11
 */
@Service
public class AviLogicServiceImpl implements IAviLogicService {

    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    //private IPpsOrderService ppsOrderService;
    PpsOrderProvider ppsOrderProvider;

    @Autowired
    //private IPpsLogicService ppsLogicService;
    PpsEntryProvider ppsEntryProvider;

    @Autowired
    private IAviTrackingRecordService aviTrackingRecordService;

    private PmAllDTO pm = new PmAllDTO();

    @Value("${inkelink.settings.shopCode:}")
    private String settingShopCode;

    private PmAllDTO getObjectedPm() {
        return pmVersionProvider.getObjectedPm();
    }

    private List<SysConfigurationEntity> getSysConfigurations(String category) {
        List<SysConfigurationEntity> configData = sysConfigurationProvider.getSysConfigurations(category);
        if (configData == null) {
            return Lists.newArrayList();
        }
        return configData;
    }

    /**
     * 获取Avi站点数据
     *
     * @param ip 参数IP地址
     * @return 返回一个Avi站点数据
     */
    @Override
    public AviStationDTO getAviStationInfo(String ip) {
        pm = getObjectedPm();
        AviStationDTO aviStationInfo = new AviStationDTO();
        //加载站点信息
        loadAviInfo(aviStationInfo, ip);
        //加载车间信息
        loadShopInfo(aviStationInfo);
        //加载线体信息
        loadAreaInfo(aviStationInfo);
        //加载工位信息
        loadStations(aviStationInfo);
        //加载avi、路由服务器远程访问地址
        loadServerAddress(aviStationInfo);
        //加载菜单配置项
        loadAviMenu(aviStationInfo);
        //加载多点关联通一个屏
        loadRelAvi(aviStationInfo);
        return aviStationInfo;
    }

    /**
     * 加载站点信息
     *
     * @param aviStationInfo 加载实体
     * @param ip             ip地址
     */
    private void loadAviInfo(AviStationDTO aviStationInfo, String ip) {
        PmAviEntity data = getPmAviInfo(ip);
        if (data != null) {
            //aviStationInfo.setId(data.getId());
            aviStationInfo.setId(String.valueOf(data.getId()));
            //aviStationInfo.setCode(data.getCode());
            aviStationInfo.setCode(data.getAviCode());
            //aviStationInfo.setName(data.getName());
            aviStationInfo.setName(data.getAviName());
            //aviStationInfo.setPmShopId(data.getPmShopId());
            aviStationInfo.setPmShopId(data.getPrcPmWorkshopId());
            aviStationInfo.setPmShopCode(data.getWorkshopCode());
            //aviStationInfo.setPmAreaId(data.getPmAreaId());
            aviStationInfo.setPmAreaId(data.getPrcPmLineId());
            aviStationInfo.setIp(data.getIpAddress());
            //aviStationInfo.setMenus(data.getFunctions());
            aviStationInfo.setMenus(data.getAviFunctions());
            aviStationInfo.setDefaultPage(data.getDefaultPage());
        } else {
            throw new InkelinkException("该IP" + ip + "没有配置AVI屏");
        }
    }

    /**
     * 更新车间信息
     *
     * @param aviStationInfo 加载实体
     */
    private void loadShopInfo(AviStationDTO aviStationInfo) {
        List<PmWorkShopEntity> shopslist = pm.getShops();
        if (shopslist == null || shopslist.size() == 0) {
            throw new InkelinkException("未获取到车间信息");
        }
        PmWorkShopEntity data = shopslist.stream()
                .filter(c -> c.getId().equals(aviStationInfo.getPmShopId())).findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("未获取到avi对应的车间信息");
        }
        //aviStationInfo.setPmShopCode(data.getCode());
        aviStationInfo.setPmShopCode(data.getWorkshopCode());
        //aviStationInfo.setPmShopName(data.getName());
        aviStationInfo.setPmShopName(data.getWorkshopName());
    }

    /**
     * 更新线体信息
     *
     * @param aviStationInfo 加载实体
     */
    private void loadAreaInfo(AviStationDTO aviStationInfo) {
        //List<PmLineEntity> areaslist = pm.getAreas();
        List<PmLineEntity> areaslist = pm.getLines();
        if (areaslist == null || areaslist.size() == 0) {
            throw new InkelinkException("未获取到线体信息");
        }
        PmLineEntity data = areaslist.stream()
                .filter(c -> c.getId().equals(aviStationInfo.getPmAreaId())).findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("未获取到对应的线体");
        }
        //aviStationInfo.setPmAreaCode(data.getCode());
        aviStationInfo.setPmAreaCode(data.getLineCode());
        //aviStationInfo.setPmAreaName(data.getName());
        aviStationInfo.setPmAreaName(data.getLineName());
    }

    /**
     * 加载工位列表
     *
     * @param aviStationInfo 加载实体
     */
    private void loadStations(AviStationDTO aviStationInfo) {
        //List<PmLineEntity> areaslist = pm.getAreas();
        List<PmLineEntity> areaslist = pm.getLines();
        if (areaslist == null || areaslist.size() == 0) {
            throw new InkelinkException("未获取到线体信息");
        }
        PmLineEntity data = areaslist.stream()
                .filter(c -> c.getId().equals(aviStationInfo.getPmAreaId())).findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("未获取到对应的线体");
        }
        //List<PmStationEntity> stationslist = pm.getStations();
        List<PmWorkStationEntity> stationslist = pm.getStations();
        if (stationslist == null || stationslist.size() == 0) {
            throw new InkelinkException("未获取到工位集合信息");
        }
        //List<PmWorkStationEntity> stations = stationslist.stream().filter(c -> c.getPmAreaId().equals(data.getId()))
        //.collect(Collectors.toList());
        List<PmWorkStationEntity> stations = stationslist.stream().filter(c -> c.getPrcPmLineId().equals(data.getId()))
                .collect(Collectors.toList());
        List<PmWorkStationEntity> stationsitem = new ArrayList<>();
        stationsitem.addAll(stations);
        stationsitem = stationsitem.stream().sorted(Comparator.comparing(PmWorkStationEntity::getWorkstationCode)).collect(Collectors.toList());
        aviStationInfo.setStationsp(stationsitem);
    }

    /**
     * 加载avi、路由服务器远程访问地址
     *
     * @param aviStationInfo 加载实体
     */
    private void loadServerAddress(AviStationDTO aviStationInfo) {

        String shopCode = settingShopCode;
        if (StringUtils.isBlank(shopCode)) {
            shopCode = aviStationInfo.getPmShopCode();
        }
        List<SysConfigurationEntity> signalrList = getSysConfigurations("SignalrServer");
        if (signalrList == null || signalrList.size() == 0) {
            throw new InkelinkException("未获取获取配置信息");
        }
        String finalShopCode = shopCode;
        SysConfigurationEntity signalrAviUrl = signalrList.stream()
                .filter(c -> (finalShopCode + "_AVIRemoteURL").equals(c.getValue()) || "ALL_AVIRemoteURL".equals(c.getValue())).findFirst().orElse(null);

        aviStationInfo.setSignalrAviUrl(signalrAviUrl == null ? "" : signalrAviUrl.getText());
        SysConfigurationEntity signalrRouteUrl = signalrList.stream()
                .filter(c -> (finalShopCode + "_RouteRemoteURL").equals(c.getValue()) || "ALL_RouteRemoteURL".equals(c.getValue())).findFirst().orElse(null);

        aviStationInfo.setSignalrRouteUrl(signalrRouteUrl == null ? "" : signalrRouteUrl.getText());
        SysConfigurationEntity signalrRouteSmUrl = signalrList.stream()
                .filter(c -> (finalShopCode + "_RouteSmlRemoteURL").equals(c.getValue()) || "ALL_RouteSmlRemoteURL".equals(c.getValue())).findFirst().orElse(null);

        aviStationInfo.setSignalrRouteSmlUrl(signalrRouteSmUrl == null ? "" : signalrRouteSmUrl.getText());
        aviStationInfo.setWebApiAviUrl(aviStationInfo.getSignalrAviUrl() == null ? "" : aviStationInfo.getSignalrAviUrl());
        aviStationInfo.setWebApiRouteUrl(aviStationInfo.getSignalrRouteUrl() == null ? "" : aviStationInfo.getSignalrRouteUrl());
        aviStationInfo.setWebApiRouteSmlUrl(aviStationInfo.getSignalrRouteSmlUrl() == null ? "" : aviStationInfo.getSignalrRouteSmlUrl());
    }

    /**
     * 加载avi站点菜单
     *
     * @param aviStationInfo 加载实体
     */
    private void loadAviMenu(AviStationDTO aviStationInfo) {
        List<AviMenuDTO> aviMenuInfos = new ArrayList<>();
        List<SysConfigurationEntity> configDatas = getSysConfigurations("aviFunction");
        if (aviStationInfo.getMenus().length() > 0) {
            for (SysConfigurationEntity items : configDatas) {
                if (aviStationInfo.getMenus().contains(items.getValue())) {
                    AviMenuDTO aviMenuDTO = new AviMenuDTO();
                    aviMenuDTO.setActionName(items.getValue());
                    aviMenuDTO.setRouterName(items.getText());
                    aviMenuInfos.add(aviMenuDTO);
                }
            }
        }
        aviStationInfo.setAviMenu(aviMenuInfos);
    }

    /**
     * 加载多个AVI点关联到同一个屏幕
     *
     * @param aviStationInfo 加载实体
     */
    private void loadRelAvi(AviStationDTO aviStationInfo) {
        List<SysConfigurationEntity> relAviList = getSysConfigurations("AviRelation");
        if (relAviList == null || relAviList.size() == 0) {
            //throw new InkelinkException("未获取分类集合");
            return;
        }
        SysConfigurationEntity relAvi = relAviList.stream().filter(c -> c.getText().contains(aviStationInfo.getCode()))
                .findFirst().orElse(null);
        if (relAvi != null) {
            List<ComboInfoDTO> relAviCombo = new ArrayList<>();
            // List<PmAviEntity> aviEntityList = pmVersionService.getAllElements(PmAviEntity.class);
            List<PmAviEntity> aviEntityList = pmVersionProvider.getObjectedPm().getAvis();
            if (aviEntityList == null) {
                throw new InkelinkException("未获取avi集合");
            }
            for (String aviCode : relAvi.getText().split(Constant.SEPARATOR_COMMA)) {
                PmAviEntity aviInfo = aviEntityList.stream().filter(m -> m.getAviCode().equals(aviCode))
                        .findFirst().orElse(null);
                if (aviInfo != null) {
                    ComboInfoDTO comboInfoDTO = new ComboInfoDTO();
                    comboInfoDTO.setValue(aviInfo.getIpAddress());
                    //comboInfoDTO.setText(aviInfo.getName());
                    comboInfoDTO.setText(aviInfo.getAviName());
                    relAviCombo.add(comboInfoDTO);
                }
            }
            aviStationInfo.setRelAvi(relAviCombo);
        }
    }

    /**
     * 获取启用的AVI站点
     *
     * @param ip ip 地址
     * @return 返回一个AVI实体
     */
    private PmAviEntity getPmAviInfo(String ip) {
        List<PmAviEntity> aviList = pm.getAvis();
        if (aviList == null || aviList.size() == 0) {
            throw new InkelinkException("AVI列表为空");
        }
        PmAviEntity pmAviInfo = aviList.stream().filter(c -> c.getIpAddress().equals(ip)).findFirst().orElse(null);
        if (pmAviInfo == null) {
            throw new InkelinkException("IP是" + ip + "的不能获取到AVI对象");
        }
        return pmAviInfo;
    }

    /**
     * 车辆识别码打印
     *
     * @param tpsCode  参数TPS 编码
     * @param shopCode 参数车间code
     */
    @Override
    public void tpsCodePrint(String tpsCode, String shopCode) {
        List<ConditionDto> conditionDtos = new ArrayList<>();
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("SN");
        conditionDto.setValue(tpsCode);
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDto.setRelation(ConditionRelation.Or);
        conditionDtos.add(conditionDto);
        ConditionDto conditionDtoSecond = new ConditionDto();
        conditionDtoSecond.setColumnName("BARCODE");
        conditionDtoSecond.setValue(tpsCode);
        conditionDto.setOperator(ConditionOper.Equal);
        conditionDtos.add(conditionDtoSecond);

        List<PpsOrderEntity> orderlist = ppsOrderProvider.getData(conditionDtos);
        if (orderlist == null || orderlist.size() == 0) {
            throw new InkelinkException("未获取对应的订单列表");
        }
        PpsOrderEntity ppsOrderEntity = orderlist.stream().findFirst().orElse(null);
        String code = ppsOrderEntity.getSn();
        ppsEntryProvider.setPrintTpsCode(code, shopCode);
        aviTrackingRecordService.saveChange();
    }

    /**
     * 条码扫描过点
     *
     * @param tpsCodeSanInfo 参数列表
     */
    @Override
    public void tpsCodeScan(TpsCodeSanDTO tpsCodeSanInfo) {
        if (tpsCodeSanInfo == null) {
            throw new InkelinkException("未获取到参数列表");
        }
        if (StringUtils.isBlank(tpsCodeSanInfo.getTpsCode())) {
            throw new InkelinkException("未获取到tpsCode");
        }
        PmAviEntity avis = pm.getAvis().stream().findFirst().orElse(null);
        if (avis == null) {
            throw new InkelinkException("AVI信息不存在");
        }
        //PmWorkShopEntity shop = pm.getShops().stream().filter(s -> s.getId().equals(avis.getPmShopId()))
        // .findFirst().orElse(null);
        PmWorkShopEntity shop = pm.getShops().stream().filter(s -> s.getId().equals(avis.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("车间信息不存在");
        }
        //车辆验证
        String tpsCode = tpsCodeSanInfo.getTpsCode();
        if (StringUtils.isBlank(tpsCode)) {
            throw new InkelinkException("未获取到tpsCode.");
        }
        tpsCodeValidation(tpsCode);
        //车辆过点()
        //aviTrackingRecordService.savePointData(tpsCodeSanInfo.getTpsCode(), tpsCodeSanInfo.getAviId());
        //更新焊装订单为上线状态
        //ppsLogicService.setBodyEntryOnline(tpsCode, shop.getCode());
        ppsEntryProvider.setBodyEntryOnline(tpsCode, shop.getWorkshopCode());
        // todo 检测车辆是否到位

        //下发车辆信息
        PpsOrderEntity vehicleInfo = ppsOrderProvider.getPpsOrderInfo(tpsCodeSanInfo.getTpsCode());
        if (vehicleInfo == null) {
            throw new InkelinkException("TPS码不存在");
        }
        //todo 验证重复下发
        aviTrackingRecordService.saveChange();
    }


    private void tpsCodeValidation(String tpsCode) {
        //List<BodyVehicleDTO> bodyVehicleData = ppsLogicService.getPrintTpscode();
        List<BodyVehicleDTO> bodyVehicleData = ppsEntryProvider.getPrintTpscode();
        BodyVehicleDTO bodyVehicleDTO = bodyVehicleData.stream().findFirst().orElse(null);
        if (bodyVehicleDTO == null) {
            throw new InkelinkException("录入条码" + tpsCode + ",没有打印");
        }
        if (!tpsCode.equals(bodyVehicleDTO.getTpsCode())) {
            throw new InkelinkException("上线车辆队列顺序不符！");
        }
    }

}
