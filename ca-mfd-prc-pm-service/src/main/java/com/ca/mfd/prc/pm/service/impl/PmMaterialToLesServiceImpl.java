package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.dto.WorkstationMaterialDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmMaterialToLesEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmMaterialToLesMapper;
import com.ca.mfd.prc.pm.service.IPmMaterialToLesService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialSubService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: LES拉取工位物料清单服务实现
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Service
public class PmMaterialToLesServiceImpl extends AbstractCrudServiceImpl<IPmMaterialToLesMapper, PmMaterialToLesEntity> implements IPmMaterialToLesService {

    @Autowired
    IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;
    @Autowired
    IPmVersionService pmVersionService;
    //    @Autowired
    //    IPmWorkstationMaterialService pmWorkstationMaterialService;
    @Autowired
    IPmWorkstationMaterialSubService pmWorkstationMaterialSubService;

    /**
     * 获取LES拉取工位物料清单
     *
     * @param sigtrue
     * @return LES拉取工位物料清单
     */
    @Override
    public PmMaterialToLesEntity getPmMaterialToLesBySigtrue(String sigtrue) {
        QueryWrapper<PmMaterialToLesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmMaterialToLesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmMaterialToLesEntity::getSigtrue, sigtrue);
        lambdaQueryWrapper.eq(PmMaterialToLesEntity::getSigtrueCreate, true);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 更新发送状态
     *
     * @param sigtrue 令牌
     */
    @Override
    public void updatePmMaterialToLesStatus(String sigtrue) {
        UpdateWrapper<PmMaterialToLesEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmMaterialToLesEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(PmMaterialToLesEntity::getPullTime, new Date());
        lambdaUpdateWrapper.eq(PmMaterialToLesEntity::getSigtrue, sigtrue);
        this.update(updateWrapper);
    }

    /**
     * @param productCode 物料号
     * @param sigtrue     加密串
     */
    @Override
    public PmMaterialToLesEntity savePmMaterialToLes(String productCode, String sigtrue) {
        PmMaterialToLesEntity model = getPmMaterialToLesBySigtrue(sigtrue);
        if (model != null) {
            return model;
        }
        PmMaterialToLesEntity entity = new PmMaterialToLesEntity();
        entity.setId(IdGenerator.getId());
        entity.setProductCode(productCode);
        entity.setSigtrue(sigtrue);
        entity.setSigtrueCreate(false);
        this.save(entity);
        return entity;
    }

    /**
     * 构建工位物料数据
     */
    @Override
    public void createPmMaterialToLes() {
        QueryWrapper<PmMaterialToLesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmMaterialToLesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmMaterialToLesEntity::getSigtrueCreate, false);
        List<PmMaterialToLesEntity> list = this.selectList(queryWrapper);
        getPmMaterial(list);
    }

    private void getPmMaterial(List<PmMaterialToLesEntity> list) {
        if (list.isEmpty()) {
            return;
        }
        for (PmMaterialToLesEntity item : list) {
            // 使用Base64解码
            byte[] decodedBytes = Base64.getDecoder().decode(item.getSigtrue());
            String strMessage = StringUtils.toEncodedString(decodedBytes, StandardCharsets.UTF_8);
            String[] sigtrues = strMessage.split("\\|");
            if (sigtrues.length != 3) {
                continue;
            }
            String[] shops = sigtrues[2].split(",");
            String[] workShopCode = null;
            if (shops.length > 0) {
                List<String> arrayList = new ArrayList<String>();
                if (shops.length > 1) {
                    for (String items : shops) {
                        arrayList.add(items);
                    }
                    workShopCode = arrayList.toArray(new String[0]);
                } else {
                    String code = shops[0];
                    if (!code.equals("All")) {
                        workShopCode[0] = code;
                    }
                }
            }
            List<PmWorkstationMaterialEntity> materialList = pmWorkstationMaterialSubService.getPmMaterial(item.getProductCode(), workShopCode,
                    null,"1",DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
            if (!materialList.isEmpty()) {
                List<WorkstationMaterialDto> workstationMaterialDtos = new ArrayList<>();
                PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();
                for (PmWorkstationMaterialEntity workItems : materialList) {
                    WorkstationMaterialDto info = new WorkstationMaterialDto();
                    PmWorkStationEntity workStationEntitie = pmAllDTO.getStations().stream()
                            .filter(s -> StringUtils.equals(s.getWorkstationCode(), workItems.getWorkstationCode())).findFirst().orElse(null);
                    info.setWorkshopId(workItems.getPrcPmWorkshopId());
                    info.setWorkshopCode(workItems.getWorkshopCode());
                    info.setLineId(workItems.getPrcPmLineId());
                    info.setLineCode(workItems.getLineCode());
                    info.setWorkstationId(workItems.getPrcPmWorkstationId());
                    info.setWorkstationCode(workItems.getWorkstationCode());
                    info.setMaterialCode(workItems.getMaterialNo());
                    info.setMaterialName(workItems.getMasterChinese());
                    info.setWorkstationUseQuantity(workItems.getMaterialNum());
                    info.setWorkstationDisplay(workStationEntitie.getDirection());
                    info.setWorkstationSeq(workStationEntitie.getWorkstationNo());
                    workstationMaterialDtos.add(info);
                }
                String materialContetn = JsonUtils.toJsonString(workstationMaterialDtos);
                UpdateWrapper<PmMaterialToLesEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<PmMaterialToLesEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.set(PmMaterialToLesEntity::getMaterialContetn, materialContetn);
                lambdaUpdateWrapper.set(PmMaterialToLesEntity::getSigtrueCreate, true);
                lambdaUpdateWrapper.eq(PmMaterialToLesEntity::getId, item);
                this.update(updateWrapper);
            }
        }
    }


    @Override
    public void getPmMaterialBak(Long id) {
        List<WorkstationMaterialDto> workstationMaterialDtos = new ArrayList<>();
        PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();
        WorkstationMaterialDto info = new WorkstationMaterialDto();
        info.setWorkshopId(1706580489323896862L);
        info.setWorkshopCode("WE");
        info.setLineId(1706580489323897956L);
        info.setLineCode("EH");
        info.setWorkstationId(1706580489323897959L);
        info.setWorkstationCode("IC03");
        info.setMaterialCode("X1P03010000005Z08W10CN000");
        info.setMaterialName("");
        info.setWorkstationUseQuantity(152D);
        info.setWorkstationDisplay(20);
        info.setWorkstationSeq(3);
        workstationMaterialDtos.add(info);

        String materialContetn = JsonUtils.toJsonString(workstationMaterialDtos);
        UpdateWrapper<PmMaterialToLesEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmMaterialToLesEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.set(PmMaterialToLesEntity::getMaterialContetn, materialContetn);
        lambdaUpdateWrapper.set(PmMaterialToLesEntity::getSigtrueCreate, true);
        lambdaUpdateWrapper.eq(PmMaterialToLesEntity::getId, id);
        this.update(wrapper);
    }
}