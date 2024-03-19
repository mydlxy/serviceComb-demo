package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ClassUtil;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmToolJobMapper;
import com.ca.mfd.prc.pm.mapper.IPmToolMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.service.IPmToolService;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink ${email}
 * @Description: 工具
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmToolServiceImpl extends AbstractPmCrudServiceImpl<IPmToolMapper, PmToolEntity> implements IPmToolService {

    @Autowired
    private IPmToolJobMapper pmToolJobDao;
    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;
    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    @Resource
    IPmToolMapper pmToolDao;
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_TOOL";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmToolEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmToolEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmToolEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmToolEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmToolEntity entity) {
        ClassUtil.validNullByNullAnnotation(entity);
        validData(entity);
    }


    private void validData(PmToolEntity model) {
        if(model.getPrcPmWorkshopId() == null || model.getPrcPmWorkshopId() == 0
                || model.getPrcPmLineId() == null || model.getPrcPmLineId() == 0){
            validWorkStation(model);
        }
    }

    private void validWorkStation(PmToolEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        String msg = "编码";
        String res = entity.getWorkstationCode();
        if(entity.getPrcPmWorkstationId() != null && entity.getPrcPmWorkstationId() > 0){
            lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
            msg = "ID";
            res = String.valueOf(entity.getPrcPmWorkstationId());
        }else{
            lwq.eq(PmWorkStationEntity::getWorkstationCode, entity.getWorkstationCode());
        }
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = this.pmWorkStationMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException(String.format("工具编码[%s]对应的工位%s[%s]不存在",entity.getToolCode(),msg,res));
        }
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
        entity.setPrcPmWorkshopId(pmWorkStationEntity.getPrcPmWorkshopId());
    }

    @Override
    public void beforeInsert(PmToolEntity entity) {
        ClassUtil.validNullByNullAnnotation(entity);
        validData(entity);
    }

    @Override
    public List<PmToolEntity> getAllDatas() {
        List<PmToolEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public boolean isScrewPicture(String toolId) {
        PmToolEntity toolInfo = pmToolDao.selectOne(Wrappers.lambdaQuery(PmToolEntity.class)
                .eq(PmToolEntity::getId, toolId)
                .eq(PmToolEntity::getVersion, 0)
                .last("limit 1"));
        if (toolInfo == null) {
            return false;
        }
        //以太网的 拧紧枪
        return toolInfo.getToolType() == 1;
    }

    @Override
    public List<PmToolEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmToolEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolEntity::getPrcPmWorkstationId, parentId);
        lambdaQueryWrapper.eq(PmToolEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmToolEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmToolEntity::getDisplayNo);
        return this.pmToolDao.selectList(queryWrapper);
    }


    @Override
    public List<PmToolEntity> getByShopId(Long shopId) {
        return pmToolDao.selectList(Wrappers.lambdaQuery(PmToolEntity.class)
                .eq(PmToolEntity::getPrcPmWorkshopId, shopId)
                .eq(PmToolEntity::getVersion, 0)
                .eq(PmToolEntity::getIsDelete, false)
                .orderByAsc(PmToolEntity::getDisplayNo));
    }

    @Override
    public List<PmToolEntity> getPmToolEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmToolEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmToolEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmToolEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmToolEntity::getIsDelete, flags);
        return pmToolDao.selectList(queryWrapper);
    }

    @Override
    public List<PmToolEntity> getPmToolEntityByToolCode(Long stationId, String toolCode, Boolean flags) {
        QueryWrapper<PmToolEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolEntity::getPrcPmWorkstationId, stationId);
        lambdaQueryWrapper.eq(PmToolEntity::getToolCode, toolCode);
        lambdaQueryWrapper.eq(PmToolEntity::getIsDelete, flags);
        return pmToolDao.selectList(queryWrapper);
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmToolMap = new HashMap<>(11);
        pmToolMap.put("workshopCode", "车间代码");
        pmToolMap.put("lineCode", "线体代码");
        pmToolMap.put("workstationCode", "工位代码");
        pmToolMap.put("toolCode", "工具号");
        pmToolMap.put("toolName", "名称");
        pmToolMap.put("toolType", "工具类型");
        pmToolMap.put("brand", "品牌");
        pmToolMap.put("netType", "交互方式");
        pmToolMap.put("ip", "IP");
        pmToolMap.put("port", "DB端口");
        pmToolMap.put("remark", "备注");
        pmToolMap.put("deleteFlag", "是否删除");
        pmToolMap.put("enableFlag", "是否启用");
        return pmToolMap;
    }

    @Override
    public PmToolEntity get(Serializable id) {
        QueryWrapper<PmToolEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolEntity::getId, id);
        lambdaQueryWrapper.eq(PmToolEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmToolEntity::getVersion, 0);
        List<PmToolEntity> pmToolEntityList = pmToolDao.selectList(queryWrapper);
        return pmToolEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void update(PmToolEntity pmToolEntity) {
        beforeUpdate(pmToolEntity);
        LambdaUpdateWrapper<PmToolEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmToolEntity::getToolCode, pmToolEntity.getToolCode());
        luw.set(PmToolEntity::getRemark, pmToolEntity.getRemark());
        luw.set(PmToolEntity::getToolType, pmToolEntity.getToolType());
        luw.set(PmToolEntity::getDisplayNo, pmToolEntity.getDisplayNo());
        luw.set(PmToolEntity::getBrand, pmToolEntity.getBrand());
        luw.set(PmToolEntity::getIp, pmToolEntity.getIp());
        luw.set(PmToolEntity::getNetType, pmToolEntity.getNetType());
        luw.set(PmToolEntity::getPort, pmToolEntity.getPort());
        luw.set(PmToolEntity::getToolName, pmToolEntity.getToolName());
        luw.set(PmToolEntity::getIsEnable, pmToolEntity.getIsEnable());
        luw.eq(PmToolEntity::getId, pmToolEntity.getId());
        luw.eq(PmToolEntity::getVersion, pmToolEntity.getVersion());
        this.update(luw);
    }

    @Override
    public void delete(Serializable[] ids) {
        QueryWrapper<PmToolJobEntity> qw = new QueryWrapper<>();
        qw.lambda().in(PmToolJobEntity::getPrcPmToolId, ids).eq(PmToolJobEntity::getIsDelete,false);
        List<PmToolJobEntity> jobs = pmToolJobDao.selectList(qw);
        if(CollectionUtils.isNotEmpty(jobs)){
            throw new InkelinkException("工具下有job不能删除");
        }
        super.delete(ids);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmToolEntity> listOfPmTool = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmTool, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmTool, currentUnDeployData);
    }

    @Override
    public ResultVO<List<ComboInfoDTO>> getToolComboInfo(Long workplaceId) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        QueryWrapper<PmToolEntity> toolQuery = new QueryWrapper<>();
        toolQuery.lambda().eq(PmToolEntity::getVersion, 0).eq(PmToolEntity::getIsDelete, 0).eq(Objects.nonNull(workplaceId), PmToolEntity::getPrcPmWorkstationId, workplaceId);
        List<PmToolEntity> pmWoStream = this.selectList(toolQuery);
        if (CollectionUtils.isNotEmpty(pmWoStream)) {
            List<ComboInfoDTO> list = new ArrayList<>();
            pmWoStream.forEach(t -> {
                ComboInfoDTO comboInfoDTO = new ComboInfoDTO();
                comboInfoDTO.setValue(String.valueOf(t.getId()));
                comboInfoDTO.setText(t.getToolName());
                list.add(comboInfoDTO);
            });
            result.ok(list);
            return result;
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(new ArrayList<>());
    }

    @Override
    public ResultVO<List<PmToolEntity>> getToolCodeAndName(Long workplaceId) {
        QueryWrapper<PmToolEntity> toolQuery = new QueryWrapper<>();
        toolQuery.lambda().eq(PmToolEntity::getVersion, 0).eq(Objects.nonNull(workplaceId), PmToolEntity::getPrcPmWorkstationId, workplaceId);
        List<PmToolEntity> tools = this.selectList(toolQuery);
        if(!tools.isEmpty()){
            QueryWrapper<PmToolJobEntity> toolJobQuery = new QueryWrapper<>();
            LambdaQueryWrapper<PmToolJobEntity> lqw = toolJobQuery.lambda();
            lqw.in(PmToolJobEntity:: getPrcPmToolId,tools.stream().map(PmToolEntity::getId).collect(Collectors.toList()));
            lqw.eq(PmToolJobEntity:: getIsDelete,false);
            List<PmToolJobEntity> toolJobs =  pmToolJobDao.selectList(lqw);
            Map<Long,List<PmToolJobEntity>> jobsByToolId = new HashMap<>(tools.size());
            if(!toolJobs.isEmpty()){
                for(PmToolJobEntity job : toolJobs){
                    jobsByToolId.computeIfAbsent(job.getPrcPmToolId(),v->new ArrayList<>()).add(job);
                }
            }
            for(PmToolEntity tool : tools){
                List<PmToolJobEntity> jobs = jobsByToolId.get(tool.getId());
                if(jobs == null){
                    tool.setPmToolJobEntity(new ArrayList<>());
                }else{
                    tool.setPmToolJobEntity(jobs);
                }
            }
        }
        return new ResultVO<List<PmToolEntity>>().ok(tools);
    }



    @Override
    protected void setBooleanVal(Map<String, String> eachRowData) {
        Set<String> booleanKeySet = BOOLEAN_FIELDS_MAPPING.keySet();
        Set<String> allKeySet = eachRowData.keySet();
        Map<String, String> appendMap = new HashMap<>(2);
        for (String eachField : allKeySet) {
            if (booleanKeySet.contains(eachField)) {
                boolean flag = TRUE_BOOL.equalsIgnoreCase(eachRowData.get(eachField))
                        || TRUE_NUM.equals(eachRowData.get(eachField))
                        || TRUE_CHINESE.equals(eachRowData.get(eachField));
                appendMap.put(BOOLEAN_FIELDS_MAPPING.get(eachField), String.valueOf(flag));
            }
        }
        eachRowData.putAll(appendMap);
    }

    private void setForeignKey(List<PmToolEntity> listOfPmTool, PmAllDTO pmAllDTO) {
        if (listOfPmTool.isEmpty()) {
            return;
        }
        for (PmToolEntity pmToolEntity : listOfPmTool) {
            setForeignKey(pmToolEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmToolEntity pmToolEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmToolEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("Tool名称[" + pmToolEntity.getToolName() + "]对应的车间编码(内部编码)[" + pmToolEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmToolEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmToolEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmToolEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("Tool名称[" + pmToolEntity.getToolName() + "]对应的线体编码[" + pmToolEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmToolEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmToolEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmToolEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmToolEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("Tool名称[" + pmToolEntity.getToolName() + "]对应的线体编码[" + pmToolEntity.getLineCode() + "]对应的工位编码[" + pmToolEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmToolEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmToolEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        Map<Long, Set<String>> mapOfToolNameByStationId = new HashMap(16);
        //Map<Long,Set<Integer>> mapOfToolDisplayNoByStationId = new HashMap(16);
        Map<Long, Set<String>> mapOfToolCodeByStationId = new HashMap(16);
        for (PmToolEntity tool : listEntity) {
            ClassUtil.validNullByNullAnnotation(tool);
            //验证顺序
            //verifyToolDisplayNo(tool,mapOfToolDisplayNoByStationId);
            //验证名称
            verifyToolName(tool, mapOfToolNameByStationId);
            //验证编码
            verifyToolCode(tool, mapOfToolCodeByStationId);

            PmToolEntity existTool = currentUnDeployData.getTools().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkshopId(), tool.getPrcPmWorkshopId())
                                    && Objects.equals(item.getPrcPmLineId(), tool.getPrcPmLineId())
                                    && Objects.equals(item.getPrcPmWorkstationId(), tool.getPrcPmWorkstationId())
                                    && Objects.equals(item.getToolCode(), tool.getToolCode()))
                    .findFirst().orElse(null);
            if (existTool != null) {
                PmToolEntity existSameDisplayNoTool = currentUnDeployData.getTools().stream().filter(
                                item -> Objects.equals(item.getPrcPmWorkshopId(), tool.getPrcPmWorkshopId())
                                        && Objects.equals(item.getPrcPmLineId(), tool.getPrcPmLineId())
                                        && Objects.equals(item.getPrcPmWorkstationId(), tool.getPrcPmWorkstationId())
                                        && Objects.equals(item.getToolName(), tool.getToolName())
                                        && !Objects.equals(item.getId(), existTool.getId()))
                        .findFirst().orElse(null);
                if (existSameDisplayNoTool != null) {
                    throw new InkelinkException("车间[" + tool.getWorkshopCode() + "]>线体[" + tool.getLineCode() + "]>工位[" + tool.getWorkstationCode() + "]>工具名称[" + tool.getDisplayNo() + "]已经存在");
                }
                LambdaUpdateWrapper<PmToolEntity> luw = new LambdaUpdateWrapper();

                luw.set(PmToolEntity::getToolCode, tool.getToolCode());
                luw.set(PmToolEntity::getToolName, tool.getToolName());
                luw.set(PmToolEntity::getToolType, tool.getToolType());
                luw.set(PmToolEntity::getBrand, tool.getBrand());
                luw.set(PmToolEntity::getNetType, tool.getNetType());
                luw.set(PmToolEntity::getIp, tool.getIp());
                luw.set(PmToolEntity::getPort, tool.getPort());
                luw.set(PmToolEntity::getRemark, tool.getRemark());
                luw.set(PmToolEntity::getIsDelete, tool.getIsDelete());
                luw.set(PmToolEntity::getIsEnable, tool.getIsEnable());

                luw.eq(PmToolEntity::getId, existTool.getId());
                luw.eq(PmToolEntity::getVersion, 0);
                this.update(luw);
            } else if (!tool.getIsDelete()) {
                PmToolEntity existSameDisplayNoTool = currentUnDeployData.getTools().stream().filter(
                                item -> Objects.equals(item.getPrcPmWorkshopId(), tool.getPrcPmWorkshopId())
                                        && Objects.equals(item.getPrcPmLineId(), tool.getPrcPmLineId())
                                        && Objects.equals(item.getPrcPmWorkstationId(), tool.getPrcPmWorkstationId())
                                        && Objects.equals(item.getToolName(), tool.getToolName()))
                        .findFirst().orElse(null);
                if (existSameDisplayNoTool != null) {
                    throw new InkelinkException("车间[" + tool.getWorkshopCode() + "]>线体[" + tool.getLineCode() + "]>工位[" + tool.getWorkstationCode() + "]>工具名称[" + tool.getToolName() + "]已经存在");
                }
                tool.setVersion(0);
                this.insert(tool);
            }
        }

    }

    private void verifyToolCode(PmToolEntity tool, Map<Long, Set<String>> mapOfToolCodeByStationId) {
        Set<String> setOfToolSequence = mapOfToolCodeByStationId.computeIfAbsent(tool.getPrcPmWorkstationId(), v -> new HashSet<>());
        if (setOfToolSequence.contains(tool.getToolCode())) {
            throw new InkelinkException("车间[" + tool.getWorkshopCode() + "]>线体[" + tool.getLineCode() + "]>工位[" + tool.getWorkstationCode() + "]>工具编码[" + tool.getToolCode() + "]重复");
        }
        setOfToolSequence.add(tool.getToolCode());
    }

    private void verifyToolDisplayNo(PmToolEntity tool, Map<Long, Set<Integer>> mapOfToolDisplayNoByStationId) {
        Set<Integer> setOfToolDisplayNo = mapOfToolDisplayNoByStationId.computeIfAbsent(tool.getPrcPmWorkstationId(), v -> new HashSet<>());
        if (setOfToolDisplayNo.contains(tool.getDisplayNo())) {
            throw new InkelinkException("车间[" + tool.getWorkshopCode() + "]>线体[" + tool.getLineCode() + "]>工位[" + tool.getWorkstationCode() + "]>工具序号[" + tool.getDisplayNo() + "]重复");
        }
        setOfToolDisplayNo.add(tool.getDisplayNo());
    }

    private void verifyToolName(PmToolEntity pullCord, Map<Long, Set<String>> mapOfToolNameByStationId) {
        Set<String> setOfToolName = mapOfToolNameByStationId.computeIfAbsent(pullCord.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfToolName.contains(pullCord.getToolName())) {
            throw new InkelinkException("车间[" + pullCord.getWorkshopCode() + "]>线体[" + pullCord.getLineCode() + "]>工位[" + pullCord.getWorkstationCode() + "]>工具名称[" + pullCord.getToolName() + "]重复");
        }
        setOfToolName.add(pullCord.getToolName());
    }
}