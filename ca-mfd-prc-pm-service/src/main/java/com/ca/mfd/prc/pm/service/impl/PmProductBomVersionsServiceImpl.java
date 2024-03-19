package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.dto.BomInfo;
import com.ca.mfd.prc.pm.dto.CharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.MaintainBomDTO;
import com.ca.mfd.prc.pm.dto.MaintainCharacteristicsInfo;
import com.ca.mfd.prc.pm.dto.SetBomEnablePara;
import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.mapper.IPmBopMapper;
import com.ca.mfd.prc.pm.mapper.IPmProductBomVersionsMapper;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import com.ca.mfd.prc.pm.service.IPmProductBomService;
import com.ca.mfd.prc.pm.service.IPmProductBomVersionsService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterService;
import com.ca.mfd.prc.pm.service.IPmTraceComponentService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: @Description: BOM版本
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductBomVersionsServiceImpl extends AbstractCrudServiceImpl<IPmProductBomVersionsMapper, PmProductBomVersionsEntity> implements IPmProductBomVersionsService {

    private static final Object LOCK_OBJ = new Object();
    private final String cacheName = "PRC_PM_PRODUCT_BOM_VERSIONS";
    private final ConcurrentMap<String, String> bomVersions = new ConcurrentHashMap<>();
    @Autowired
    private IPmProductBomService pmProductBomService;
    @Autowired
    private IPmProductBomVersionsMapper pmProductBomVersionsMapper;
    @Autowired
    private LocalCache localCache;

    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;
    @Autowired
    private IPmBopMapper pmBopMapper;
    @Autowired
    private  IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;
    @Autowired
    private IPmCharacteristicsDataService pmCharacteristicsDataService;
    @Autowired
    private IPmTraceComponentService pmTraceComponentService;


    public void inputCD701Bom(String tbname,String matrNo) {
        //CD701EV_WD7
        List<HashMap<String, Object>> datas = pmBopMapper.getBomwd7(tbname);
        List<String>  rules = new ArrayList<>();
        String attr = tbname;
        MaintainBomDTO main = new MaintainBomDTO();
        main.setBomData(new ArrayList<>());
        main.setBomVersions("");
        main.setIsEnable(true);
        main.setProductMaterialNo(matrNo);
        for (HashMap<String, Object> ent : datas) {
            String materialNo = getMapVal(ent, "物料编码");
            if (StringUtils.isBlank(materialNo)) {
                continue;
            }
            //特征串
            String[] rulname = getMapVal(ent, "使用规则")
                    .replace("(", "").replace(")", "")
                    .replace("|", ",").replace("&", ",")
                    .split(",");
            for (String ru : rulname) {
                if (!StringUtils.isBlank(ru) && !rules.contains(ru)) {
                    rules.add(ru);
                }
            }
            BomInfo bom = new BomInfo();
            bom.setQuantity(ConvertUtils.getDecimal(getMapVal(ent, "合计用量"), BigDecimal.ONE)); //用量 合计用量
            bom.setUnit(getMapVal(ent, "计量单位")); //计量单位
            bom.setWeight(BigDecimal.ZERO);
            bom.setWeightUnit("");
            bom.setPackageCount(BigDecimal.ZERO);

            bom.setMaterialCn(getMapVal(ent, "物料名称"));
            bom.setMaterialEn("");
            bom.setMaterialNo(materialNo);

            bom.setAction("");

            bom.setLineCode("");
            bom.setShopCode("");
            bom.setOrgCode("");
            bom.setStationCode("");

            main.getBomData().add(bom);
        }
        inserChartist(rules,matrNo,attr);
        insertMaster(datas,attr);
        maintainBomAttr(main,attr);
        saveChange();
    }

    private void inserChartist(List<String> rules,String mainNo, String attr) {
        List<PmCharacteristicsDataEntity> rdatas = pmCharacteristicsDataService.getListByCodes(rules);

        MaintainCharacteristicsInfo data = new MaintainCharacteristicsInfo();
        data.setCharacteristicsVersions("");
        data.setCharacteristicsData(new ArrayList<>());
        data.setProductMaterialNo(mainNo);
        data.setIsEnable(true);
        for (PmCharacteristicsDataEntity ent : rdatas) {
            CharacteristicsInfo cnt = new CharacteristicsInfo();
            cnt.setCode(ent.getCharacteristicsCode());
            cnt.setValue(ent.getCharacteristicsValue());
            cnt.setName(ent.getCharacteristicsName());

            cnt.setValueCn(ent.getValueCn());
            cnt.setValueEn(ent.getValueEn());

            cnt.setSource(0);
            cnt.setDescriptionCn(ent.getDescriptionCn());
            cnt.setDescriptionEn(ent.getDescriptionEn());

            data.getCharacteristicsData().add(cnt);
        }

        pmProductCharacteristicsVersionsService.maintainCharacteristics(data,attr);
    }

    private void insertMaster(List<HashMap<String, Object>> datas,String attr) {
        List<PmProductMaterialMasterEntity> masterAdds = new ArrayList<>();
        for (HashMap<String, Object> ent : datas) {
            String materialNo = getMapVal(ent, "物料编码");
            if (StringUtils.isBlank(materialNo)) {
                continue;
            }
            PmProductMaterialMasterEntity bom = new PmProductMaterialMasterEntity();
            bom.setUnit(getMapVal(ent, "计量单位")); //计量单位
            bom.setPackageCount(BigDecimal.ZERO);

            bom.setPartGroup(getMapVal(ent, "模块编码")); //模块编码

            bom.setMaterialCn(getMapVal(ent, "物料名称"));
            bom.setMaterialEn("");
            bom.setMaterialNo(materialNo);

            bom.setWeight(ConvertUtils.getDecimal(getMapVal(ent, "用量"),BigDecimal.ONE)); //用量 合计用量
            bom.setWeightUnit("");
            bom.setOrg(""); //生效日期
            bom.setAttribute1(attr);

            QueryWrapper<PmProductMaterialMasterEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmProductMaterialMasterEntity::getMaterialNo, materialNo);
            if (pmProductMaterialMasterService.selectCount(qry) == 0) {
                masterAdds.add(bom);
            }
        }
        pmProductMaterialMasterService.insertBatch(masterAdds);

    }

    private String getMapVal(HashMap<String, Object> et, String key) {
        if (et == null || !et.containsKey(key) || et.get(key) == null) {
            return "";
        }
        return et.get(key).toString().trim();
    }

    @Override
    public PmProductBomVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo).eq(PmProductBomVersionsEntity::getCheckCode, checkCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public PmProductBomVersionsEntity getByProductMaterialNoBomVerson(String productMaterialNo, String bomVersions) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo).eq(PmProductBomVersionsEntity::getBomVersions, bomVersions);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 维护BOM（主要针对于一计划一BOM）
     */
    @Override
    public String maintainBom(MaintainBomDTO bomData) {
        return maintainBomAttr(bomData, "");
    }

    private String maintainBomAttr(MaintainBomDTO bomData,String attr) {
        if (StringUtils.isBlank(bomData.getProductMaterialNo())) {
            throw new InkelinkException("无效的产品物料号");
        }
        bomData.getBomData().sort(Comparator.comparing(BomInfo::getMaterialNo).thenComparingDouble(c -> c.getQuantity().doubleValue()));
        List<BomInfo> orderBom = new ArrayList<>(bomData.getBomData());
        //获取校验数据
        String verifyData = md5(JsonUtils.toJsonString(orderBom));
        //MES自己维护BOM版本
        if (StringUtils.isBlank(bomData.getBomVersions())) {
            bomData.setBomVersions(bomData.getProductMaterialNo() + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
            PmProductBomVersionsEntity bomVersionsInfo = getByProductMaterialNo(bomData.getProductMaterialNo(), verifyData);
            if (bomVersionsInfo == null) {
                //如果相同的物料号已经添加过直接返回物料号的版本
                if (bomVersions.containsKey(bomData.getProductMaterialNo())) {
                    return bomVersions.get(bomData.getProductMaterialNo());
                }
                bomVersionsInfo = new PmProductBomVersionsEntity();
                bomVersionsInfo.setId(IdGenerator.getId());
                bomVersionsInfo.setProductMaterialNo(bomData.getProductMaterialNo());
                bomVersionsInfo.setBomVersions(bomData.getBomVersions());
                bomVersionsInfo.setCheckCode(verifyData);
                bomVersionsInfo.setIsEnable(bomData.getIsEnable());
                bomVersionsInfo.setAttribute1(attr);
                //添加BOM新的信息
                insert(bomVersionsInfo);
                Long bomVersionId = bomVersionsInfo.getId();
                pmProductBomService.insertBatch(bomData.getBomData().stream().map(c -> {
                    PmProductBomEntity et = new PmProductBomEntity();
                    et.setBomVersionsId(bomVersionId);
                    et.setMaterialNo(c.getMaterialNo());
                    et.setMaterialCn(c.getMaterialCn());
                    et.setMaterialEn(c.getMaterialEn());
                    et.setQuantity(c.getQuantity());
                    et.setUnit(c.getUnit());
                    et.setOrganizationCode(c.getOrgCode());
                    et.setWorkshopCode(c.getShopCode());
                    et.setLineCode(c.getLineCode());
                    et.setStationCode(c.getStationCode());
                    et.setWeight(c.getWeight());
                    et.setWeightUnit(c.getWeightUnit());
                    et.setPackageCount(c.getPackageCount());
                    et.setAttribute1(attr);
                    return et;
                }).collect(Collectors.toList()));

                if (bomData.getIsEnable()) {
                    LambdaUpdateWrapper<PmProductBomVersionsEntity> upset = new LambdaUpdateWrapper<>();
                    upset.set(PmProductBomVersionsEntity::getIsEnable, false);
                    upset.eq(PmProductBomVersionsEntity::getProductMaterialNo, bomData.getProductMaterialNo());
                    update(upset);
                }
            }
            bomVersions.put(bomData.getProductMaterialNo(), bomVersionsInfo.getBomVersions());
            return bomVersionsInfo.getBomVersions();
        } else {
            return bomData.getBomVersions();
        }
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    /**
     * 获取BOM详细数据
     *
     * @param productMaterialNo 物料编号
     * @param bomVersions       物料版本
     * @return 物料结合
     */
    @Override
    public List<PmProductBomEntity> getBomData(String productMaterialNo, String bomVersions) {
        List<PmProductBomEntity> datas = localCache.getObject(cacheName + productMaterialNo + bomVersions);
        if (datas == null || datas.isEmpty()) {
            synchronized (LOCK_OBJ) {
                datas = localCache.getObject(cacheName + productMaterialNo + bomVersions);
                if (datas == null || datas.isEmpty()) {
                    datas = getDatas(productMaterialNo, bomVersions);
                    localCache.addObject(cacheName + productMaterialNo + bomVersions, datas, -1);
                }
            }
        }
        return datas;
    }

    /**
     * 获取BOM最新的版本
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    @Override
    public String getBomVersions(String productMaterialNo) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductBomVersionsEntity::getIsEnable, true)
                .orderByDesc(PmProductBomVersionsEntity::getCreationDate);

        PmProductBomVersionsEntity bomVersionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (bomVersionsInfo == null) {
            throw new InkelinkException("该物料[" + productMaterialNo + "]没有对应的BOM数据");
        }
        return bomVersionsInfo.getBomVersions();
    }

    /**
     * 获取版本列表
     *
     * @param productMaterialNo 物料编号
     * @return
     */
    @Override
    public List<String> getBomVersionsList(String productMaterialNo) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                .orderByDesc(PmProductBomVersionsEntity::getCreationDate);
        List<PmProductBomVersionsEntity> productBomVersions = this.selectList(qry);
        if (productBomVersions.isEmpty()) {
            return Collections.emptyList();
        }
        return productBomVersions.stream().map(PmProductBomVersionsEntity::getBomVersions).collect(Collectors.toList());
    }

    /**
     * 根据物料号查询版本号
     *
     * @param productMaterialNo
     * @return 物料号列表
     */
    @Override
    public List<PmProductBomVersionsEntity> getBomVersionsByCode(String productMaterialNo) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                .orderByDesc(PmProductBomVersionsEntity::getCreationDate);
        return this.selectList(qry);
    }

    /**
     * 设置BOM启用版本
     *
     * @param para
     * @return
     */
    @Override
    public void setBomEnable(SetBomEnablePara para) {
        PmProductBomVersionsEntity pmProductBomTarget = this.selectById(para.getId());
        if (pmProductBomTarget == null) {
            throw new InkelinkException("该版本不存在或已经被删除!");
        }
        LambdaUpdateWrapper<PmProductBomVersionsEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmProductBomVersionsEntity::getIsEnable, false)
                .eq(PmProductBomVersionsEntity::getProductMaterialNo, pmProductBomTarget.getProductMaterialNo());
        this.update(luw);
        pmProductBomTarget.setIsEnable(true);
        this.updateById(pmProductBomTarget);
        this.saveChange();
    }

    private List<PmProductBomEntity> getDatas(String productMaterialNo, String bomVersions) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductBomVersionsEntity::getBomVersions, bomVersions);

        PmProductBomVersionsEntity bomVersionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (bomVersionsInfo == null) {
            throw new InkelinkException("未找到" + productMaterialNo + "产品版本" + bomVersions + "BOM数据");
        }
        Date now = new Date();
        QueryWrapper<PmProductBomEntity> qwProductBom = new QueryWrapper<>();
        qwProductBom.lambda().eq(PmProductBomEntity::getBomVersionsId, bomVersionsInfo.getId());
        /*        .and(a -> a.isNull(PmProductBomEntity::getEffectiveDateStart).or()
                        .le(PmProductBomEntity::getEffectiveDateStart, now))
                .and(a -> a.isNull(PmProductBomEntity::getEffectiveDateEnd).or()
                        .ge(PmProductBomEntity::getEffectiveDateEnd, now));*/
        return pmProductBomService.getData(qwProductBom, false);
    }

    /**
     * 保存bom数据到逐渐
     *
     * @param productMaterialNo 物料编号
     * @return 获取bom版本信息
     */
    @Override
    public void saveBomToCom(String productMaterialNo, String bomVersions) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductBomVersionsEntity::getBomVersions, bomVersions);
        PmProductBomVersionsEntity bomVersionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (bomVersionsInfo == null) {
            throw new InkelinkException("未找到" + productMaterialNo + "产品版本" + bomVersions + "BOM数据");
        }
        List<PmProductBomEntity> boms = pmProductBomService.getByBomVersionId(bomVersionsInfo.getId());
        pmTraceComponentService.saveByBom(boms);
    }

    @Override
    public String copyBom(String productMaterialNo, String bomVersions) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda()
                //.eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                //.eq(PmProductBomVersionsEntity::getIsEnable, true);
                .eq(PmProductBomVersionsEntity::getBomVersions, bomVersions);

        PmProductBomVersionsEntity bomVersionsInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (bomVersionsInfo == null) {
            throw new InkelinkException("未找到" + productMaterialNo + "产品版本" + bomVersions + "BOM数据");
        } else {
            QueryWrapper<PmProductBomVersionsEntity> qryEt = new QueryWrapper<>();
            qryEt.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo)
                    .eq(PmProductBomVersionsEntity::getIsEnable, true)
                    .orderByDesc(PmProductBomVersionsEntity::getCreationDate);

            PmProductBomVersionsEntity oldVersion = getTopDatas(1, qryEt).stream().findFirst().orElse(null);
            if (oldVersion != null) {
                return oldVersion.getBomVersions();
            }
        }
        if (StringUtils.equals(productMaterialNo, bomVersionsInfo.getProductMaterialNo())) {
            return bomVersionsInfo.getBomVersions();
        } else {
            PmProductBomVersionsEntity newBomVersion = new PmProductBomVersionsEntity();
            newBomVersion.setProductMaterialNo(productMaterialNo);
            newBomVersion.setBomVersions(newBomVersion.getProductMaterialNo() + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
            newBomVersion.setIsEnable(true);
            newBomVersion.setCheckCode(bomVersionsInfo.getCheckCode());
            if (newBomVersion.getProductMaterialNo().length() > 31) {
                newBomVersion.setAttribute1("1");
            } else {
                newBomVersion.setAttribute1("2");
            }
            newBomVersion.setAttribute2("form" + bomVersionsInfo.getProductMaterialNo());
            newBomVersion.setId(IdGenerator.getId());
            this.insert(newBomVersion);

            List<PmProductBomEntity> boms = pmProductBomService.getByBomVersionId(bomVersionsInfo.getId());
            for (PmProductBomEntity bm : boms) {
                bm.setBomVersionsId(newBomVersion.getId());
                bm.setId(IdGenerator.getId());
            }
            pmProductBomService.insertBatch(boms, 200, false, 1);
            return newBomVersion.getBomVersions();
        }
    }

    /**
     * 根据零件号获取整车物料版本信息
     */
    @Override
    public PmProductBomVersionsEntity getVersionByMaterialNo(String materialNo, String orderCategory) {
        //区分整车物料和 零件下的物料
        List<PmProductBomVersionsEntity> lst = pmProductBomVersionsMapper.getVersionByMaterialNo(materialNo, orderCategory);
        if (lst == null || lst.isEmpty()) {
            return null;
        } else {
            return lst.stream().findFirst().orElse(null);
        }
    }

}