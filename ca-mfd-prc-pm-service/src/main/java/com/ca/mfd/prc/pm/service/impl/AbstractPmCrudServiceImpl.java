package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmBaseEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author 阳波
 * @ClassName AbstractCrudServiceImpl
 * @description: pm服务父类
 * @date 2023年09月28日
 * @version: 1.0
 */
public abstract class AbstractPmCrudServiceImpl<M extends BaseMapper<T>, T extends PmBaseEntity> extends AbstractCrudServiceImpl<M, T> {
    public void publishByWorkShopId(Long workShopId, Integer version) {
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        conditionDtos.add(new ConditionDto("prcPmWorkshopId", String.valueOf(workShopId), ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("version", "0", ConditionOper.Equal));
        List<T> pmEntitys = getData(conditionDtos);
        if (CollectionUtils.isNotEmpty(pmEntitys)) {
            List<T> collect = pmEntitys.stream().map(entity -> {
                entity.setIsDelete(true);
                entity.setVersion(version);
                return entity;
            }).collect(Collectors.toList());
            insertBatch(collect,300,false,1);
        }
    }

    @Override
    public boolean insert(T entity, boolean verify){
        return super.insert(entity,verify);
    }

    public T get(Serializable id, Integer version){
        List<ConditionDto> conditionDtos = new ArrayList<>(2);
        conditionDtos.add(new ConditionDto("id", String.valueOf(id), ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("version", String.valueOf(version), ConditionOper.Equal));
        T pmEntity = getData(conditionDtos).stream().findFirst().orElse(null);
        if (pmEntity == null) {
            throw new InkelinkException("id:["+id+"]版本:["+version+"]不存在或已经删除！");
        }
        return pmEntity;
    }

    @Override
    public T get(Serializable id){
        return get(id,0);
    }


    protected Map<String,List<Map<String,String>>> splitData(List<Map<String, String>> datas){
        Map<String,List<Map<String,String>>> target = new HashMap<>(2);
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updataList = new ArrayList<>();
        target.put("insert",insertList);
        target.put("updata",updataList);
        for(Map<String,String> eachItem : datas){
            if(StringUtils.isBlank(eachItem.get("id"))
                    || "0".equals(eachItem.get("id"))){
                insertList.add(eachItem);
            }else{
                updataList.add(eachItem);
            }
        }
        return target;
    }
}
