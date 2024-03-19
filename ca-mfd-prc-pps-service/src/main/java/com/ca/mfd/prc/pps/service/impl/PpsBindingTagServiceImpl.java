package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.entity.PpsBindingTagEntity;
import com.ca.mfd.prc.pps.entity.PpsBindingTagLogEntity;
import com.ca.mfd.prc.pps.mapper.IPpsBindingTagMapper;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsBindingTagLogService;
import com.ca.mfd.prc.pps.service.IPpsBindingTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: 吊牌绑定管理服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsBindingTagServiceImpl extends AbstractCrudServiceImpl<IPpsBindingTagMapper, PpsBindingTagEntity> implements IPpsBindingTagService {

    @Autowired
    private IPpsBindingTagLogService ppsBindingTagLogService;
    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Override
    public PpsBindingTagEntity getFirstByCode(String tagCode) {
        QueryWrapper<PpsBindingTagEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsBindingTagEntity::getTagCode, tagCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 绑定吊牌
     *
     * @param tagNo
     * @param vin
     * @param bindingAviCode
     * @return
     */
    @Override
    public void bindingTag(String tagNo, String vin, String bindingAviCode,Integer bindingMedium) {
        if (StringUtils.isBlank(vin)) {
            vin = "";
        }
        if (StringUtils.isBlank(bindingAviCode)) {
            bindingAviCode = "";
        }
        if (StringUtils.isBlank(tagNo)) {
            throw new InkelinkException("无效的吊牌信息");
        }
        tagNo = tagNo.trim();
        //unbindTag(tagNo);
        QueryWrapper<PpsBindingTagEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsBindingTagEntity::getTagCode, tagNo);
        PpsBindingTagEntity tagInfo = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (tagInfo != null) {
            throw new InkelinkException("吊牌" + tagNo + "已绑定车辆，无法绑定");
        }
        int bindingPointType = 1;
        String finalBindingAviCode = bindingAviCode;
        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis().stream().filter(c -> StringUtils.equals(c.getAviCode(), finalBindingAviCode))
                .findFirst().orElse(null);
        if (aviInfo != null) {
            bindingPointType = 2;
        }

        PpsBindingTagEntity et = new PpsBindingTagEntity();
        et.setBarcode(vin);
        et.setBindingAviCode(bindingAviCode);
        et.setBindingPointType(bindingPointType);
        et.setBindingMedium(bindingMedium);
        et.setTagCode(tagNo);

        this.insert(et);

        PpsBindingTagLogEntity taglog = new PpsBindingTagLogEntity();
        taglog.setBarcode(vin);
        taglog.setBindingAviCode(bindingAviCode);
        taglog.setBindingPointType(bindingPointType);
        taglog.setTagCode(tagNo);
        taglog.setBindingType(1);
        taglog.setBindingMedium(bindingMedium);
        ppsBindingTagLogService.insert(taglog);
    }

    /**
     * 解绑吊牌
     *
     * @param tagNo
     */
    @Override
    public void unbindTag(String tagNo) {
        tagNo = tagNo.trim();
        QueryWrapper<PpsBindingTagEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsBindingTagEntity::getTagCode, tagNo);
        List<PpsBindingTagEntity> datas = selectList(qry);
        if (datas.size() == 0) {
            throw new InkelinkException("吊牌：" + tagNo + ",未绑定任何数据");
        }

        for (PpsBindingTagEntity item : datas) {
            PpsBindingTagLogEntity taglog = new PpsBindingTagLogEntity();
            taglog.setBarcode(item.getBarcode());
            taglog.setBindingAviCode(item.getBindingAviCode());
            taglog.setBindingPointType(item.getBindingPointType());
            taglog.setTagCode(item.getTagCode());
            taglog.setBindingMedium(item.getBindingMedium());
            taglog.setBindingType(2);
            ppsBindingTagLogService.insert(taglog);
        }
        UpdateWrapper<PpsBindingTagEntity> delUp = new UpdateWrapper<>();
        delUp.lambda().eq(PpsBindingTagEntity::getTagCode, tagNo);
        delete(delUp);
    }

}