package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationOperBookMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import com.ca.mfd.prc.pm.service.IPmWorkStationOperBookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 岗位操作指导书
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmWorkStationOperBookServiceImpl extends AbstractCrudServiceImpl<IPmWorkStationOperBookMapper, PmWorkstationOperBookEntity> implements IPmWorkStationOperBookService {

    private static final Map<Integer,String> OPER_BOOK_TYPES = new HashMap<>(3);

    @Autowired
    private IPmWorkStationOperBookMapper workplaceOperBookMapper;
    @Autowired
    private IPmWorkShopMapper workShopMapper;
    @Autowired
    private IPmLineMapper pmLineMapper;
    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;
    @Autowired
    private SysConfigurationProvider pmSysConfigurationService;
    @Autowired
    private IPmCharacteristicsDataService pmCharacteristicsDataService;
    static {
        OPER_BOOK_TYPES.put(1,"操作图册");
        OPER_BOOK_TYPES.put(2,"零件图册");
        OPER_BOOK_TYPES.put(3,"PFMEA文件");
    }


    @Override
    public void beforeUpdate(PmWorkstationOperBookEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmWorkstationOperBookEntity model) {
        validData(model);
    }

    private void validData(PmWorkstationOperBookEntity model) {
        ClassUtil.validNullByNullAnnotation(model);
        //工位信息
        validWorkStation(model);
        //线体信息
        validLine(model);
        //车间信息
        validWorkShop(model);

        //validUniq(model);
        if (Boolean.FALSE.equals(FeatureTool.verifyExpression(model.getFeatureCode()))) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>物料号[%s]特征值[%s]无效",
                    model.getLineCode(), model.getWorkstationCode(), model.getMaterialNo(), model.getFeatureCode()));
        }

    }

