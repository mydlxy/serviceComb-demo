package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;

import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;

import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.dto.MidBomPartInfo;
import com.ca.mfd.prc.pm.communication.entity.MidBomPartEntity;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidBomPartMapper;
import com.ca.mfd.prc.pm.communication.service.IMidBomPartService;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialMasterService;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmProductBomService;
import com.ca.mfd.prc.pm.service.IPmProductBomVersionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 单车BOM服务实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@Service
public class MidBomPartServiceImpl extends MidBomBaseServiceImpl<IMidBomPartMapper, MidBomPartEntity, MidBomPartInfo> implements IMidBomPartService {
    private static final Logger logger = LoggerFactory.getLogger(MidBomPartServiceImpl.class);
    @Autowired
    private IPmProductBomVersionsService bomVersionsService;
    @Autowired
    private IPmProductBomService bomService;
    @Autowired
    private IMidMaterialMasterService materialMasterService;

    @Override
    public String getBomPartVersion(String materialNo,String plantCode,String specifyDate) {
        //调用外部接口
        List<MidBomPartInfo> infos = fetchDataFromApi(materialNo,plantCode,specifyDate);
        if(CollectionUtils.isEmpty(infos)){
            throw new InkelinkException("从BOM获取物料号："+materialNo+"；对应BOM无数据");
        }
        //获取校验数据
        List<MidBomPartInfo> tmp = new ArrayList<>();
        for(MidBomPartInfo et:infos){
            MidBomPartInfo item = new MidBomPartInfo();
            BeanUtils.copyProperties(et,item);
            item.setEffectiveFrom(null);
            item.setEffectiveTo(null);
            tmp.add(item);
        }
        String verifyData = md5(JsonUtils.toJsonString(tmp));
        //版本号
        String versions = materialNo + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        PmProductBomVersionsEntity bomVersionsInfo = getByProductMaterialNo(materialNo, verifyData);
        if (bomVersionsInfo == null) {
            List<MidMaterialMasterEntity> midmaters = materialMasterService.getAllDatas();
            bomVersionsInfo = new PmProductBomVersionsEntity();
            bomVersionsInfo.setId(IdGenerator.getId());
            bomVersionsInfo.setProductMaterialNo(materialNo);
            bomVersionsInfo.setBomVersions(versions);
            bomVersionsInfo.setCheckCode(verifyData);
            bomVersionsInfo.setIsEnable(true);
            bomVersionsInfo.setAttribute1("2");
            bomVersionsService.insert(bomVersionsInfo);
            bomVersionsService.saveChange();
            Long bomVersionId = bomVersionsInfo.getId();
            bomService.insertBatch(infos.stream().map(c -> {
                PmProductBomEntity et = new PmProductBomEntity();
                et.setBomVersionsId(bomVersionId);
                MidMaterialMasterEntity midMaterialMasterEntity = midmaters.stream().filter(m -> m.getMaterialCode().equals(c.getMaterialCode())).findFirst().orElse(null);
                et.setMaterialNo(c.getMaterialCode());
                if(midMaterialMasterEntity!=null){
                    et.setMaterialCn(midMaterialMasterEntity.getMaterialName()==null?"":midMaterialMasterEntity.getMaterialName());
                    et.setMaterialEn(midMaterialMasterEntity.getMaterialNameEn()==null?"":midMaterialMasterEntity.getMaterialNameEn());
                }
                et.setQuantity(new BigDecimal(c.getQuantity()));
                return et;
            }).collect(Collectors.toList()));
            bomService.saveChange();

            LambdaUpdateWrapper<PmProductBomVersionsEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmProductBomVersionsEntity::getIsEnable, false);
            upset.eq(PmProductBomVersionsEntity::getProductMaterialNo, materialNo);
            bomVersionsService.update(upset);
            bomVersionsService.saveChange();
        }else {
            versions=bomVersionsInfo.getBomVersions();
        }
        return versions;
    }

    private List<MidBomPartInfo> fetchDataFromApi(String materialNo,String plantCode,String specifyDate) {
        return super.fetchDataFromApi("bompart_receive","零件BOM信息",ApiTypeConst.BOM_PART,true, materialNo, plantCode, specifyDate);
    }

    @Override
    protected MidBomPartEntity getEntity(MidBomPartInfo c, Long loginfoId){
        MidBomPartEntity entity = new MidBomPartEntity();
        BeanUtils.copyProperties(c,entity);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<MidBomPartInfo> fetchEntity(BomResultVo resultVO){
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidBomPartInfo.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param =new HashMap<>(4);
        param.put("materialCode",params[0]);
        param.put("plantCode",params[1]);
        param.put("specifyDate",params[2]);
        return param;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    private PmProductBomVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo).eq(PmProductBomVersionsEntity::getCheckCode, checkCode);
        return bomVersionsService.getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}