package com.ca.mfd.prc.pm.remote.app.cmc.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.CmcPmAreaVo;
import com.ca.mfd.prc.pm.dto.CmcPmBopVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentPowerVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentVo;
import com.ca.mfd.prc.pm.dto.CmcPmOrganizationVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkCenterVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkUnitVo;
import com.ca.mfd.prc.pm.remote.app.cmc.ICmcPmFactoryService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CmcPmFactoryProvider {
    private static final Log log = LogFactory.getLog(CmcPmFactoryProvider.class);

    @Value("${inkelink.cmc.code:abcdefg}")
    private String cmcVisitCode;

    @Value("${inkelink.cmc.source:1}")
    private Integer cmcVisitSource;

    @Value("${inkelink.cmc.mustsyc:true}")
    private boolean mustSycCmc;
    @Autowired
    private ICmcPmFactoryService cmcPmFactoryService;

    public List<CmcPmEquipmentVo> getEquipmentPage(DataDto model) {
        ResultVO<List<CmcPmEquipmentVo>> result = cmcPmFactoryService.getEquipmentPage(model);
        if (!result.getSuccess() && mustSycCmc) {
            throw new InkelinkException("获取设备时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取设备信息时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public List<CmcPmEquipmentPowerVo> getEquipmentPowerPage(DataDto model) {
        ResultVO<List<CmcPmEquipmentPowerVo>> result = cmcPmFactoryService.getEquipmentPowerPage(model);
        if (!result.getSuccess() && mustSycCmc) {
            throw new InkelinkException("获取设备能力时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取设备能力时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public List<CmcPmBopVo> getBopPage(DataDto model) {
        ResultVO<List<CmcPmBopVo>> result = cmcPmFactoryService.getBopPage(model);
        if (!result.getSuccess() && mustSycCmc) {
            throw new InkelinkException("获取Bop时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取Bop信息时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object batchDelete(String dataType, List<Long> ids){
        ResultVO<CmcPmOrganizationVo> result = cmcPmFactoryService.batchDelete(cmcVisitCode,cmcVisitSource,dataType,ids);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量删除"+dataType+"时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量删除"+dataType+"时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object areaBatchSave(List<CmcPmAreaVo> areas){
        ResultVO result = cmcPmFactoryService.areaBatchSave(cmcVisitCode,cmcVisitSource,areas);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存区域时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存区域时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object workCenterBatchSave(List<CmcPmWorkCenterVo> workCenters){
        ResultVO result = cmcPmFactoryService.workCenterBatchSave(cmcVisitCode,cmcVisitSource,workCenters);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存工作中心时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存工作中心时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object workUnitBatchSave(List<CmcPmWorkUnitVo> workUnits){
        ResultVO result = cmcPmFactoryService.workUnitBatchSave(cmcVisitCode,cmcVisitSource,workUnits);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存工作单元时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存工作单元时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object equipmentBatchSave(List<CmcPmEquipmentVo> equipments){
        ResultVO result = cmcPmFactoryService.equipmentBatchSave(cmcVisitCode,cmcVisitSource,equipments);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存设备时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存设备时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object equipmentPowerBatchSave(List<CmcPmEquipmentPowerVo> equipmentPowers){
        ResultVO result = cmcPmFactoryService.equipmentPowerBatchSave(cmcVisitCode,cmcVisitSource,equipmentPowers);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存设备能力时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存设备能力时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public Object bopBatchSave(List<CmcPmBopVo> workBops){
        ResultVO result = cmcPmFactoryService.bopBatchSave(cmcVisitCode,cmcVisitSource,workBops);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("批量保存Bop时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("批量保存Bop时,通用工厂服务inkelink-cmc-pm-factory返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }
}
