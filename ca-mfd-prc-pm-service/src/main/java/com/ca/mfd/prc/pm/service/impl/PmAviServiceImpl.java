package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.service.IPmAviService;
import com.ca.mfd.prc.pm.service.IPmOtService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: AVI站点
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmAviServiceImpl extends PmAviParentServiceImpl implements IPmAviService {

    public static final int VIRTUALLY_WORKSTATION = 2;
    @Lazy
    @Autowired
    private IPmVersionService pmVersionService;

    @Autowired
    private IPmOtService pmOtService;

    @Autowired
    private IPmLineMapper pmLineMapper;

    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;

    /**
     * @return AVI点列表
     */
    @Override
    public List<AviInfoDTO> getAviInfos() {
        List<AviInfoDTO> avis = new ArrayList<>();
        PmAllDTO currPm = pmVersionService.getObjectedPm();
        if (currPm == null) {
            return avis;
        }
        List<PmAviEntity> pmAvis = currPm.getAvis();
        if (pmAvis == null || pmAvis.size() == 0) {
            return avis;
        }
        for (PmAviEntity pmAvi : pmAvis) {
            AviInfoDTO addInfo = new AviInfoDTO();
            PmLineEntity pmLine = currPm.getLines().stream().filter(o -> o.getId().equals(pmAvi.getPrcPmLineId())).findFirst().orElse(null);
            PmWorkShopEntity pmShop = null;
            if (pmLine != null) {
                pmShop = currPm.getShops().stream().filter(o -> o.getId().equals(pmLine.getPrcPmWorkshopId())).findFirst().orElse(null);
            }
            addInfo.setPmLineCode(pmLine == null ? "" : pmLine.getLineCode());
            addInfo.setPmLineName(pmLine == null ? "" : pmLine.getLineName());
            addInfo.setPmAviCode(pmAvi.getAviCode());
            addInfo.setPmAviId(pmAvi.getId());
            addInfo.setPmAviName(pmAvi.getAviName());
            addInfo.setPmShopCode(pmShop == null ? "" : pmShop.getWorkshopCode());
            addInfo.setPmShopName(pmShop == null ? "" : pmShop.getWorkshopName());
            avis.add(addInfo);
        }
        return avis;
    }

    /**
     * 验证虚拟工位
     * @return
     */
    private boolean verifyVirtuallyWorkStation(PmAviEntity model){
        if(model.getAviAttribute() == VIRTUALLY_WORKSTATION &&
                StringUtils.isNotBlank(model.getStationCode())){
            QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
            qw.select("WORKSTATION_CODE");
            qw.eq("PRC_PM_LINE_ID",model.getPrcPmLineId());
            qw.eq("WORKSTATION_CODE",model.getStationCode());
            qw.eq("IS_DELETE",false);
            return this.pmWorkStationMapper.selectList(qw).size() > 0;
        }
        return true;
    }



    @Override
    public void canDeleteLine(Serializable[] lineIds) {
        QueryWrapper<PmAviEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PmAviEntity::getPrcPmLineId, lineIds);
        List<PmAviEntity> aviList = getData(qry, false);
        if (CollectionUtils.isNotEmpty(aviList)) {
            PmAviEntity aviEntity = aviList.stream().findFirst().orElse(null);
            if(Objects.nonNull(aviEntity)){
                throw new InkelinkException("线体下有AVI，不允许删除!");
            }
        }
    }

    @Override
    public void update(PmAviEntity pmAviEntity) {
        beforeUpdate(pmAviEntity);
        LambdaUpdateWrapper<PmAviEntity> luw = new LambdaUpdateWrapper();
        luw.set(PmAviEntity::getAviCode, pmAviEntity.getAviCode());
        luw.set(PmAviEntity::getAviName, pmAviEntity.getAviName());
        luw.set(PmAviEntity::getAviType, pmAviEntity.getAviType());
        luw.set(PmAviEntity::getAviAttribute, pmAviEntity.getAviAttribute());
        luw.set(PmAviEntity::getIpAddress, pmAviEntity.getIpAddress());
        luw.set(PmAviEntity::getRemark, pmAviEntity.getRemark());
        luw.set(PmAviEntity::getStationCode, pmAviEntity.getStationCode());
        luw.set(PmAviEntity::getIsEnable, pmAviEntity.getIsEnable());
        luw.set(PmAviEntity::getAviDisplayNo, pmAviEntity.getAviDisplayNo());
        luw.set(PmAviEntity::getIsMain, pmAviEntity.getIsMain());
        luw.set(PmAviEntity::getAviFunctions, pmAviEntity.getAviFunctions());
        luw.set(PmAviEntity::getOpcConnector, pmAviEntity.getOpcConnector());
        luw.set(PmAviEntity::getPointDb, pmAviEntity.getPointDb());
        luw.set(PmAviEntity::getDb4, pmAviEntity.getDb4());
        luw.set(PmAviEntity::getDb3, pmAviEntity.getDb3());
        luw.set(PmAviEntity::getDefaultPage, pmAviEntity.getDefaultPage());
        luw.eq(PmAviEntity::getId, pmAviEntity.getId());
        luw.eq(PmAviEntity::getVersion, pmAviEntity.getVersion());
        luw.eq(PmAviEntity::getPrcPmWorkshopId, pmAviEntity.getPrcPmWorkshopId());
        luw.eq(PmAviEntity::getPrcPmLineId, pmAviEntity.getPrcPmLineId());
        this.update(luw);
    }

    @Override
    public void beforeUpdate(PmAviEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmAviEntity model) {
        validData(model);
    }

    private void validData(PmAviEntity model) {
        if (model.getPrcPmWorkshopId() == null) {
            throw new InkelinkException("车间信息未传入");
        }
        if (model.getPrcPmLineId() == null) {
            throw new InkelinkException("线体信息未传入");
        }
        if (StringUtils.isBlank(model.getDefaultPage())) {
            throw new InkelinkException("默认访问界面未传入");
        }
        validDataUnique(model.getId(), "AVI_CODE", model.getAviCode(), "已经存在编码为%s的数据,AVI编码必须全厂间唯一",null,null);
        String tempdea = isExistIp(String.valueOf(model.getId()), model.getIpAddress());
        if (tempdea != null) {
            throw new InkelinkException(tempdea);
        }
        QueryWrapper<PmLineEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmLineEntity> qry = queryWrapper.lambda();
        qry.eq(PmLineEntity::getId, model.getPrcPmLineId());
        qry.eq(PmLineEntity::getIsDelete, false);
        qry.eq(PmLineEntity::getVersion, 0);
        PmLineEntity validModel = pmLineMapper.selectOne(qry);
        if (!validModel.getIsEnable()) {
            throw new InkelinkException("线体名称：" + validModel.getLineName() + "=>线体没有启用");
        }
        if(!verifyVirtuallyWorkStation(model)){
            throw new InkelinkException(String.format("AVI对应的虚拟工位号[%s]在当前线体下不存在",model.getStationCode()));
        }

    }

    public String isExistIp(String id, String ip) {
        if (!StringUtils.isBlank(ip)) {
            PmAviEntity aviInfo = getAviEntityByIp(id, ip);
            if (!ObjectUtils.isEmpty(aviInfo)) {
                return "AVI(" + aviInfo.getAviName() + "已经配置了IP" + ip;
            }

            PmOtEntity otInfo = pmOtService.getEntityByIp(id, ip);
            if (!ObjectUtils.isEmpty(otInfo)) {
                return "OT(" + otInfo.getOtName() + ")已经配置了IP" + ip;
            }
        }
        return null;
    }

    private PmAviEntity getAviEntityByIp(String id, String ip) {
        QueryWrapper<PmAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmAviEntity::getIpAddress, ip);
        lambdaQueryWrapper.ne(PmAviEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    private PmAviEntity getAviEntityByCode(String id, String code) {
        QueryWrapper<PmAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmAviEntity::getAviCode, code);
        lambdaQueryWrapper.ne(PmAviEntity::getId, id);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

}