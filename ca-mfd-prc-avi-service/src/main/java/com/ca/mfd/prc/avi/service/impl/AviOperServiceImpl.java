package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.dto.AviOperDto;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.entity.AviOperEntity;
import com.ca.mfd.prc.avi.mapper.IAviOperMapper;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviOperService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordOperService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 关键点行为配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviOperServiceImpl extends AbstractCrudServiceImpl<IAviOperMapper, AviOperEntity> implements IAviOperService {

    private static final Logger logger = LoggerFactory.getLogger(AviOperServiceImpl.class);
    private static final String cacheName = "PRC_AVI_OPER";
    private static final Object lockObj = new Object();
    @Autowired
    PmVersionProvider pmVersionProvider;
//    @Autowired
//    SysConfigurationProvider configurationProvider;
//    @Autowired
//    PpsOrderProvider ppsOrderProvider;
////    @Autowired
//    IAviTrackingRecordOperService aviTrackingRecordOperService;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AviOperEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(AviOperEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(AviOperEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<AviOperEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<AviOperEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<AviOperEntity>> getDataFunc = (obj) -> {
                List<AviOperEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<AviOperEntity> caches = localCache.getObject(cacheName, getDataFunc, 60 * 10);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<AviOperEntity> getAviOperEntityList(int aviType, String aviCode) {
        QueryWrapper<AviOperEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviOperEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviOperEntity::getAviType, aviType);
        lambdaQueryWrapper.and(s -> s.eq(AviOperEntity::getAviCode, aviCode).or().eq(AviOperEntity::getAviCode, ""));
        return selectList(queryWrapper);
    }


