package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.eps.dto.CollectModuleWoItem;
import com.ca.mfd.prc.eps.dto.CollectModuleWoPara;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataItemEntity;
import com.ca.mfd.prc.eps.mapper.IEpsModuleWoDataMapper;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataEntity;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsModuleProductStatusEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsModuleProductStatusProvider;
import com.ca.mfd.prc.eps.service.IEpsModuleWoDataItemService;
import com.ca.mfd.prc.eps.service.IEpsModuleWoDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @Description: 模组工艺数据服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsModuleWoDataServiceImpl extends AbstractCrudServiceImpl<IEpsModuleWoDataMapper, EpsModuleWoDataEntity> implements IEpsModuleWoDataService {

    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PpsModuleProductStatusProvider ppsModuleProductStatusProvider;
    @Autowired
    private IEpsModuleWoDataItemService epsModuleWoDataItemService;

    /**
     * 收集预成组检测数据
     *
     * @param para
     */
    @Override
    public void collectModuleWo(CollectModuleWoPara para) {
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        //获取工位对象
        PmWorkStationEntity workstationInfo = pmall.getStations().stream().filter(c ->
                        StringUtils.equals(c.getWorkstationCode(), para.getWorkstationCode()))
                .findFirst().orElse(null);
        if (workstationInfo == null) {
            throw new InkelinkException("工位" + para.getWorkstationCode() + "不存在");
        }
        //获取线体对象
        PmLineEntity lineInfo = pmall.getLines().stream().filter(c -> Objects.equals(c.getId(), workstationInfo.getPrcPmLineId()))
                .findFirst().orElse(null);
        //删除历史数据
        QueryWrapper<EpsModuleWoDataEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleWoDataEntity::getSn, para.getSn())
                .eq(EpsModuleWoDataEntity::getDecviceName, para.getWoName());
        EpsModuleWoDataEntity oldData = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (oldData != null) {
            this.delete(oldData.getId());
            UpdateWrapper<EpsModuleWoDataItemEntity> upItem = new UpdateWrapper<>();
            upItem.lambda().eq(EpsModuleWoDataItemEntity::getPrcEpsModuleWoDataId, oldData.getId());
            epsModuleWoDataItemService.delete(upItem);

            ppsModuleProductStatusProvider.deleteByProductBarcode(para.getSn());
        }

        //更新产品状态
        PpsModuleProductStatusEntity proStatus = new PpsModuleProductStatusEntity();
        proStatus.setProductStatus(para.getResult());
        proStatus.setProductBarcode(para.getSn());
        ppsModuleProductStatusProvider.insertModel(proStatus);

        //添加检测主体数据
        EpsModuleWoDataEntity dataInfo = new EpsModuleWoDataEntity();
        dataInfo.setId(IdGenerator.getId());
        dataInfo.setSn(para.getSn());
        dataInfo.setLineCode(lineInfo.getLineCode());
        dataInfo.setWorkstationCode(para.getWorkstationCode());
        dataInfo.setProductType(para.getProductType());
        dataInfo.setDecviceName(para.getWoName());
        dataInfo.setDataTableName("PRC_EPS_MODULE_WO_DATA_ITEM");
        dataInfo.setResult(para.getResult());
        insert(dataInfo);

        //添加检测项
        List<EpsModuleWoDataItemEntity> items = new ArrayList<>();
        int i = 1;
        for (CollectModuleWoItem item : para.getItems()) {
            EpsModuleWoDataItemEntity et = new EpsModuleWoDataItemEntity();
            et.setPrcEpsModuleWoDataId(dataInfo.getId());
            et.setDisplayNo(i);
            et.setWoName(item.getWoName());
            et.setWoCode(item.getWoCode());
            et.setWoUplimit(item.getWoUplimit());
            et.setWoDownlimit(item.getWoDownlimit());
            et.setWoStandard(item.getWoStandard());
            et.setWoValue(item.getWoValue());
            et.setWoResult(item.getWoResult());
            et.setWoUnit(item.getWoUnit());
            items.add(et);
            i++;
        }
        if (items.size() > 0) {
            epsModuleWoDataItemService.insertBatch(items);
        }
    }

    /**
     * 获取检测数据
     *
     * @param sn
     * @return
     */
    @Override
    public List<EpsModuleWoDataEntity> getModuleWoBySn(String sn) {
        QueryWrapper<EpsModuleWoDataEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsModuleWoDataEntity::getSn, sn);
        List<EpsModuleWoDataEntity> dataInfos = selectList(qry);
        for (EpsModuleWoDataEntity item : dataInfos) {
            item.setItems(epsModuleWoDataItemService.getByWoDataId(item.getId()));
        }
        return dataInfos;
    }
}