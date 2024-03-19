package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pqs.dto.AttchmentDto;
import com.ca.mfd.prc.pqs.entity.PqsEntryAttchmentEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsEntryAttchmentMapper;
import com.ca.mfd.prc.pqs.service.IPqsEntryAttchmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 质检附件服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsEntryAttchmentServiceImpl extends AbstractCrudServiceImpl<IPqsEntryAttchmentMapper, PqsEntryAttchmentEntity> implements IPqsEntryAttchmentService {
    @Autowired
    IdentityHelper identityHelper;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_ENTRY_ATTCHMENT";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsEntryAttchmentEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsEntryAttchmentEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsEntryAttchmentEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取质检附件数据
     *
     * @return
     */
    @Override
    public List<PqsEntryAttchmentEntity> getAllDatas() {
        Function<Object, ? extends List<PqsEntryAttchmentEntity>> getDataFunc = (obj) -> {
            List<PqsEntryAttchmentEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsEntryAttchmentEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取附件列表
     *
     * @param inspectionNo 质检工单号
     * @return 附件列表
     */
    @Override
    public List<AttchmentDto> getAttachMent(String inspectionNo) {

        return getAllDatas().stream().filter(p -> StringUtils.equals(p.getInspectionNo(), inspectionNo)).map(t -> {
            AttchmentDto attchmentDto = new AttchmentDto();
            BeanUtils.copyProperties(t, attchmentDto);
            attchmentDto.setAttachName(t.getAttchmentName());
            return attchmentDto;
        }).collect(Collectors.toList());
    }

    /**
     * 保存附件
     *
     * @param attchmentDto 附件
     */
    @Override
    public void saveAttachMent(AttchmentDto attchmentDto) {
        PqsEntryAttchmentEntity entity = new PqsEntryAttchmentEntity();
        entity.setInspectionNo(attchmentDto.getInspectionNo());
        entity.setAttchmentName(attchmentDto.getAttachName());
        entity.setAddress(attchmentDto.getAddress());
        entity.setRemark(attchmentDto.getRemark());
        entity.setLastUpdateDate(new Date());
        entity.setLastUpdatedUser(identityHelper.getUserName() != null ? identityHelper.getUserName() : StringUtils.EMPTY);
        if (attchmentDto.getId().equals(Constant.DEFAULT_ID)) {
            entity.setId(IdGenerator.getId());
            save(entity);
        } else {
            LambdaUpdateWrapper<PqsEntryAttchmentEntity> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.eq(PqsEntryAttchmentEntity::getId, attchmentDto.getId())
                    .set(PqsEntryAttchmentEntity::getInspectionNo, entity.getInspectionNo())
                    .set(PqsEntryAttchmentEntity::getAttchmentName, entity.getAttchmentName())
                    .set(PqsEntryAttchmentEntity::getAddress, entity.getAddress())
                    .set(PqsEntryAttchmentEntity::getRemark, entity.getRemark())
                    .set(PqsEntryAttchmentEntity::getLastUpdateDate, entity.getLastUpdateDate())
                    .set(PqsEntryAttchmentEntity::getLastUpdatedUser, entity.getLastUpdatedUser());
            this.update(updateWrapper);
        }
    }

    /**
     * 删除附件列表
     *
     * @param id 附件ID
     */
    @Override
    public void deleteAttachMent(Long id) {

        delete(id);
    }
}