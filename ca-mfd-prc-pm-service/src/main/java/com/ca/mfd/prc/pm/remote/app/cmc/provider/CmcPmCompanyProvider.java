package com.ca.mfd.prc.pm.remote.app.cmc.provider;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.CmcPmAreaVo;
import com.ca.mfd.prc.pm.dto.CmcPmOrganizationVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkCenterVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkUnitVo;
import com.ca.mfd.prc.pm.remote.app.cmc.ICmcPmCompanyService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CmcPmCompanyProvider {
    private static final Log log = LogFactory.getLog(CmcPmCompanyProvider.class);

    @Value("${inkelink.cmc.mustsyc:true}")
    private boolean mustSycCmc;
    @Autowired
    private ICmcPmCompanyService cmcPmCompanyService;


    public List<CmcPmOrganizationVo> getPlantPage(DataDto model) {
        ResultVO<List<CmcPmOrganizationVo>> result = cmcPmCompanyService.getPlantPage(model);
        if (!result.getSuccess() && mustSycCmc) {
            throw new InkelinkException("获取工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public List<CmcPmAreaVo> getAreaPage(DataDto model) {
        ResultVO<List<CmcPmAreaVo>> result = cmcPmCompanyService.getAreaPage(model);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("获取区域信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取区域信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public List<CmcPmWorkCenterVo> getWorkCenterPage(DataDto model) {
        ResultVO<List<CmcPmWorkCenterVo>> result = cmcPmCompanyService.getWorkCenterPage(model);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("获取工作中心时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取工作中心时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public List<CmcPmWorkUnitVo> getWorkUnitPage(DataDto model) {
        ResultVO<List<CmcPmWorkUnitVo>> result = cmcPmCompanyService.getWorkUnitPage(model);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("获取工作单元时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("获取工作单元时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public CmcPmOrganizationVo edit(CmcPmOrganizationVo dto){
        ResultVO<CmcPmOrganizationVo> result = cmcPmCompanyService.edit(dto);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("保存工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("保存工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }

    public CmcPmOrganizationVo del(List<Long> idList){
        IdsModel model = new IdsModel();
        String[] ids = new String[idList.size()];
        idList.toArray(ids);
        model.setIds(ids);
        ResultVO<CmcPmOrganizationVo> result = cmcPmCompanyService.delete(model);
        if (!result.getSuccess() && mustSycCmc){
            throw new InkelinkException("删除工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
        }else if(!result.getSuccess() && !mustSycCmc) {
            log.error("删除工厂信息时,通用工厂服务inkelink-cmc-pm-company返回错误：" + result.getMessage());
            return null;
        }
        return result.getData();
    }
}
