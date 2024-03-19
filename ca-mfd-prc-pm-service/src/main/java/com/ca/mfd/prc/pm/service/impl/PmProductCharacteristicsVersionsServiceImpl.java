package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.dto.CharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.SetCharacteristicsEnablePara;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsVersionsEntity;
import com.ca.mfd.prc.pm.mapper.IPmProductCharacteristicsVersionsMapper;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 特征版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductCharacteristicsVersionsServiceImpl extends AbstractCrudServiceImpl<IPmProductCharacteristicsVersionsMapper, PmProductCharacteristicsVersionsEntity> implements IPmProductCharacteristicsVersionsService {
    private static final String CACHE_NAME = "PRC_PM_PRODUCT_CHARACTERISTICS_VERSIONS";
    private static final Object LOCK_OBJ = new Object();
    private static final ConcurrentMap<String, String> PRODUCT_CHARACTERISTICS_VERSIONS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> PRODUCT_CHARACTERISTICS = new ConcurrentHashMap<>();
    //    @Autowired
//    private IPmCharacteristicsDataService pmCharacteristicsDataService;
    @Autowired
    private IPmProductCharacteristicsService pmProductCharacteristicsService;
    @Autowired
    private LocalCache localCache;

    @Override
    public PmProductCharacteristicsVersionsEntity getByMaterialNoVersions(String productMaterialNo, String versions) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qryver = new QueryWrapper<>();
        qryver.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductCharacteristicsVersionsEntity::getVersions, versions);
        return getTopDatas(1, qryver).stream().findFirst().orElse(null);
    }

    @Override
    public PmProductCharacteristicsVersionsEntity getByCharacteristicsVersions(String characteristicsVersions) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmProductCharacteristicsVersionsEntity> lambdaQueryWrapper = wrapper.lambda();
        lambdaQueryWrapper.eq(PmProductCharacteristicsVersionsEntity::getVersions, characteristicsVersions);
        return this.selectList(wrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PmProductCharacteristicsVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode) {
        //c => c.ProductMaterialNo == data.ProductMaterialNo && c.CheckCode == verifyData
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductCharacteristicsVersionsEntity::getCheckCode, checkCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    /**
     * 维护特征（主要针对于一计划一特征）
     *
     * @param data 特征数据
     * @return bom 版本号
     */
    @Override
    public String maintainCharacteristics(MaintainCharacteristicsInfo data,String attr) {

        if (StringUtils.isBlank(data.getProductMaterialNo())) {
            throw new InkelinkException("无效的产品物料号");
        }
        List<CharacteristicsInfo> cdata = data.getCharacteristicsData().stream().sorted(Comparator.comparing(CharacteristicsInfo::getName)).collect(Collectors.toList());
        //获取校验数据
        // var verifyData = GetMD5(data.ProductMaterialNo);
        String verifyData = md5(JsonUtils.toJsonString(data.getCharacteristicsData()));
        //pmCharacteristicsDataService.getVehicleFeature();
        //获取当前系统的特征
        List<String> characteristicsData = new ArrayList<>();

        PmProductCharacteristicsVersionsEntity characteristicsVersionsInfo = getByProductMaterialNo(data.getProductMaterialNo(), verifyData);
        if (characteristicsVersionsInfo == null) {
            if (!PRODUCT_CHARACTERISTICS.containsKey(data.getProductMaterialNo())) {
                for (CharacteristicsInfo item : data.getCharacteristicsData()) {

                    ///对比当前特征是否存在不存在就加到添加到特征主数据中
                    if (characteristicsData.stream().filter(c -> StringUtils.equals(c, item.getValue())).findFirst().orElse(null) == null) {
//                        PmCharacteristicsDataEntity et = new PmCharacteristicsDataEntity();
//                        et.setName(item.getName());
//                        et.setDescriptionCn(item.getDescriptionCn());
//                        et.setDescriptionEn(item.getDescriptionEn());
//                        et.setCode(item.getCode());
//                        et.setValue(item.getValue());
//                        et.setValueCn(item.getValueCn());
//                        et.setValueEn(item.getValueEn());
//                        et.setSource(item.getSource());
//                        pmCharacteristicsDataService.insert(et);
                    }
                }
                PRODUCT_CHARACTERISTICS.put(data.getProductMaterialNo(), verifyData);
            }
        }
        //MES自己维护BOM版本
        if (StringUtils.isBlank(data.getCharacteristicsVersions())) {
            data.setCharacteristicsVersions(data.getProductMaterialNo() + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));

            if (characteristicsVersionsInfo == null) {
                if (PRODUCT_CHARACTERISTICS_VERSIONS.containsKey(data.getProductMaterialNo())) {
                    return PRODUCT_CHARACTERISTICS_VERSIONS.get(data.getProductMaterialNo());
                }
                characteristicsVersionsInfo = new PmProductCharacteristicsVersionsEntity();
                characteristicsVersionsInfo.setId(IdGenerator.getId());
                characteristicsVersionsInfo.setProductMaterialNo(data.getProductMaterialNo());
                characteristicsVersionsInfo.setVersions(data.getCharacteristicsVersions());
                characteristicsVersionsInfo.setCheckCode(verifyData);
                characteristicsVersionsInfo.setIsEnable(data.getIsEnable());
                characteristicsVersionsInfo.setAttribute1(attr);
                //添加特征新的信息
                insert(characteristicsVersionsInfo);
                Long characteristicsVersionsId = characteristicsVersionsInfo.getId();
                pmProductCharacteristicsService.insertBatch(data.getCharacteristicsData().stream().map(c -> {
                    PmProductCharacteristicsEntity et = new PmProductCharacteristicsEntity();
                    et.setCharacteristicsVersionsId(characteristicsVersionsId);
                    et.setMaterialNo(data.getProductMaterialNo());
                    et.setProductCharacteristicsName(c.getName());
                    et.setDescriptionCn(c.getDescriptionCn());
                    et.setDescriptionEn(c.getDescriptionEn());
                    et.setProductCharacteristicsCode(c.getCode());
                    et.setProductCharacteristicsValue(c.getValue());
                    et.setValueCn(c.getValueCn());
                    et.setValueEn(c.getValueEn());
                    et.setSource(c.getSource());
                    et.setAttribute1(attr);
                    return et;
                }).collect(Collectors.toList()));

                //按照实际情况这边的整车物料号只会出现一个版本所以默认必须是启动的
                if (data.getIsEnable()) {
                    LambdaUpdateWrapper<PmProductCharacteristicsVersionsEntity> upset = new LambdaUpdateWrapper<>();
                    upset.set(PmProductCharacteristicsVersionsEntity::getIsEnable, false);
                    upset.eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, data.getProductMaterialNo());
                    update(upset);

                }
            }
            PRODUCT_CHARACTERISTICS_VERSIONS.put(data.getProductMaterialNo(), characteristicsVersionsInfo.getVersions());
            return characteristicsVersionsInfo.getVersions();
        } else//SAP同步特征进行维护 江淮这边该方法已废弃
        {
            //该版本特征是否存在
            if (characteristicsVersionsInfo != null) {
                //该版本BOM的内容是否发生变化
                if (StringUtils.equals(characteristicsVersionsInfo.getCheckCode(), verifyData)) {
                    return characteristicsVersionsInfo.getVersions();
                } else//该版本BOM的内容发生变化后将删除重新创建
                {
                    UpdateWrapper<PmProductCharacteristicsVersionsEntity> delQry = new UpdateWrapper<>();
                    delQry.lambda().eq(PmProductCharacteristicsVersionsEntity::getId, characteristicsVersionsInfo.getId());
                    delete(delQry);

                    UpdateWrapper<PmProductCharacteristicsEntity> deltrsQry = new UpdateWrapper<>();
                    deltrsQry.lambda().eq(PmProductCharacteristicsEntity::getCharacteristicsVersionsId, characteristicsVersionsInfo.getId());
                    pmProductCharacteristicsService.delete(deltrsQry);

                    //清除缓存
                    localCache.removeObject(CACHE_NAME + characteristicsVersionsInfo.getProductMaterialNo() + characteristicsVersionsInfo.getVersions());
                    characteristicsVersionsInfo = null;
                }
            }

            characteristicsVersionsInfo = new PmProductCharacteristicsVersionsEntity();
            characteristicsVersionsInfo.setId(IdGenerator.getId());
            characteristicsVersionsInfo.setProductMaterialNo(data.getProductMaterialNo());
            characteristicsVersionsInfo.setVersions(data.getCharacteristicsVersions());
            characteristicsVersionsInfo.setCheckCode(verifyData);
            characteristicsVersionsInfo.setIsEnable(data.getIsEnable());

            //添加BOM新的信息
            insert(characteristicsVersionsInfo);
            Long characteristicsVersionsId = characteristicsVersionsInfo.getId();
            pmProductCharacteristicsService.insertBatch(data.getCharacteristicsData().stream().map(c -> {
                PmProductCharacteristicsEntity et = new PmProductCharacteristicsEntity();
                et.setMaterialNo(data.getProductMaterialNo());
                et.setCharacteristicsVersionsId(characteristicsVersionsId);
                et.setProductCharacteristicsName(c.getName());
                et.setDescriptionCn(c.getDescriptionCn() == null ? "" : c.getDescriptionCn());
                et.setDescriptionEn(c.getDescriptionEn() == null ? "" : c.getDescriptionEn());
                et.setProductCharacteristicsCode(c.getCode());
                et.setProductCharacteristicsValue(c.getValue());
                et.setValueCn(StringUtils.isBlank(c.getValueCn()) ? c.getValue() : c.getValueCn());
                //   ValueEn = string.IsNullOrEmpty(c.ValueEn) ? c.ValueEn : c.ValueEn, TODO 是否问题
                et.setValueEn(c.getValueEn());
                et.setSource(c.getSource());
                return et;
            }).collect(Collectors.toList()));

            if (data.getIsEnable()) {
                LambdaUpdateWrapper<PmProductCharacteristicsVersionsEntity> upset = new LambdaUpdateWrapper<>();
                upset.set(PmProductCharacteristicsVersionsEntity::getIsEnable, false);
                upset.eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, data.getProductMaterialNo());
                update(upset);
            }
            return characteristicsVersionsInfo.getVersions();
        }
        //#endregion
    }

    /**
     * 获取特征详细数据
     *
     * @param productMaterialNo 物料编号
     * @param versions          物料版本
     * @return 特征集合
     */
    @Override
    public List<PmProductCharacteristicsEntity> getCharacteristicsData(String productMaterialNo, String versions) {
        List<PmProductCharacteristicsEntity> datas = localCache.getObject(CACHE_NAME + productMaterialNo + versions);
        if (datas == null || datas.isEmpty()) {
            synchronized (LOCK_OBJ) {
                datas = localCache.getObject(CACHE_NAME + productMaterialNo + versions);
                if (datas == null || datas.isEmpty()) {
                    PmProductCharacteristicsVersionsEntity versionsInfo = getByMaterialNoVersions(productMaterialNo, versions);
                    if (versionsInfo == null) {
                        throw new InkelinkException("未找到" + productMaterialNo + "产品版本" + versions + "特征数据");
                    }
                    datas = pmProductCharacteristicsService.getByCharacteristicsVersionsId(versionsInfo.getId());
                    localCache.addObject(CACHE_NAME + productMaterialNo + versions, datas, -1);
                }
            }
        }
        return datas;
    }

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取特征版本信息
     */
    @Override
    public String getCharacteristicsVersions(String productMaterialNo) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductCharacteristicsVersionsEntity::getIsEnable, true)
                .orderByDesc(PmProductCharacteristicsVersionsEntity::getCreationDate);
        PmProductCharacteristicsVersionsEntity versionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (versionsInfo == null) {
            throw new InkelinkException("该物料[" + productMaterialNo + "]没有对应的特征数据");
        }
        return versionsInfo.getVersions();
    }

    /**
     * 获取特征版本列表
     *
     * @param productMaterialNo
     * @return
     */
    @Override
    public List<String> getCharacteristicsVersionsList(String productMaterialNo) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .orderByDesc(PmProductCharacteristicsVersionsEntity::getCreationDate);
        return this.selectList(qry).stream().map(c -> c.getVersions()).collect(Collectors.toList());
    }

    /**
     * 根据物料号查询特征版本
     *
     * @param productMaterialNo 物料号
     * @return 特征版本
     */
    @Override
    public PmProductCharacteristicsVersionsEntity getCharacteristicsVersionsByCode(String productMaterialNo) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductCharacteristicsVersionsEntity::getIsEnable, true)
                .orderByDesc(PmProductCharacteristicsVersionsEntity::getCreationDate);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 设置特征启用版本
     *
     * @param param
     */
    @Override
    public void setCharacteristicsEnable(SetCharacteristicsEnablePara param) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getId, param.getId());
        PmProductCharacteristicsVersionsEntity pmProductCharacteristicsVersionsEntity = this.getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (pmProductCharacteristicsVersionsEntity == null) {
            throw new InkelinkException("ID为" + param.getId() + "不存在或已经被删除!");
        }
        LambdaUpdateWrapper<PmProductCharacteristicsVersionsEntity> uw = new LambdaUpdateWrapper<>();
        uw.set(PmProductCharacteristicsVersionsEntity::getIsEnable, false)
                .eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, pmProductCharacteristicsVersionsEntity.getProductMaterialNo());
        this.update(uw);
        uw = new LambdaUpdateWrapper<>();
        uw.set(PmProductCharacteristicsVersionsEntity::getIsEnable, true)
                .eq(PmProductCharacteristicsVersionsEntity::getId, pmProductCharacteristicsVersionsEntity.getId());
        this.update(uw);
        this.saveChange();
    }


    @Override
    public String copyCharacteristics(String productMaterialNo, String characteristicsVersions) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda()
                //.eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                //.eq(PmProductBomVersionsEntity::getIsEnable, true);
                .eq(PmProductCharacteristicsVersionsEntity::getVersions, characteristicsVersions);

        PmProductCharacteristicsVersionsEntity characteristicsVersionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (characteristicsVersionsInfo == null) {
            throw new InkelinkException("未找到" + productMaterialNo + "产品版本" + characteristicsVersions + "特征数据");
        }else{
            QueryWrapper<PmProductCharacteristicsVersionsEntity> qryEt = new QueryWrapper<>();
            qryEt.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                    .eq(PmProductCharacteristicsVersionsEntity::getIsEnable, true)
                    .orderByDesc(PmProductCharacteristicsVersionsEntity::getCreationDate);
            PmProductCharacteristicsVersionsEntity oldVersion = getTopDatas(1, qryEt).stream().findFirst().orElse(null);
            if(oldVersion !=null) {
                return oldVersion.getVersions();
            }
        }

        if (StringUtils.equals(productMaterialNo, characteristicsVersionsInfo.getProductMaterialNo())) {
            return characteristicsVersionsInfo.getVersions();
        } else {
            PmProductCharacteristicsVersionsEntity newCharacteristicsVersionsVersion = new PmProductCharacteristicsVersionsEntity();
            newCharacteristicsVersionsVersion.setProductMaterialNo(productMaterialNo);
            newCharacteristicsVersionsVersion.setVersions(newCharacteristicsVersionsVersion.getProductMaterialNo() + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
            newCharacteristicsVersionsVersion.setIsEnable(true);
            newCharacteristicsVersionsVersion.setCheckCode(characteristicsVersionsInfo.getCheckCode());
            newCharacteristicsVersionsVersion.setId(IdGenerator.getId());
            this.insert(newCharacteristicsVersionsVersion);

            List<PmProductCharacteristicsEntity> boms = pmProductCharacteristicsService.getByCharacteristicsVersionsId(characteristicsVersionsInfo.getId());
            for (PmProductCharacteristicsEntity bm : boms) {
                bm.setCharacteristicsVersionsId(newCharacteristicsVersionsVersion.getId());
                bm.setId(IdGenerator.getId());
                bm.setMaterialNo(productMaterialNo);
            }
            pmProductCharacteristicsService.insertBatch(boms);
            return newCharacteristicsVersionsVersion.getVersions();
        }
    }

}