//    private boolean validCharacteristic(String characteristic) {
//        List<SysConfigurationEntity> listOfVehicleModle = this.pmSysConfigurationService.getSysConfigurations(VEHICLE_MODEL);
//        List<String> listOfVehicleMasterFeatures = pmCharacteristicsDataService.getAllDatas().stream()
//                .map(i -> i.getCharacteristicsValue()).collect(Collectors.toList());
//        return BusinessValidUtils.checkVehicleOption(characteristic, listOfVehicleMasterFeatures, listOfVehicleModle);
//    }

    private void validUniq(PmWorkstationOperBookEntity model) {
        QueryWrapper<PmWorkstationOperBookEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationOperBookEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkstationOperBookEntity::getPrcPmWorkstationId, model.getPrcPmWorkstationId());
        lambdaQueryWrapper.eq(PmWorkstationOperBookEntity::getPrcPmProcessNo, model.getPrcPmProcessNo());
        lambdaQueryWrapper.eq(PmWorkstationOperBookEntity::getWoBookType, model.getWoBookType());
        lambdaQueryWrapper.ne(PmWorkstationOperBookEntity::getId, model.getId() == null ? 0 : model.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>工序号[%s]>指导书类型[%s]已经存在", model.getLineCode(), model.getWorkstationCode(), model.getPrcPmProcessNo() ,OPER_BOOK_TYPES.get(model.getWoBookType())));
        }
    }


    private void validWorkStation(PmWorkstationOperBookEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lqw = qw.lambda();
        lqw.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId().toString());
        lqw.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = pmWorkStationMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException(String.format("工位ID[%s]不存在", entity.getPrcPmWorkstationId()));
        }
        entity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
    }

    private void validLine(PmWorkstationOperBookEntity entity) {
        //线体信息
        QueryWrapper<PmLineEntity> lineQry = new QueryWrapper<>();
        lineQry.lambda().eq(PmLineEntity::getId,entity.getPrcPmLineId()).eq(PmLineEntity::getVersion,0);
        PmLineEntity pmLineEntity = pmLineMapper.selectOne(lineQry);
        if (pmLineEntity == null) {
            throw new InkelinkException(String.format("线体ID[%s]不存在", entity.getPrcPmLineId()));
        }
        entity.setLineCode(pmLineEntity.getLineCode());
        entity.setPrcPmWorkshopId(pmLineEntity.getPrcPmWorkshopId());
    }

    private void validWorkShop(PmWorkstationOperBookEntity entity) {
        //车间信息
        List<ConditionDto> shopConditionDtoList = new ArrayList<>(2);
        PmWorkShopEntity pmWorkShopEntity = workShopMapper.getByShopIdAndVersion(entity.getPrcPmWorkshopId(), 0);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException(String.format("车间ID[%s]不存在", entity.getPrcPmWorkshopId()));
        }
        entity.setPrcPmWorkshopId(pmWorkShopEntity.getId());
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
    }

    @Override
    public ResultVO<List<ComboInfoDTO>> getQualityBook() {
        LambdaQueryWrapper<PmWorkstationOperBookEntity> wr = new LambdaQueryWrapper<>();
        wr.eq(PmWorkstationOperBookEntity::getWoBookType, 2);
        List<PmWorkstationOperBookEntity> list = workplaceOperBookMapper.selectList(wr);
        List<ComboInfoDTO> arr = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(list)) {
            list.forEach(t -> {
                ComboInfoDTO dto = new ComboInfoDTO();
                dto.setText(t.getWoBookName());
                dto.setValue(t.getWoBookPath());
                arr.add(dto);
            });
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(arr);
    }

    @Override
    public ResultVO<List<PmWorkstationOperBookEntity>> getBookList(int category, String workstationCode, String bookName) {
        LambdaQueryWrapper<PmWorkstationOperBookEntity> wr = new LambdaQueryWrapper<>();
        wr.eq(PmWorkstationOperBookEntity::getWoBookType, category);

        //工位
        if (StringUtils.isNotEmpty(workstationCode)) {
            wr.in(PmWorkstationOperBookEntity::getWorkstationCode, workstationCode);
        }
        //文件名
        if (StringUtils.isNotEmpty(bookName)) {
            wr.in(PmWorkstationOperBookEntity::getWoBookName, bookName);
        }
        List<PmWorkstationOperBookEntity> list = workplaceOperBookMapper.selectList(wr);
        if (ObjectUtil.isNotEmpty(list)) {
            list.stream().sorted(Comparator.comparing(PmWorkstationOperBookEntity::getWorkstationCode))
                    .sorted(Comparator.comparing(PmWorkstationOperBookEntity::getWoBookName))
                    .collect(Collectors.toList());
        }
        return new ResultVO<List<PmWorkstationOperBookEntity>>().ok(list);
    }


    @Override
    public List<PmWorkstationOperBookEntity> getBookList(int bookType, Long workstationId, int processNo) {
        LambdaQueryWrapper<PmWorkstationOperBookEntity> wr = new LambdaQueryWrapper<>();
        wr.eq(PmWorkstationOperBookEntity::getPrcPmWorkstationId, workstationId);
        wr.eq(PmWorkstationOperBookEntity::getPrcPmProcessNo, processNo);
        wr.eq(PmWorkstationOperBookEntity::getWoBookType, bookType);
        List<PmWorkstationOperBookEntity> list = workplaceOperBookMapper.selectList(wr);
        if (ObjectUtil.isNotEmpty(list)) {
            list.stream().sorted(Comparator.comparing(PmWorkstationOperBookEntity::getWorkstationCode))
                    .sorted(Comparator.comparing(PmWorkstationOperBookEntity::getWoBookName))
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<PmWorkstationOperBookEntity> getPmWorkstationEntity(Long shopId) {
            QueryWrapper<PmWorkstationOperBookEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<PmWorkstationOperBookEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(PmWorkstationOperBookEntity::getPrcPmWorkshopId, shopId);
            lambdaQueryWrapper.eq(PmWorkstationOperBookEntity::getIsDelete, false);
            return workplaceOperBookMapper.selectList(queryWrapper);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData){
        List<PmWorkstationOperBookEntity> listOfWorkstationOperBook;
        try{
            listOfWorkstationOperBook = this.convertExcelDataToEntity(listFromExcelSheet,
                    mapSysConfigByCategory, sheetName);
        }catch (Exception e){
            log.error("操作指导书导入失败",e);
            throw new InkelinkException("操作指导书导入失败:" + e.getMessage());
        }
        //设置外键
        setForeignKey(listOfWorkstationOperBook, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfWorkstationOperBook, currentUnDeployData);
    }

    private void verifyAndSaveEntity(List<PmWorkstationOperBookEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        for (PmWorkstationOperBookEntity pmWorkstationOperBookEntity : listEntity) {
            saveOperBookFromExcel(pmWorkstationOperBookEntity,currentUnDeployData);
        }
    }

    private void saveOperBook(PmWorkstationOperBookEntity pmWorkstationOperBookEntity,PmAllDTO currentUnDeployData,
                              int woBookType){
        ClassUtil.validNullByNullAnnotation(pmWorkstationOperBookEntity);
        PmWorkstationOperBookEntity existWorkstationOperBook = currentUnDeployData.getOperBooks().stream().filter(
                        item -> Objects.equals(item.getPrcPmWorkstationId(), pmWorkstationOperBookEntity.getPrcPmWorkstationId())
                                && Objects.equals(item.getPrcPmProcessNo(), pmWorkstationOperBookEntity.getPrcPmProcessNo())
                                && Objects.equals(item.getWoBookType(), woBookType))
                .findFirst().orElse(null);
        if (existWorkstationOperBook != null) {
            LambdaUpdateWrapper<PmWorkstationOperBookEntity> luw = new LambdaUpdateWrapper();
            luw.set(PmWorkstationOperBookEntity::getWoBookName, woBookType == 1 ? "操作图册" : "零件图册");
            luw.set(PmWorkstationOperBookEntity::getWoBookPath, woBookType == 1 ? pmWorkstationOperBookEntity.getActionImage() : pmWorkstationOperBookEntity.getMaterialImage());
            luw.set(PmWorkstationOperBookEntity::getWoBookType, woBookType);
            luw.set(PmWorkstationOperBookEntity::getPrcPmProcessNo, pmWorkstationOperBookEntity.getPrcPmProcessNo());
            luw.set(PmWorkstationOperBookEntity::getMaterialNo, pmWorkstationOperBookEntity.getMaterialNo());
            luw.set(PmWorkstationOperBookEntity::getMaterialName, pmWorkstationOperBookEntity.getMaterialName());
            luw.set(PmWorkstationOperBookEntity::getIsDelete, pmWorkstationOperBookEntity.getIsDelete());
            luw.eq(PmWorkstationOperBookEntity::getId, existWorkstationOperBook.getId());
            this.update(luw);
        } else if (!pmWorkstationOperBookEntity.getIsDelete()) {
            this.insert(pmWorkstationOperBookEntity);
        }
    }

    private void saveOperBookFromExcel(PmWorkstationOperBookEntity pmWorkstationOperBookEntity,PmAllDTO currentUnDeployData){
        if(StringUtils.isNotBlank(pmWorkstationOperBookEntity.getActionImage())){
            saveOperBook(pmWorkstationOperBookEntity,currentUnDeployData,1);
        }
        if(StringUtils.isNotBlank(pmWorkstationOperBookEntity.getMaterialImage())){
            saveOperBook(pmWorkstationOperBookEntity,currentUnDeployData,2);
        }
    }

    private void setForeignKey(List<PmWorkstationOperBookEntity> listOfOperBook, PmAllDTO pmAllDTO) {
        if (listOfOperBook.isEmpty()) {
            return;
        }
        for (PmWorkstationOperBookEntity pmWorkstationOperBookEntity : listOfOperBook) {
            setForeignKey(pmWorkstationOperBookEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmWorkstationOperBookEntity pmWorkstationOperBookEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmWorkstationOperBookEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("操作指导书[" + pmWorkstationOperBookEntity.getWoBookName() + "]对应的车间编码(内部编码)[" + pmWorkstationOperBookEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmWorkstationOperBookEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmWorkstationOperBookEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmWorkstationOperBookEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("操作指导书[" + pmWorkstationOperBookEntity.getWoBookName() + "]对应的线体编码[" + pmWorkstationOperBookEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmWorkstationOperBookEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmWorkstationOperBookEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmWorkstationOperBookEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmWorkstationOperBookEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("操作指导书[" + pmWorkstationOperBookEntity.getWoBookName() + "]对应的线体编码[" + pmWorkstationOperBookEntity.getLineCode() + "]对应的工位编码[" + pmWorkstationOperBookEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmWorkstationOperBookEntity.setPrcPmWorkstationId(workStation.getId());
    }




}