package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.dto.BindingUnitPara;
import com.ca.mfd.prc.eps.dto.GetCellRelationInfo;
import com.ca.mfd.prc.eps.dto.GetModuleRelationInfo;
import com.ca.mfd.prc.eps.dto.GetUnitRelationInfo;
import com.ca.mfd.prc.eps.mapper.IEpsModuleRelationMapper;
import com.ca.mfd.prc.eps.entity.EpsModuleRelationEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsModuleProductStatusEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsModuleProductStatusProvider;
import com.ca.mfd.prc.eps.service.IEpsModuleRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 电池模组关系服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsModuleRelationServiceImpl extends AbstractCrudServiceImpl<IEpsModuleRelationMapper, EpsModuleRelationEntity> implements IEpsModuleRelationService {

    @Autowired
    private PpsModuleProductStatusProvider ppsModuleProductStatusProvider;

    /**
     * 获取模组关系结构
     *
     * @param barcode
     * @return
     */
    @Override
    public GetModuleRelationInfo getModuleRelation(String barcode) {
        QueryWrapper<EpsModuleRelationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleRelationEntity::getCellBarcode, barcode);
        EpsModuleRelationEntity relationInfo = getTopDatas(1,qry).stream().findFirst().orElse(null);

        if (relationInfo == null)
        {
            throw new InkelinkException("无效的条码"+barcode);
        }

        List<EpsModuleRelationEntity> relationInfos = new ArrayList<>();

        if (!StringUtils.isBlank(relationInfo.getModuleBarcode()))
        {
            QueryWrapper<EpsModuleRelationEntity> qryLst = new QueryWrapper<>();
            qryLst.lambda().eq(EpsModuleRelationEntity::getModuleBarcode, relationInfo.getModuleBarcode());
            relationInfos = selectList(qryLst);
            //.Table.Where(c => c.ModuleBarcode == relationInfo.ModuleBarcode).ToList();
        }
        else if (!StringUtils.isBlank(relationInfo.getUnitBarcode()))
        {
            QueryWrapper<EpsModuleRelationEntity> qryLst = new QueryWrapper<>();
            qryLst.lambda().eq(EpsModuleRelationEntity::getUnitBarcode, relationInfo.getUnitBarcode());
            relationInfos = selectList(qryLst);
            //relationInfos = .Where(c => c.UnitBarcode == relationInfo.UnitBarcode).ToList();
        }

        List<String> barcodes = new ArrayList<>();

        barcodes.addAll(relationInfos.stream().map(EpsModuleRelationEntity::getModuleBarcode).collect(Collectors.toList()));
        barcodes.addAll(relationInfos.stream().map(EpsModuleRelationEntity::getUnitBarcode).collect(Collectors.toList()));
        barcodes.addAll(relationInfos.stream().map(EpsModuleRelationEntity::getCellBarcode).collect(Collectors.toList()));
        barcodes = barcodes.stream().distinct().collect(Collectors.toList());

        List<PpsModuleProductStatusEntity> statusList = ppsModuleProductStatusProvider.getListByBarCodes(barcodes);
        GetModuleRelationInfo data = new GetModuleRelationInfo();
        EpsModuleRelationEntity module = relationInfos.stream().map(c -> {
            EpsModuleRelationEntity et = new EpsModuleRelationEntity();
            et.setModuleCode(c.getModuleCode());
            et.setModuleBarcode(c.getModuleBarcode());
            return et;
        }).distinct().findFirst().orElse(null);

        data.setModuleBarcode(module.getModuleBarcode());
        data.setModuleCode(module.getModuleCode());
        data.setKey(module.getModuleBarcode());

        PpsModuleProductStatusEntity mstatus = statusList.stream().filter(c ->
                StringUtils.equals(c.getProductBarcode(), module.getModuleBarcode())).findFirst().orElse(null);
        data.setStatus(mstatus == null ? 1 : mstatus.getProductStatus());

        List<GetUnitRelationInfo> units = relationInfos.stream().map(c -> {
            EpsModuleRelationEntity et = new EpsModuleRelationEntity();
            et.setUnitCode(c.getUnitCode());
            et.setUnitBarcode(c.getUnitBarcode());
            return et;
        }).distinct().map(c -> {
            GetUnitRelationInfo et = new GetUnitRelationInfo();
            et.setModuleCode(module.getModuleCode());
            et.setModuleBarcode(module.getModuleBarcode());
            et.setUnitCode(c.getUnitCode());
            et.setUnitBarcode(c.getUnitBarcode());
            et.setKey(c.getUnitBarcode());
            return et;
        }).collect(Collectors.toList());
        data.setUnits(units);

        for (GetUnitRelationInfo item : data.getUnits()) {
            PpsModuleProductStatusEntity ustatus = statusList.stream().filter(c -> StringUtils.equals(c.getProductBarcode(), item.getUnitBarcode()))
                    .findFirst().orElse(null);
            item.setStatus(ustatus == null ? 1 : ustatus.getProductStatus());
            item.getCells().addAll(relationInfos.stream().filter(c -> StringUtils.equals(c.getUnitBarcode(), item.getUnitBarcode()))
                    .map(c -> {
                        GetCellRelationInfo et = new GetCellRelationInfo();
                        et.setModuleCode(module.getModuleCode());
                        et.setModuleBarcode(module.getModuleBarcode());
                        et.setUnitCode(item.getUnitCode());
                        et.setUnitBarcode(item.getUnitBarcode());
                        et.setCellCode(c.getCellCode());
                        et.setCellBarcode(c.getCellBarcode());
                        et.setCellType(c.getCellType());
                        et.setKey(c.getCellBarcode());
                        return et;
                    }).collect(Collectors.toList()));

            for (GetCellRelationInfo citem : item.getCells()) {
                PpsModuleProductStatusEntity cstatus = statusList.stream().filter(c ->
                                StringUtils.equals(c.getProductBarcode(), citem.getCellBarcode()))
                        .findFirst().orElse(null);
                citem.setStatus(cstatus == null ? 1 : cstatus.getProductStatus());
            }
        }
        return data;
    }

    /**
     * 绑定小单元
     *
     * @param para
     * @return
     */
    @Override
    public void bindingUnit(BindingUnitPara para) {
        QueryWrapper<EpsModuleRelationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleRelationEntity::getCellBarcode, para.getCellBarcode());
        EpsModuleRelationEntity relationInfo = getTopDatas(1,qry).stream().findFirst().orElse(null);
        if (relationInfo == null) {
            throw new InkelinkException("系统中未找到电芯" + para.getCellBarcode() + "与小单元关系");
        }
        QueryWrapper<EpsModuleRelationEntity> qryCt = new QueryWrapper<>();
        qryCt.lambda().eq(EpsModuleRelationEntity::getUnitBarcode, relationInfo.getUnitBarcode());

        List<Long> ids = selectList(qryCt).stream().map(EpsModuleRelationEntity::getId).collect(Collectors.toList());
        UpdateWrapper<EpsModuleRelationEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsModuleRelationEntity::getModuleCode, para.getModuleCode())
                .set(EpsModuleRelationEntity::getModuleBarcode, para.getModuleBarcode())
                .in(EpsModuleRelationEntity::getId, ids);
        update(upset);
    }

    /**
     * 解除小单元关系绑定
     *
     * @param barcode
     * @return
     */
    @Override
    public void deleteUnit(String barcode) {
        UpdateWrapper<EpsModuleRelationEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsModuleRelationEntity::getModuleCode, StringUtils.EMPTY)
                .set(EpsModuleRelationEntity::getModuleBarcode, StringUtils.EMPTY)
                .eq(EpsModuleRelationEntity::getUnitBarcode, barcode);
        update(upset);
    }

    /**
     * 绑定元素
     *
     * @param para
     * @return
     */
    @Override
    public void bindingCell(EpsModuleRelationEntity para) {
        QueryWrapper<EpsModuleRelationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleRelationEntity::getCellBarcode, para.getCellBarcode());
        EpsModuleRelationEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (data != null) {
            //delete(data.getId());
            throw new InkelinkException("物料" + para.getCellBarcode() + "已绑定在小单元" + data.getUnitBarcode() + "上面");
        }
        insert(para);
    }

    /**
     * 删除关系元素
     *
     * @param barcode
     * @return
     */
    @Override
    public void deleteCell(String barcode) {
        UpdateWrapper<EpsModuleRelationEntity> upset = new UpdateWrapper<>();
        upset.lambda().eq(EpsModuleRelationEntity::getCellBarcode, barcode);
        delete(upset);
    }
}