package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.MaterialInfoDTO;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterImgEntity;
import com.ca.mfd.prc.pm.mapper.IPmProductMaterialMasterImgMapper;
import com.ca.mfd.prc.pm.mapper.IPmProductMaterialMasterMapper;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 物料图片
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductMaterialMasterImgServiceImpl extends AbstractCrudServiceImpl<IPmProductMaterialMasterImgMapper, PmProductMaterialMasterImgEntity> implements IPmProductMaterialMasterImgService {

    private static final String cacheName = "PRC_PM_PRODUCT_MATERIAL_MASTER_IMG";
    @Autowired
    private IPmProductMaterialMasterMapper productMaterialMasterDaoMapper;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */

    @Override
    public void beforeUpdate(PmProductMaterialMasterImgEntity entity) {
        checkValue(entity);
    }

    private void checkValue(PmProductMaterialMasterImgEntity entity) {
        Long id = entity.getId();
        QueryWrapper<PmProductMaterialMasterImgEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductMaterialMasterImgEntity::getImgFileName, entity.getImgFileName())
                .ne(Objects.nonNull(id), PmProductMaterialMasterImgEntity::getId, id)
                .eq(PmProductMaterialMasterImgEntity::getIsDelete, false);
        Long count = selectCount(qry);
        if (count > 0) {
            throw new InkelinkException("文件名不能重复");
        }
    }

    @Override
    public void beforeInsert(PmProductMaterialMasterImgEntity entity) {
        checkValue(entity);
    }
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmProductMaterialMasterImgEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmProductMaterialMasterImgEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmProductMaterialMasterImgEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmProductMaterialMasterImgEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 检索物料列表
     *
     * @param searchKey 搜索关键字
     * @return
     */
    @Override
    public List<MaterialInfoDTO> getMaterialInfos(String searchKey) {
        return localCache.getObject(cacheName, (obj) -> {
            return getMaterialInfosFromDataBase(searchKey);
        }, -1);
    }

    private List<MaterialInfoDTO> getMaterialInfosFromDataBase(String searchKey) {
        QueryWrapper<PmProductMaterialMasterEntity> qw = new QueryWrapper();
        qw.lambda().like(PmProductMaterialMasterEntity::getMaterialNo, searchKey)
                .or().like(PmProductMaterialMasterEntity::getMaterialCn, searchKey);
        List<PmProductMaterialMasterEntity> pmProductMaterialMasterEntityList
                = this.productMaterialMasterDaoMapper.selectList(qw);
        if (pmProductMaterialMasterEntityList.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> materialNoList = this.selectList(new QueryWrapper<PmProductMaterialMasterImgEntity>()).stream()
                .map(PmProductMaterialMasterImgEntity::getMaterialNo).collect(Collectors.toList());
        List<MaterialInfoDTO> filteredPmProductMaterialMasterEntityList = new ArrayList<>(pmProductMaterialMasterEntityList.size());
        for (PmProductMaterialMasterEntity pmProductMaterialMasterEntity : pmProductMaterialMasterEntityList) {
            if (!materialNoList.contains(pmProductMaterialMasterEntity.getMaterialNo())) {
                filteredPmProductMaterialMasterEntityList.add(new MaterialInfoDTO(pmProductMaterialMasterEntity.getMaterialNo(),
                        pmProductMaterialMasterEntity.getMaterialCn()));
            }
        }
        return filteredPmProductMaterialMasterEntityList;
    }

}