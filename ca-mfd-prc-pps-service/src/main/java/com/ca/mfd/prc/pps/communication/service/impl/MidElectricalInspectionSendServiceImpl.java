package com.ca.mfd.prc.pps.communication.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.pps.communication.dto.*;
import com.ca.mfd.prc.pps.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.pps.communication.service.IMidElectricalInspectionSendService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomConfigService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomListService;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleMasterService;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.pm.IPmOrgService;
import com.ca.mfd.prc.pps.remote.app.pm.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Description: 配置字版本服务实现
 * @author inkelink
 * @date 2023年11月27日
 * @变更说明 BY inkelink At 2023年11月27日
 */
@Service
public class MidElectricalInspectionSendServiceImpl implements IMidElectricalInspectionSendService {

    @Autowired
    private IPpsOrderService ppsOrderService;

    @Autowired
    private IPpsPlanService ppsPlanService;

    @Autowired
    private IMidSoftwareBomListService bomListService;

    @Autowired
    private IMidSoftwareBomConfigService bomConfigService;

    @Autowired
    private IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;

    @Autowired
    private IMidVehicleMasterService vehicleMasterService;

    @Autowired
    private IPmOrgService pmOrgService;

    @Override
    public EcuCarInfoDto getEcuCarInfoByVin(String vin) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("sn", vin, ConditionOper.Equal, ConditionRelation.Or));
        dtos.add(new ConditionDto("barcode", vin, ConditionOper.Equal, ConditionRelation.Or));
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getData(dtos).stream().findFirst().orElse(null);
        if (ppsOrderEntity == null) {
            throw new InkelinkException("未查到车辆订单数据");
        }
        PpsPlanEntity firstByPlanNo = ppsPlanService.getFirstByPlanNo(ppsOrderEntity.getPlanNo());
        if (firstByPlanNo == null) {
            throw new InkelinkException("未查到计划数据");
        }

        //获取软件配置字
        //List<BomConfigDto> bomConfig = bomConfigService.getBomConfig(firstByPlanNo.getProductCode(), DateUtils.format(firstByPlanNo.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
        List<BomConfigDto> bomConfig = bomConfigService.getBomConfig(firstByPlanNo.getProductCode(), DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        if(CollectionUtils.isEmpty(bomConfig)){
            throw new InkelinkException("未取软到件配置字");
        }
        //获取软件清单
        //List<SoftwareBomListDto> softwareBomListDto = bomListService.getSoftBom(firstByPlanNo.getProductCode(),DateUtils.format(firstByPlanNo.getEstimatedStartDt(),DateUtils.DATE_PATTERN));
        List<SoftwareBomListDto> softwareBomListDto = bomListService.getSoftBom(firstByPlanNo.getProductCode(),DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
        if(CollectionUtils.isEmpty(softwareBomListDto)){
            throw new InkelinkException("未获取到软件清单");
        }

        List<EcuSoftwareListDto> ecuList=new ArrayList<>();
        softwareBomListDto.forEach(x->{
            EcuSoftwareListDto listDto=new EcuSoftwareListDto();
            listDto.setEcuTypeCode(x.getEcuTypeCode());
            listDto.setTargetIp(x.getTargetIp());
            listDto.setLogicalAddress(x.getLogicalAddress());
            listDto.setPublishChangeCode(x.getPublishChangeCode());
            listDto.setSoftwareCode(x.getSoftwareCode());
            listDto.setSoftwareVersion(x.getSoftwareVersion());
            listDto.setIsCheckSwVersion(x.getIsCheckSwVersion()?"1":"0");
            listDto.setHardwarePartNo(x.getHardwarePartNo());
            listDto.setDeploymentLocation(x.getDeploymentLocation());
            listDto.setSoftwarePackage(x.getSoftwarePackage());
            listDto.setIsSupportOta(x.getIsSupportOta()?"1":"0");
            listDto.setIsOnlineWritten(x.getIsOnlineWritten()?"1":"0");
            listDto.setIsSupportDiaWritten(x.getIsSupportDiaWritten()?"1":"0");
            listDto.setSoftwarePartNumberDid(x.getSoftwarePartNumberDid());
            listDto.setSoftwareVersionNumberDid(x.getSoftwareVersionNumberDid());
            listDto.setDiagnosticReqId(x.getDiagnosticReqId());
            listDto.setDiagnosticResId(x.getDiagnosticResId());
            listDto.setLastVehicleVersionCode(x.getLastVehicleVersionCode());
            List<SoftwareConfigDto> collect = bomConfig.get(0).getSoftwareConfigList().stream().filter(w -> w.getEcuCode().equals(x.getEcuTypeCode())).collect(Collectors.toList());
            listDto.setChangeCode(bomConfig.get(0).getChangeCode());
            List<EcuSoftwareDidDto> ecuConf =new ArrayList<>();
            collect.stream().forEach(v->{
                ecuConf.addAll(v.getEcuConfigList());
            });
            listDto.setEcuConf(ecuConf);
            ecuList.add(listDto);
        });

        //组装ecucarinfo数据
        EcuCarInfoDto infoDto=new EcuCarInfoDto();
        infoDto.setVinCode(vin);
        infoDto.setTypeCode(firstByPlanNo.getModel());
        String[] str=firstByPlanNo.getProductCode().split("\\.");
        if(str!=null){
            infoDto.setCarCode(str[0]);
        }
        infoDto.setOptionCode(firstByPlanNo.getCharacteristic7());
        infoDto.setAttributeCode(firstByPlanNo.getOrderSign());
        infoDto.setEcuList(ecuList);
        infoDto.setCreationDate(new Date());
        infoDto.setLastUpdateDate(new Date());
        return infoDto;
    }

    @Override
    public CarInfoDto getCarInfoByVin(String vin) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("sn", vin, ConditionOper.Equal, ConditionRelation.Or));
        dtos.add(new ConditionDto("barcode", vin, ConditionOper.Equal, ConditionRelation.Or));
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getData(dtos).stream().findFirst().orElse(null);
        if (ppsOrderEntity == null) {
            throw new InkelinkException("未查到车辆订单数据");
        }
        PpsPlanEntity firstByPlanNo = ppsPlanService.getFirstByPlanNo(ppsOrderEntity.getPlanNo());
        if (firstByPlanNo == null) {
            throw new InkelinkException("未查到计划数据");
        }
        CarInfoDto infoDto=new CarInfoDto();

        //根据整车物料号和型号查询车型
        MidVehicleMasterEntity masterEntity= vehicleMasterService.getVehicleMasterByParam(ppsOrderEntity.getProductCode(),ppsOrderEntity.getModel());
        if(masterEntity!=null){
            infoDto.setBasicVehicleModel(masterEntity.getBasicVehicleModel());
        }
        //获取特征值
        List<String> characteristics = pmProductCharacteristicsVersionsService.getCharacteristicsData(firstByPlanNo.getProductCode(), firstByPlanNo.getCharacteristicVersion()).getData().stream().map(PmProductCharacteristicsEntity::getProductCharacteristicsValue)
                .collect(Collectors.toList());

        //组装ecucarinfo数据
        infoDto.setVinCode(vin);
        infoDto.setTypeCode(firstByPlanNo.getModel());
        String[] str=firstByPlanNo.getProductCode().split("\\.");
        if(str!=null){
            infoDto.setCarCode(str[0]);
        }
        infoDto.setOptionCode(firstByPlanNo.getCharacteristic7());
        infoDto.setAttributeCode(firstByPlanNo.getOrderSign());
        infoDto.setColorCode(firstByPlanNo.getCharacteristic6());
        infoDto.setOrgCode(pmOrgService.getCurrentOrgCode().getData());
        infoDto.setVehicleMaterialNumber(firstByPlanNo.getProductCode());
        infoDto.setFeatureCode(String.join(",",characteristics));
        infoDto.setCreationDate(new Date());
        return infoDto;
    }

    @Override
    public SiteInfoDto getSiteInfoByVin(String vin) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("sn", vin, ConditionOper.Equal, ConditionRelation.Or));
        dtos.add(new ConditionDto("barcode", vin, ConditionOper.Equal, ConditionRelation.Or));
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getData(dtos).stream().findFirst().orElse(null);
        if (ppsOrderEntity == null) {
            throw new InkelinkException("未查到车辆订单数据");
        }
        PpsPlanEntity firstByPlanNo = ppsPlanService.getFirstByPlanNo(ppsOrderEntity.getPlanNo());
        if (firstByPlanNo == null) {
            throw new InkelinkException("未查到计划数据");
        }


        //组装ecucarinfo数据
        SiteInfoDto infoDto=new SiteInfoDto();
        infoDto.setVinCode(vin);
        infoDto.setTypeCode(firstByPlanNo.getModel());
        infoDto.setCarCode(firstByPlanNo.getCharacteristic7());
        infoDto.setOptionCode(firstByPlanNo.getAttribute1());
        infoDto.setAttributeCode(firstByPlanNo.getOrderSign());
        infoDto.setColorCode(firstByPlanNo.getCharacteristic6());
        infoDto.setSiteInformation("");
        infoDto.setCreationDate(new Date());
        return infoDto;
    }

    @Override
    public EcuCarInfoDto getEcuCarInfoByVinTest(MidDjTestSendEntity dto) {
        //获取软件配置字
        List<BomConfigDto> bomConfig = bomConfigService.getBomConfig(dto.getVehicleMaterialNumbers(), DateUtils.format(dto.getSpecifyDate(), DateUtils.DATE_PATTERN));
        if(CollectionUtils.isEmpty(bomConfig)){
            throw new InkelinkException("未取软到件配置字");
        }
        //获取软件清单
        List<SoftwareBomListDto> softwareBomListDto = bomListService.getSoftBom(dto.getVehicleMaterialNumbers(),DateUtils.format(dto.getSpecifyDate(),DateUtils.DATE_PATTERN));
        if(CollectionUtils.isEmpty(softwareBomListDto)){
            throw new InkelinkException("未获取到软件清单");
        }

        List<EcuSoftwareListDto> ecuList=new ArrayList<>();
        softwareBomListDto.forEach(x->{
            EcuSoftwareListDto listDto=new EcuSoftwareListDto();
            listDto.setEcuTypeCode(x.getEcuTypeCode());
            listDto.setTargetIp(x.getTargetIp());
            listDto.setLogicalAddress(x.getLogicalAddress());
            listDto.setPublishChangeCode(x.getPublishChangeCode());
            listDto.setSoftwareCode(x.getSoftwareCode());
            listDto.setSoftwareVersion(x.getSoftwareVersion());
            listDto.setIsCheckSwVersion(x.getIsCheckSwVersion()?"1":"0");
            listDto.setHardwarePartNo(x.getHardwarePartNo());
            listDto.setDeploymentLocation(x.getDeploymentLocation());
            listDto.setSoftwarePackage(x.getSoftwarePackage());
            listDto.setIsSupportOta(x.getIsSupportOta()?"1":"0");
            listDto.setIsOnlineWritten(x.getIsOnlineWritten()?"1":"0");
            listDto.setIsSupportDiaWritten(x.getIsSupportDiaWritten()?"1":"0");
            listDto.setSoftwarePartNumberDid(x.getSoftwarePartNumberDid());
            listDto.setSoftwareVersionNumberDid(x.getSoftwareVersionNumberDid());
            listDto.setDiagnosticReqId(x.getDiagnosticReqId());
            listDto.setDiagnosticResId(x.getDiagnosticResId());
            listDto.setLastVehicleVersionCode(x.getLastVehicleVersionCode());
            List<SoftwareConfigDto> collect = bomConfig.get(0).getSoftwareConfigList().stream().filter(w -> w.getEcuCode().equals(x.getEcuTypeCode())).collect(Collectors.toList());
            listDto.setChangeCode(bomConfig.get(0).getChangeCode());
            List<EcuSoftwareDidDto> ecuConf =new ArrayList<>();
            collect.stream().forEach(v->{
                ecuConf.addAll(v.getEcuConfigList());
            });
            listDto.setEcuConf(ecuConf);
            ecuList.add(listDto);
        });

        //组装ecucarinfo数据
        EcuCarInfoDto infoDto=new EcuCarInfoDto();
        infoDto.setVinCode(dto.getVin());
        infoDto.setTypeCode("CD701");
        String[] str=dto.getVehicleMaterialNumbers().split("\\.");
        if(str!=null){
            infoDto.setCarCode(str[0]);
        }
        infoDto.setOptionCode("CNH1001-L");
        infoDto.setAttributeCode(dto.getDingCode());
        infoDto.setEcuList(ecuList);
        infoDto.setCreationDate(new Date());
        infoDto.setLastUpdateDate(new Date());
        return infoDto;
    }

    @Override
    public CarInfoDto getCarInfoByVinTest(MidDjTestSendEntity dto) {
        //组装ecucarinfo数据
        CarInfoDto infoDto=new CarInfoDto();
        infoDto.setVinCode(dto.getVin());
        infoDto.setTypeCode("CD701");
        infoDto.setBasicVehicleModel("CD701EV-010");
        String[] str=dto.getVehicleMaterialNumbers().split("\\.");
        if(str!=null){
            infoDto.setCarCode(str[0]);
            infoDto.setColorCode(str[2]);
        }
        infoDto.setFaDate(dto.getSpecifyDate());
        infoDto.setOptionCode("CNH1001-L");
        infoDto.setAttributeCode(dto.getDingCode());
        infoDto.setOrgCode(pmOrgService.getCurrentOrgCode().getData());
        infoDto.setVehicleMaterialNumber(dto.getVehicleMaterialNumbers());
        infoDto.setFeatureCode("AAA001,AAB001,AAC001,AAD001,AAE001,AAF001,AAG001,AAH001,AAJ001,AAK002,AAL001,AAM001,AAN001,AAP004,AAQ002,AAR002,AAS001,AAT001,AAU001,AAV001,BAA001,BAB001,BAC002,BAD002,BAE001,BAF001,BAG003,BAH003,BAJ002,BAK003,BAL001,BAM001,BAN001,BAP001,BAQ001,BAR002,BAS001,BAT001,CAA001,CAB001,CAC001,CAD001,CAE001,CAF001,CAG003,DAA002,DAB002,DAC004,DAD002,DAE002,DAF001,DAG002,DBA004,DBB003,DBC001,DBD001,DBE001,EAA001,EAB001,EAC001,EAD002,EAE002,EAF001,EAG001,EAH001,EAJ002,EAK001,FAA001,FAB003,FAC005,FAD005,FAE002,FAF001,FAG001,FBA001,FBB001,FBC001,FBD002,FBE001,GAA002,GAB002,GAC002,GAD002,GAE002,GAF002,GAG002,GAH002,GAJ002,GAK001,GAM002,GAN002,GAP002,GAQ002,GAR002,GAS002,GAT002,GAU002,GAV001,GAW002,GAX002,HAA001,HAB002,HAC002,HAD001,HAE002,HAF001,HAG001,HAH001,HAJ001,HAK001,HBA002,HBB002,HBC002,HBD001,HBE001,HBF001,HBG002,HCA002,HCB002,HCC002,JAA002,JAB002,JAC002,JAD002,JAE001,JAF001,JAG002,JAH001,JAJ001,JAK001,JAL002,JAM002,JAN001,JAP002,JAQ002,JAR002,JAS002,JAT001,JAU001,JAV002,JAW003,JAX003,JAY001,JAZ001,KAA001,KAB002,KAC002,KAD002,KAE002,KAF001,KAG001,KAH001,KAJ003,KAK002,KAL001,KAM001,KAN001,KAP001,LAA002,LAB002,LAC002,LAD002,LAE001,LAF001,LAG003,LAH001,LAJ001,LAK003,LAL001,LAM001,LAN002,LAP001,LAQ001,LAR002,LAS002,LAT001,LAU001,MAA003,MAB002,MAC003,MAD003,MAE001,NAA001,NAB003,NAC001,NAD003,NAE001,NAF003,NAG001,NAH002,NAJ001,NAK001,NAL001,NAM001,NAN004,NAP002,NAQ003,NAR003,NAS003,NAT003,NAU002,NAV001,NAW001,NAX001,NAY001,NAZ001,NBA001,NBB002,NBC001,NBD001,NBE001,NBF002,NBG001,NBH001,NBJ003,NBK001,PAA001,PAB003,PAC002,PAD002,PAE002,PAF002,PAG002,PAH002,PAJ001,PAK002,PAL002,PAM001,PAN002,PAP001,QAA001,QAB001,QAC001,QAD002,QAE002,QAF002,QAG002,QAH002,QAJ001,QAK002,QAL002,QAM001,QAN001,QAP001,QAQ004,QAR001,QAS001,QAT004,QAU001,QAV001,QAW002,QAX002,QAY001,QAZ001,QBA001,QBB002,QBC001,QBD001,QBE001,QBF002,QBG002,QBH004,QBJ001,QBK001,QBL003,QBM001,QBN001,QBP001,QBQ001,QBR002,QBS002,QBT001,QBU001,QBV001,QBW001,QBX001,RAA002,RAB001,RAC001,RAD001,RAE003,RAF003,RAG002,RAH003,RAJ007,RAK004,RAL004,RAM002,RBA001,RBB002,RBC002,RBD003,RBE002,RBF001,RBG001,RBH002,RBJ002,RBL001,RCA001,RCB002,RCC001,RCD001,RCE002,RCF002,SAA002,SAB002,SAC001,SAD001,SAE003,SAF001,SAG001,SAH002,SAJ002,SAK001,SAL002,SAM001,SAN002,SAP001,SAQ001,SAR001,SAS002,SAT004,SAU002,SAV002,SAW001,SAX001,SAY001,SAZ001,SBA001,SBB001,SBC001,SBD002,SBE001,SBF001,SBG001,SBH001,SBJ001,SBK003,SBL002,SBM001,SBN001,SBP003,SBQ002,SBR001,SBS001,SBT001,SBU001,SBV002,SBW002,SBX002,SBY001");
        infoDto.setCreationDate(new Date());
        return infoDto;
    }
}