//    /**
//     * 后台手动获取需要执行的行为
//     *
//     * @param aviCode
//     * @param sn
//     * @param aviType
//     * @return
//     */
//    @Override
//    public ResultVO<List<AviOperEntity>> getAviOperInfo(String aviCode, String sn, int aviType) {
//        PpsOrderEntity orderInfo = ppsOrderProvider.getPpsOrderInfo(sn);
//        ResultVO<List<AviOperEntity>> result = new ResultVO<>();
//
//        if (orderInfo == null) {
//            result.setCode(-1);
//            result.setMessage("产品信息不存在");
//            return result;
//        }
//
//        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis()
//                .stream().filter(s -> s.getAviCode().equals(aviCode))
//                .findFirst().orElse(null);
//
//        if (aviInfo == null) {
//            result.setCode(-1);
//            result.setMessage("站点信息不存在");
//            return result;
//        }
//
//        try {
//            List<ComboInfoDTO> comboInfoDTOS = configurationProvider.getComboDatas("aviOperation");
//            comboInfoDTOS.add(new ComboInfoDTO("ChangeEntryStatus", "订单状态更改"));
//            comboInfoDTOS.add(new ComboInfoDTO("OrderQueueRelease", "生成过点队列"));
//            QueryWrapper<AviOperEntity> queryWrapper = new QueryWrapper<>();
//            LambdaQueryWrapper<AviOperEntity> lambdaQueryWrapper = queryWrapper.lambda();
//            lambdaQueryWrapper.eq(AviOperEntity::getAviType, aviType);
//            lambdaQueryWrapper.and(s -> s.eq(AviOperEntity::getAviCode, aviCode).or().isNull(AviOperEntity::getAviCode));
//            List<AviOperEntity> aviOperEntityList = selectList(queryWrapper);
//
//            List<AviOperDto> aviOperDtoList = new ArrayList<>();
//            for (AviOperEntity oper : aviOperEntityList) {
//                String actionName = comboInfoDTOS.stream()
//                        .filter(b -> b.getValue().equals(oper.getAction()))
//                        .findFirst()
//                        .map(ComboInfoDTO::getText)
//                        .orElse(oper.getAction());
//                AviOperDto dto = new AviOperDto();
//                dto.setPrcAviOperId(oper.getId());
//                dto.setAction(oper.getAction());
//                dto.setAviName(actionName);
//                dto.setAviCode(oper.getAviCode());
//                dto.setAviName(oper.getAviName());
//                dto.setIsEnabled(oper.getIsEnabled());
//                dto.setIsRepeat(oper.getIsRepeat());
//                aviOperDtoList.add(dto);
//
//            }
//
//            String[] aviOperaction = aviOperEntityList.stream().map(s -> s.getAction()).toArray(String[]::new);
//
//            List<AviTrackingRecordOperEntity> recorList = aviTrackingRecordOperService.getData(orderInfo.getSn(), aviCode).stream()
//                    .filter(s -> java.util.Arrays.asList(aviOperaction).contains(s.getAction()))
//                    .collect(Collectors.toList());
//
//            for (AviOperDto aviOper : aviOperDtoList) {
//                String repeat = aviOper.getIsRepeat() ? "允许重复执行行为" : "警告重复执行可能会导致数据错乱";
//                AviTrackingRecordOperEntity model = recorList.stream().filter(s -> s.getAction().equals(aviOper.getAction()))
//                        .findFirst().orElse(null);
//
//                if (model != null) {
//                    aviOper.setIsProcess(true);
//                    aviOper.setMessage(String.format("产品%s在%s时间点以拆分，执行状态%s\n%s", model.getSn(), model.getCreationDate(), model.getIsProcess(), repeat));
//                } else {
//                    aviOper.setIsProcess(false);
//                    aviOper.setMessage(String.format("产品%s还未产生行为\n%s", model.getSn(), repeat));
//                }
//            }
//
//            result.setData(aviOperEntityList);
//        } catch (Exception exe) {
//            throw new InkelinkException(exe.getMessage());
//        }
//        return result;
//    }

    @Override
    public void beforeInsert(AviOperEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviOperEntity model) {
        valid(model);
    }

    private void valid(AviOperEntity model) {
        if (StringUtils.isBlank(model.getAviCode())) {
            model.setAviCode("0");
            model.setAviName("所有AVI点");
        }
        Long recordCount = getRecordCountByCode(model.getAviCode(), model.getAction(),
                model.getAviType(), model.getId());
        if (recordCount > 0) {
            throw new InkelinkException("该AVI已经配置过这个行为");
        }
        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo != null) {
            model.setAviName(aviInfo.getAviName());
        }
        PmLineEntity lineEntity;
        if (aviInfo != null) {
            lineEntity = pmAllDtoResultVo.getLines().stream().filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId()))
                    .findFirst().orElse(null);
            if (lineEntity != null) {
                model.setLineCode(lineEntity.getLineCode());
            }
        } else {
            lineEntity = null;
        }

        PmWorkShopEntity shopEntity = null;
        if (lineEntity != null) {
            shopEntity = pmAllDtoResultVo.getShops().stream().filter(s -> Objects.equals(s.getId(), lineEntity.getPrcPmWorkshopId()))
                    .findFirst().orElse(null);
            if (shopEntity != null) {
                model.setWorkshopCode(shopEntity.getWorkshopCode());
            }
        }
    }


    private Long getRecordCountByCode(String aviCode, String action, Integer aviType, Long id) {
        QueryWrapper<AviOperEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviOperEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviOperEntity::getAviCode, aviCode);
        lambdaQueryWrapper.eq(AviOperEntity::getAction, action);
        lambdaQueryWrapper.eq(AviOperEntity::getAviType, aviType);
        //lambdaQueryWrapper.eq(AviOperEntity::getAviOperType, avIOperType);
        //        lambdaQueryWrapper.eq(AviOperEntity::getAttribute1, attribute1);
        //        lambdaQueryWrapper.eq(AviOperEntity::getAttribute2, attribute2);
        //lambdaQueryWrapper.eq(AviOperEntity::getAttribute3, attribute3);
        lambdaQueryWrapper.ne(AviOperEntity::getId, id);
        return selectCount(queryWrapper);
    }
}