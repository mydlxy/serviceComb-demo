package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSnConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsEntryConfigMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSequenceNumberProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 工单订阅配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsEntryConfigServiceImpl extends AbstractCrudServiceImpl<IPpsEntryConfigMapper, PpsEntryConfigEntity> implements IPpsEntryConfigService {

    private static final Integer ENTRY_OPC_CONNECT_LEN = 4;
    private static final String CHAR_SPLIT = ";";
    @Autowired
    private SysSequenceNumberProvider sysSequenceNumberProvider;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;

    /**
     * 根据shopCode.areaCodes 查询
     *
     * @param shopCode  车间code
     * @param areaCodes areaCodes
     * @return 对列表集合
     */
    @Override
    public List<PpsEntryConfigEntity> getPpsEntryConfigListByShopCode(String shopCode, Integer generateType, List<String> areaCodes) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getWorkshopCode, shopCode);
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getGenerateType, generateType);
        lambdaQueryWrapper.in(PpsEntryConfigEntity::getLineCode, areaCodes);
        return this.selectList(queryWrapper);
    }

    @Override
    public List<PpsEntryConfigEntity> getPpsEntryConfigListByShopCode(String shopCode, Integer generateType, String planModel) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getWorkshopCode, shopCode);
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getGenerateType, generateType);
        lambdaQueryWrapper.like(PpsEntryConfigEntity::getModel, planModel);
        return this.selectList(queryWrapper);
    }

    @Override
    public PpsEntryConfigEntity getFirstByShopCode(String shopCode, Integer generateType, String areaCode) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getWorkshopCode, shopCode);
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getGenerateType, generateType);
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getLineCode, areaCode);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryConfigEntity getFirstByLineCode(String lineCode, String planModel) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getLineCode, lineCode);
        lambdaQueryWrapper.like(PpsEntryConfigEntity::getModel, planModel);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryConfigEntity getFirstByLineCode(String lineCode) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getLineCode, lineCode);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryConfigEntity getFirstBySubCode(String subCode) {
        QueryWrapper<PpsEntryConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryConfigEntity::getSubCode, subCode);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public void beforeUpdate(PpsEntryConfigEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PpsEntryConfigEntity model) {
        validData(model);
    }

    private void validData(PpsEntryConfigEntity model) {
        if (StringUtils.isBlank(model.getWorkshopCode())) {
            throw new InkelinkException("车间信息未传入");
        }
        if (StringUtils.isBlank(model.getLineCode())) {
            throw new InkelinkException("线体信息未传入");
        }
        if (StringUtils.isBlank(model.getSubCode())) {
            throw new InkelinkException("订阅代码不存在");
        }
        if (model.getSubObject() == 1) {
            if (model.getEntryOpcConnect().split(CHAR_SPLIT).length < ENTRY_OPC_CONNECT_LEN) {
                throw new InkelinkException("PLC链接地址错误(格式参考10.10.10.1;102;0;1)");
            }
        }
        //订阅码不能重复
        validDataUnique(model.getId(), "SUB_CODE", model.getSubCode(), "已经存在订阅码为%s的数据", "", "");

       /* QueryWrapper<PpsEntryConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryConfigEntity::getLineCode, model.getLineCode())
                .ne(PpsEntryConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("线体" + model.getLineCode() + "已配置分线工单，不能重复配置");
        }*/

    }

    @Override
    public void afterUpdate(PpsEntryConfigEntity model) {
        addSeqConfig(model);
    }

    @Override
    public void afterInsert(PpsEntryConfigEntity model) {
        addSeqConfig(model);
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        List<String> categorys = new ArrayList<>();
        for (Serializable id : idList) {
            PpsEntryConfigEntity model = this.get(id);
            String key = StringUtils.EMPTY;
            if (model.getSubCode() != null && model.getSubCode().length() > 0) {
                key = model.getWorkshopCode() + "_" + model.getLineCode() + "_" + model.getSubCode();
            } else {
                key = model.getWorkshopCode() + "_" + model.getLineCode();
            }
            String seqkey = "PpsEntry_" + key + "_Seq";
            String codekey = "PpsEntry_" + key + "_Code";
            categorys.add(seqkey);
            categorys.add(codekey);
        }
        sysSnConfigProvider.deleteByCategory(categorys);
    }

    @Override
    public void addSeqConfig(PpsEntryConfigEntity model) {
        String key = StringUtils.EMPTY;
        if (model.getSubCode() != null && model.getSubCode().length() > 0) {
            key = model.getWorkshopCode() + "_" + model.getLineCode() + "_" + model.getSubCode();
        } else {
            key = model.getWorkshopCode() + "_" + model.getLineCode();
        }
        //辅线工单顺序号生成规则
        String seqkey = "PpsEntry_" + key + "_Seq";
        List<SysSnConfigEntity> seqDatas = new ArrayList<>();
        SysSnConfigEntity seq = new SysSnConfigEntity();
        seq.setCategory(seqkey);
        seq.setDisplayNo(1);
        seq.setLength(9);
        seq.setModel("1");
        seq.setParam1("scope");
        seqDatas.add(seq);

        String codekey = "PpsEntry_" + key + "_Code";
        SysSnConfigEntity code = new SysSnConfigEntity();
        code.setCategory(codekey);
        code.setDisplayNo(1);
        code.setLength(4);
        code.setModel("1");
        code.setParam1("scope");
        seqDatas.add(code);

        sysSnConfigProvider.addSeqConfig(seqDatas);

    }

}