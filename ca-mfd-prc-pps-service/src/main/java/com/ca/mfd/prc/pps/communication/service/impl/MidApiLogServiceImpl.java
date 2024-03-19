package com.ca.mfd.prc.pps.communication.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.AsBatchPiecesDto;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
import com.ca.mfd.prc.pps.communication.dto.AsOrderScrapDto;
import com.ca.mfd.prc.pps.communication.dto.AsQueueStartDto;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.BomMaterialUseDto;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.remote.app.pm.entity.MidColorBaseEntity;
import com.ca.mfd.prc.pps.communication.remote.app.pm.provider.PmCommunicationProvider;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanService;
import com.ca.mfd.prc.pps.communication.service.IMidBomMaterialUseService;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleMasterService;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidApiLogServiceImpl extends MidApiLogBaseServiceImpl implements IMidApiLogService {

    private static final Logger logger = LoggerFactory.getLogger(MidApiLogServiceImpl.class);


    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IPpsAsAviPointService ppsAsAviPointService;

    @Autowired
    private IMidAsBathPlanService midAsBathPlanService;

    @Autowired
    private PmOrgProvider pmOrgProvider;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IMidVehicleMasterService vehicleModelService;
    @Autowired
    private IMidBomMaterialUseService bomMaterialUseService;
    @Autowired
    private PmCommunicationProvider pmCommunicationProvider;
    @Autowired
    private PmProductBomVersionsProvider pmProductBomVersionsProvider;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsService;

    public Integer getBomMode(Integer bomMode) {
        if(bomMode!=null){
            return bomMode;
        }
        //0：本地模式，1 ：接口模式
        String model = sysConfigurationProvider.getConfiguration("bom_model", "midapi");
        if (StringUtils.isBlank(model)) {
            return 0;
        } else {
            return Integer.valueOf(model.trim());
        }
    }

    public String getWhitProductCode(){
        return sysConfigurationProvider.getConfiguration("white_product_code","midapi");
    }

    public List<String> getProductCodeStartsWith() {
        String startCodes = sysConfigurationProvider.getConfiguration("start_product_code", "midapi");
        if (StringUtils.isBlank(startCodes)) {
            return new ArrayList<>();
        }
        return Arrays.asList(startCodes.split(","));
    }

    private String getBomVersionCfg(){
       return sysConfigurationProvider.getConfiguration("bom_version","midapi");
    }

    private String getBomPartVersionCfg(){
        return sysConfigurationProvider.getConfiguration("bompart_version","midapi");
    }

    private String getCharacteristicsVersionCfg(){
        return sysConfigurationProvider.getConfiguration("characteristics_version","midapi");
    }

    /**
     * 根据整车物料号获取bom版本号
     *
     * @param materialNo
     * @return
     */
    @Override
    public String getBomVersion(Integer bomMode,String plantCode,String materialNo,String specifyDate) {
        if (getBomMode(bomMode) == 0) {
            return pmProductBomVersionsProvider.copyBom(materialNo, getBomVersionCfg());
        } else {
            String bomsVersion = "";
            try {
                //从bom 获取整车BOM
                bomsVersion = pmCommunicationProvider.getBomVersion(plantCode, materialNo, specifyDate);
            } catch (Exception e) {
                logger.error("从bom获取整车bom失败", e);
            }
            if (StringUtils.isBlank(bomsVersion)) {
                //3 匹配BOM和本地，  2 只匹配BOM ，1 假四段码使用本地  0 本地
                if (bomMode == 3 || bomMode == 1) {
                    //从本地获取
                    bomsVersion = pmProductBomVersionsProvider.getBomVersions(materialNo);
                } else {
                    throw new InkelinkException("从bom获取整车bom失败,物料号：" + materialNo);
                }
            }
            if (StringUtils.isBlank(bomsVersion)) {
                throw new InkelinkException("bom获取整车bom失败,物料号：" + materialNo);
            }
            return bomsVersion;
        }
    }

    /**
     * 根据整车物料号获取特征版本号
     *
     * @param materialNo
     * @return
     */
    @Override
    public String getCharacteristicsVersion(Integer bomMode,String materialNo) {
        if (getBomMode(bomMode) == 0) {
            return pmProductCharacteristicsVersionsService.copyCharacteristics(materialNo, getCharacteristicsVersionCfg());
        } else {
            String characteristicsVersion = "";
            try {
                //从bom 获取整车特征
                characteristicsVersion = pmCommunicationProvider.getCharacteristicsVersion(materialNo);
            } catch (Exception e) {
                logger.error("从bom获取整车特征失败", e);
            }
            if (StringUtils.isBlank(characteristicsVersion)) {
                //3 匹配BOM和本地，  2 只匹配BOM ，1 假四段码使用本地  0 本地
                if (bomMode == 3 || bomMode == 1) {
                    //从本地获取
                    characteristicsVersion = pmProductCharacteristicsVersionsService.getCharacteristicsVersions(materialNo);
                } else {
                    throw new InkelinkException("从bom获取整车特征失败,物料号：" + materialNo);
                }
            }
            if (StringUtils.isBlank(characteristicsVersion)) {
                throw new InkelinkException("bom获取整车特征失败,物料号：" + materialNo);
            }
            return characteristicsVersion;
        }
    }

    /**
     * 根据零件物料号获取零件bom版本号
     *
     * @param materialNo
     * @return
     */
    @Override
    public String getBomPartVersion(Integer bomMode,String materialNo) {
        if (getBomMode(bomMode) == 0) {
            return pmProductBomVersionsProvider.copyBom(materialNo, getBomPartVersionCfg());
        } else {
            String bomsVersion = "";
            try {
                String orgCode = pmOrgProvider.getCurrentOrgCode();
                String specifyDate = DateUtils.format(new Date(),DateUtils.DATE_PATTERN);
                //从bom 获取零件BOM
                bomsVersion = pmCommunicationProvider.getBomPartVersion(materialNo,orgCode,specifyDate);
            } catch (Exception e) {
                logger.error("从bom获取零件bom失败", e);
            }
            if (StringUtils.isBlank(bomsVersion)) {
                //3 匹配BOM和本地，  2 只匹配BOM ，1 假四段码使用本地  0 本地
                if (bomMode == 3 || bomMode == 1) {
                    //从本地获取
                     bomsVersion = pmProductBomVersionsProvider.getBomVersions(materialNo);
                }else{
                    throw new InkelinkException("获取零件bom失败,物料号：" + materialNo);
                }
            }
            if (StringUtils.isBlank(bomsVersion)) {
                throw new InkelinkException("零件bom获取失败,物料号：" + materialNo);
            }
            return bomsVersion;
        }
    }

    /**
     * 获取颜色
     *
     * @param colorCode
     * @return
     */
    @Override
    public List<MidColorBaseEntity> getByClorCode(String colorCode) {
        return pmCommunicationProvider.getByClorCode(colorCode);
    }

    private String getColorCode(String[] productCodes){
        if(productCodes==null){
            return "";
        }
        return productCodes.length > 2 ? com.ca.mfd.prc.common.utils.StringUtils.trimEnd(productCodes[2], ".") : "";
    }

    private List<SysConfigurationEntity> bomSpecCfg = null;

    private String getCharacteristicsValue( List<String> characteristics,String key) {
        String outKey = bomSpecCfg == null ? "" : bomSpecCfg.stream()
                .filter(c -> StringUtils.equals(key, c.getValue()))
                .findFirst().orElse(new SysConfigurationEntity()).getText();
        if (!StringUtils.isBlank(outKey)) {
            List<String> outColorChars = characteristics.stream().filter(c -> c != null && c.startsWith(outKey)).collect(Collectors.toList());
            String outColorCn = String.join("、", outColorChars);
            return outColorCn == null ? "" : outColorCn;
        }
        return "";
    }

    /**
     * 获取天窗类型(配置：window_type_S;TRE001 。window_type_N;TRE002,TRE003 )
     *
     * */
    private String getWindowByCharacteristics(List<String> characteristics) {
        List<SysConfigurationEntity> wincfg = bomSpecCfg.stream()
                .filter(c -> StringUtils.startsWith(c.getValue(), "window_type_"))
                .collect(Collectors.toList());
        String winCode = "";
        for (SysConfigurationEntity cfg : wincfg) {
            if (StringUtils.isBlank(cfg.getText())) {
                continue;
            }
            List<String> outKeys = Arrays.asList(cfg.getText().split(","));
            if (characteristics.stream().filter(c -> c != null && outKeys.contains(c)).count() > 0) {
                winCode = cfg.getValue().replace("window_type_", "").trim();
                break;
            }
        }
        return winCode;
    }

    /**
     * 设置 选装包、车身色，内饰色,基础车型
     *
     * */
    @Override
    public void setVeExtendInfo(PpsPlanEntity et) {
        //bom 特征配置
        if(bomSpecCfg==null) {
            bomSpecCfg = sysConfigurationProvider.getSysConfigurations("BOM_SPEC");
        }
        VehicleModelDto vehicleModel = null;
        String proCode = et.getProductCode();
        try {
            //if(StringUtils.equals(et.getAttribute2(),"2")){
            vehicleModel = getBomVehicleModel(proCode);
        } catch (Exception e) {
            logger.error("从bom获取整车车辆信息失败", e);
        }
        if (vehicleModel != null) {
            et.setCharacteristic1(vehicleModel.getBomRoom());
            et.setModel(vehicleModel.getBomRoom());
        }
        if(StringUtils.isBlank(et.getModel())) {
            throw new InkelinkException("整车物料号：" + proCode + "没有找到车辆基础信息");
        }
        //SC6501AAABEV.CNH1001-L.+0000001.WW1
        //SC6501AAABEV.CNH1001-L+00002.WW1
        String[] productCodes = et.getProductCode().split("\\.");
        //颜色标识 6
        String colorCode = getColorCode(productCodes);
        et.setCharacteristic6(colorCode);
        //车辆状态码 7
        String midCode = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(productCodes[1], ".");
        String stateCode = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(midCode.split("\\+")[0], "+");
        et.setCharacteristic7(stateCode);
        //双色车标识 8
        String doubleColor = "0";
        if (StringUtils.isNotBlank(colorCode)) {
            String colorMd = colorCode.trim().toUpperCase();
            Boolean isUpper = true;
            for (char ar : colorMd.toCharArray()) {
                if (ar < 'A' || ar > 'Z') {
                    isUpper = false;
                }
            }
            doubleColor = isUpper ? "1" : "0";
        }
        et.setCharacteristic8(doubleColor);
        et.setCharacteristic9("");

        Integer selectType = 1;
        if (selectType == 1) {
            List<PmProductCharacteristicsEntity> characteristDatas = null;
            try {
                characteristDatas = pmProductCharacteristicsVersionsService.getCharacteristicsData(et.getProductCode(), et.getCharacteristicVersion());
            } catch (Exception e) {
                logger.error("获取特征明细数据", e);
            }
            if (characteristDatas != null && !characteristDatas.isEmpty()) {
                //整车特征
                List<String> characteristics = characteristDatas.stream().map(c -> c.getProductCharacteristicsValue()).distinct().collect(Collectors.toList());

                //外观颜色 AAT
                et.setCharacteristic2(getCharacteristicsValue(characteristics,"out_color"));
                //内饰风格 AAQ
                et.setCharacteristic3(getCharacteristicsValue(characteristics,"inside_color"));
                //选装包特征
                et.setAttribute1(getCharacteristicsValue(characteristics,"T"));
                //天窗 9  天窗类型 RAK  N-无，S-小天窗，A-全景
                et.setCharacteristic9(getWindowByCharacteristics(characteristics));
            } else {
                logger.error("获取特征信息失败:整车物料号," + et.getProductCode());
            }
        } else {
            if (vehicleModel == null) {
                logger.error("从bom获取整车车辆信息失败:整车物料号," + et.getProductCode());
                return;
            }
            //通过整车物料号解析
            et.setAttribute1(vehicleModel.getOptionalPackageList());
            String clorCode = getColorCode(productCodes);
            if (!StringUtils.isBlank(clorCode)) {
                //根据颜色代码获取颜色
                List<MidColorBaseEntity> colors = new ArrayList<>();
                try {
                    colors = getByClorCode(clorCode);
                } catch (Exception e) {
                    logger.error("从bom获取颜色信息失败", e);
                }
                if (colors != null && !colors.isEmpty()) {
                    //TODO 确认结构
                    MidColorBaseEntity inClor = colors.stream().filter(c -> StringUtils.equals(c.getIaed(), "内饰"))
                            .findFirst().orElse(null);
                    MidColorBaseEntity outClor = colors.stream().filter(c -> StringUtils.equals(c.getIaed(), "外饰"))
                            .findFirst().orElse(null);
                    et.setCharacteristic2(outClor == null ? "" : outClor.getColorName());
                    et.setCharacteristic3(inClor == null ? "" : inClor.getColorName());
                }
            }
        }
    }

    /**
     * 根据整车物料号获取最新车型信息(bom提供)
     *
     * @param materialNo 整车物料号
     * @return
     */
    @Override
    public VehicleModelDto getBomVehicleModel(String materialNo) {
        List<VehicleModelDto> list = vehicleModelService.getVehicleModelData(materialNo);
        if (list == null || list.isEmpty()) {
            return null;
        }
        VehicleModelDto data = list.stream().filter(c -> c.getEffectiveFrom() == null)
                .findFirst().orElse(null);
        if (data != null) {
            return data;
        }
        //取最新的一条
        return list.stream().sorted(Comparator.comparing(VehicleModelDto::getEffectiveFrom).reversed())
                .findFirst().orElse(null);
    }

    /**
     * 根据零件物料号获取整车物料号(bom提供)
     *
     * @param materialNo 零件物料号
     * @return 整车物料号
     */
    @Override
    public String getProduceCodeByMaterialNo(Integer bomMode,String materialNo,String plantCode,String specifyDate,String orderCategory) {
        String productCode = "";
        //整车查询
        if (StringUtils.equals("1", orderCategory) && getBomMode(bomMode) != 0) {
            List<BomMaterialUseDto> list = new ArrayList<>();
            try {
                list = bomMaterialUseService.getBomMaterialUseData(materialNo, plantCode, specifyDate);
            } catch (Exception e) {
                logger.error("", e);
            }
            if (list != null && !list.isEmpty()) {
                BomMaterialUseDto data = list.stream().filter(c -> CollectionUtils.isNotEmpty(c.getVehicleMaterialNumbers()))
                        .findFirst().orElse(null);
                productCode = data == null ? "" : data.getVehicleMaterialNumbers().get(0).toString();
            }
        }
        if (StringUtils.isBlank(productCode)) {
            //从本地获取(前提 是必须先拉取到整车bom数据)
           /* PmProductBomVersionsEntity productBomVersion = pmProductBomVersionsProvider.getVersionByMaterialNo(materialNo,orderCategory);
            productCode = productBomVersion == null ? "" : productBomVersion.getProductMaterialNo();*/
        }
        return productCode;
    }

    /**
     * 发送AS订单报废
     */
    @Override
    public ResultVO<String> sendOrderScrap(List<AsOrderScrapDto> fbacks) {

        String reqNo = UUIDUtils.getGuid();
        logger.info("AS报废车辆记录[" + reqNo + "]开始:");

        logger.info("AS报废车辆记录[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("asorderscrap_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[asorderscrap_send]");
        }
        String orgCode = pmOrgProvider.getCurrentOrgCode();

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_ORDER_SCRAP);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            this.insert(loginfo);
            this.saveChange();

            loginfo.setDataLineNo(fbacks.size());
            for (AsOrderScrapDto et : fbacks) {
                et.setOrgCode(orgCode);
            }
            String sendData = JsonUtils.toJsonString(fbacks);
            loginfo.setReqData(sendData);
            String ars = apiPtService.postapi(apiPath, fbacks, null);
            loginfo.setResponseData(ars);
            try {
                ////rowCount
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JsonUtils.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "AS报废车辆记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception eas) {
                logger.error("", eas);
                status = 1;
                errMsg = "AS报废车辆记录[" + reqNo + "]处理失败:";
                logger.info(errMsg);
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = "AS报废车辆记录[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            this.clearChange();
        }

        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        this.update(loginfo);
        this.saveChange();

        logger.info("AS报废车辆记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    /**
     * 发送AS批次进度反馈
     */
    @Override
    public  List<AsBatchPiecesDto> getAsBatchPieces(List<PpsAsAviPointEntity> list) {
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        List<String> plannos = list.stream().map(PpsAsAviPointEntity::getPlanNo).distinct().collect(Collectors.toList());
        List<AsBatchPiecesDto> baths = new ArrayList<>();
        if (plannos == null || plannos.isEmpty()) {
            return baths;
        }
        List<MidAsBathPlanEntity> asBathPlans = midAsBathPlanService.getByPlanNos(plannos);

        for (String planNo : plannos) {
            List<PpsAsAviPointEntity> olds = list.stream().filter(c -> StringUtils.equals(c.getPlanNo(), planNo))
                    .collect(Collectors.toList());
            /*for (PpsAsAviPointEntity et : olds) {
                if (et.getAviCode() != null && et.getAviCode().indexOf('#') >= 0) {
                    et.setAviCode(et.getAviCode().split("#")[0]);
                }
            }*/
            // String orderCategory = olds.get(0).getOrderCategory();
            //分组排重  计划号：上线下线标识：工位代码
            Map<String, List<MidAsBathPlanEntity>> asPlans = asBathPlans.stream().filter(c -> StringUtils.equals(c.getAttribute3(), planNo))
                    .sorted(Comparator.comparing(MidAsBathPlanEntity::getCreationDate).reversed())
                    .collect(Collectors.groupingBy(c -> c.getAttribute3() + ":" + c.getWsFlag() + ":" + c.getAttribute2()));
            if (!asPlans.isEmpty()) {
                for (Map.Entry<String, List<MidAsBathPlanEntity>> mp : asPlans.entrySet()) {
                    MidAsBathPlanEntity item = mp.getValue().get(0);
                    String aviCode = item.getAttribute2();

                    List<MidAsBathPlanEntity> planAvis = asBathPlans.stream().filter(c -> StringUtils.equals(c.getAttribute3(), planNo))
                            .collect(Collectors.toList());
                    //查找上报点
                    PmAviEntity avi = pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), aviCode))
                            .findFirst().orElse(null);
                    String tpCode = StringUtils.isBlank(avi.getDb3()) ? avi.getAviCode() : avi.getDb3();
                    MidAsBathPlanEntity planAviInfo = planAvis.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getWsCode(), tpCode))
                            .findFirst().orElse(null);
                    if (planAviInfo == null) {
                        /*btplan.setAsSendFlag(3);
                        bathUpdate.add(btplan);*/
                        continue;
                    }
                    //判断AVI点是否已经报工？ 可以重复报工 TODO
                    List<PpsAsAviPointEntity> aviPoints = olds.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), aviCode))
                            .sorted(Comparator.comparing(PpsAsAviPointEntity::getCreationDate).reversed())
                            .collect(Collectors.toList());
                    if (aviPoints != null && !aviPoints.isEmpty()) {
                        for (PpsAsAviPointEntity aviPoint : aviPoints) {
                            AsBatchPiecesDto info = new AsBatchPiecesDto();
                            info.setTaskCode(item.getProcessTaskCode());
                            info.setOrgCode(aviPoint.getOrgCode());
                            info.setShopCode(aviPoint.getWorkshopCode());
                            info.setLineCode(aviPoint.getLineCode());
                            info.setWsCode(item.getWsCode());
                            info.setWsFlag(item.getWsFlag());

                            info.setYieldQuantity(aviPoint.getQualifiedCount());
                            info.setDefectQuantity(aviPoint.getBadCount());
                            info.setLotStatus(2);
                            info.setActualStartTime(aviPoint.getScanTime());
                            info.setActualEndTime(aviPoint.getScanTime());
                            info.setAttr1(aviPoint.getAttribute1());
                            info.setAttr2(aviPoint.getAttribute2());
                            info.setAttr3(aviPoint.getAttribute3());
                            info.setAttr4(aviPoint.getAttribute4());
                            baths.add(info);
                        }
                    }
                }
            }
        }
        return baths;
    }

    public  PmAviEntity getAviByAsTpCode(PmAllDTO pmall,String asTpCode) {
        PmAviEntity avi = pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getDb3(), asTpCode))
                .findFirst().orElse(null);
        if (avi != null) {
            return avi;
        }
        avi = pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), asTpCode))
                .findFirst().orElse(null);
        return avi;
    }

    /**
     * 发送AS保留车
     */
    @Override
    public ResultVO<String> sendKeepCar(List<AsKeepCarDto> fbacks) {

        String reqNo = UUIDUtils.getGuid();
        logger.info("AS保留车辆记录[" + reqNo + "]开始:");

        logger.info("AS保留车辆记录[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("askeepcar_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[askeepcar_send]");
        }
        String orgCode = pmOrgProvider.getCurrentOrgCode();

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_KEEP_CAR);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());

        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            this.insert(loginfo);
            this.saveChange();
            loginfo.setDataLineNo(fbacks.size());

            for (AsKeepCarDto et : fbacks) {
                et.setOrgCode(orgCode);
            }
            String sendData = JsonUtils.toJsonString(fbacks);
            loginfo.setReqData(sendData);
            String ars = apiPtService.postapi(apiPath, fbacks, null);
            loginfo.setResponseData(ars);
            try {
                ////rowCount
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JsonUtils.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "AS保留车辆记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception eas) {
                logger.error("", eas);
                status = 1;
                errMsg = "AS保留车辆记录[" + reqNo + "]处理失败:";
                logger.info(errMsg);
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = "AS保留车辆记录[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            this.clearChange();
        }

        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        this.update(loginfo);
        this.saveChange();

        logger.info("AS保留车辆记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    /**
     * 发送AS待开工队列
     *
     * @param fbacks 发送数据
     * @return 处理结果
     */
    @Override
    public ResultVO<String> sendQueueStart(List<AsQueueStartDto> fbacks) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS待开工队列[" + reqNo + "]开始:");

        logger.info("AS待开工队列[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.isEmpty()) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("asqueuestart_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[asqueuestart_send]");
        }
        String orgCode = pmOrgProvider.getCurrentOrgCode();
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_QUEUE_START);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            loginfo.setRemark(reqNo);
            this.insert(loginfo);
            this.saveChange();
            loginfo.setDataLineNo(fbacks.size());
            for (AsQueueStartDto et : fbacks) {
                et.setOrgCode(orgCode);
            }
            String sendData = JsonUtils.toJsonString(fbacks);
            loginfo.setReqData(sendData);
            String ars = apiPtService.postapi(apiPath, fbacks, null);
            loginfo.setResponseData(ars);
            try {
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JsonUtils.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "AS待开工队列[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception eas) {
                logger.error("", eas);
                status = 1;
                errMsg = "AS待开工队列[" + reqNo + "]处理失败:";
                logger.info(errMsg);
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = "AS待开工队列[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            this.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        this.update(loginfo);
        this.saveChange();
        logger.info("AS待开工队列[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    /**
     * 发送LMS整车计划锁定
     *
     * @param fbacks 发送数据
     * @return 处理结果
     */
    @Override
    public ResultVO<String> sendLmsLockPlan(List<LmsLockPlanDto> fbacks) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("LMS整车计划锁定[" + reqNo + "]开始:");
        logger.info("LMS整车计划锁定[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.isEmpty()) {
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("lmslockplan_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[lmslockplan_send]");
        }
        String orgCode = pmOrgProvider.getCurrentOrgCode();
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_LOCKPLAN);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            loginfo.setRemark(reqNo);
            this.insert(loginfo);
            this.saveChange();

            //完善数据
            for (LmsLockPlanDto dt : fbacks) {
                dt.setOrgCode(orgCode);
                String aviCode = dt.getAviCode();
                String vin = dt.getVin();
                if (!StringUtils.isBlank(aviCode)) {
                    String finalAviCode = aviCode;
                    PmAviEntity aviInfo = pmAllDTO.getAvis().stream().filter(s ->
                            StringUtils.equals(s.getAviCode(), finalAviCode)).findFirst().orElse(null);
                    if (aviInfo == null) {
                        errMsg = "发送Lms整车锁定计划异常,查询avi站点异常,aviCode::" + aviCode + ",vin码:" + vin;
                        throw new InkelinkException(errMsg);
                    }
                    PmLineEntity lineEntity = pmAllDTO.getLines().stream().filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
                    if (lineEntity == null) {
                        errMsg = "发送Lms整车锁定计划异常,查询线体异常,aviCode::" + aviCode + ",vin码:" + vin;
                        throw new InkelinkException(errMsg);
                    }
                    String llineCode = lineEntity.getLineCode();
                    PmWorkShopEntity workShopEntity = pmAllDTO.getShops().stream().filter(s -> Objects.equals(s.getId(), lineEntity.getPrcPmWorkshopId())).findFirst().orElse(null);
                    if (workShopEntity == null) {
                        errMsg = "发送Lms整车锁定计划异常,查询车间异常,aviCode::" + aviCode + ",vin码:" + vin;
                        throw new InkelinkException(errMsg);
                    }
                    String shopCode = workShopEntity.getWorkshopCode();
                    dt.setOrgCode(orgCode);
                    dt.setWorkshopCode(shopCode);
                    dt.setLineCode(llineCode);
                }
            }

            loginfo.setDataLineNo(fbacks.size());
            String sendData = JsonUtils.toJsonString(fbacks);
            loginfo.setReqData(sendData);
            String ars = apiPtService.postapi(apiPath, fbacks, null);
            loginfo.setResponseData(ars);
            try {
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JsonUtils.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "LMS整车计划锁定[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception eas) {
                logger.error("", eas);
                status = 1;
                errMsg = "LMS整车计划锁定[" + reqNo + "]处理失败:";
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "LMS整车计划锁定[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            this.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        this.update(loginfo);
        this.saveChange();
        logger.info("LMS整车计划锁定[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    @Override
    public ResultVO<String> sendLmsLockPlanBak(List<LmsLockPlanDto> fbacks) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("LMS整车计划锁定[" + reqNo + "]开始:");
        logger.info("LMS整车计划锁定[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.isEmpty()) {
            throw new InkelinkException("没有需要上报的数据");
        }
        int status = 1;
        String errMsg = "";
        String apiPath="";
        String ars = apiPtService.postapi(apiPath, fbacks, null);
        AsResultVo asResultVo = JsonUtils.parseObject(ars, AsResultVo.class);
        //        ResponseEntity<ResultVO> responseEntity = null;
        //        responseEntity =  restTemplate.postForEntity(apiPath, JSON.toJSONString(fbacks), ResultVO.class);
        if (asResultVo != null) {
            if (StringUtils.equals("1", asResultVo.getCode())) {
                status = 1;
            } else {
                status = 5;
                errMsg = "LMS整车计划锁定[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                logger.info(errMsg);
            }
        }
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }
}