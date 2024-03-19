package com.ca.mfd.prc.core.main.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.core.main.dto.DatasourceAppConfig;
import com.ca.mfd.prc.core.main.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.core.main.mapper.ISysSequenceNumberMapper;
import com.ca.mfd.prc.core.main.service.ISysSequenceNumberService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 业务编码配规则配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysSequenceNumberServiceImpl extends AbstractCrudServiceImpl<ISysSequenceNumberMapper, SysSequenceNumberEntity> implements ISysSequenceNumberService {
    private static final Logger logger = LoggerFactory.getLogger(SysSequenceNumberServiceImpl.class);
    @Autowired
    ISysSequenceNumberMapper sysSequenceNumberDao;

    @Autowired
    DatasourceAppConfig datasourceAppConfig;

    private final String CACHE_NAME = "PRC_SYS_SEQUENCE_NUMBER";
    @Autowired
    private LocalCache localCache;


    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<SysSequenceNumberEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(SysSequenceNumberEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(SysSequenceNumberEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<SysSequenceNumberEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(SysSequenceNumberEntity entity) {
        validData(entity);
    }

    @Override
    public void beforeUpdate(SysSequenceNumberEntity entity) {
        validData(entity);
    }

    private void validData(SysSequenceNumberEntity model) {
        if (model.getMaxValue() < model.getMinValue()) {
            throw new InkelinkException("最小值不能大于最大值");
        }
        if (model.getMaxValue().toString().length() > model.getSequencenumberLen()) {
            throw new InkelinkException("最大值的长度大于设定的长度");
        }
        if (model.getSequencenumberLen() > 9) {
            throw new InkelinkException("最大长度不能超过9");
        }
        QueryWrapper<SysSequenceNumberEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysSequenceNumberEntity::getSequenceType, model.getSequenceType())
                .ne(SysSequenceNumberEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("代码%s已存在", model.getSequenceType()));
        }
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<SysSequenceNumberEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<SysSequenceNumberEntity>> getDataFunc = (obj) -> {
                List<SysSequenceNumberEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<SysSequenceNumberEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    @Override
    public SysSequenceNumberEntity getSysSequenceInfoByType(String sequenceType) {
        QueryWrapper<SysSequenceNumberEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysSequenceNumberEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(SysSequenceNumberEntity::getSequenceType, sequenceType);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 获取流水号/需要提交统一事务，如果在循环中使用生成的流水号是一个
     *
     * @param seqType 流水号类别
     * @return 流水号
     */
    @Override
    public String getSeqNumWithTransaction(String seqType) {
        Date dt = new Date();
        List<SysSequenceNumberEntity> dataList = selectList(new QueryWrapper<SysSequenceNumberEntity>().eq("SEQUENCE_TYPE", seqType));
        if (CollectionUtil.isEmpty(dataList)) {
            throw new InkelinkException("编码规则代码" + seqType + "没有设置！");
        }
        SysSequenceNumberEntity data = dataList.get(0);
        int num = data.getCurMaxSequenceNumber() + 1;
        if (num > data.getMaxValue() || num < data.getMinValue()) {
            num = data.getMinValue();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String scope = "scope";
        if (!StringUtils.equals(data.getResetType(), scope) && !StringUtils.equals(sdf.format(data.getCurMaxDate()), sdf.format(dt))) {
            num = data.getMinValue();
        }
        String text = data.getPartYearFmt();
        if (StringUtils.isNotBlank(data.getPartMonthFmt())) {
            text = data.getPartMonthFmt();
        }
        if (StringUtils.isNotBlank(data.getPartDayFmt())) {
            text = data.getPartDayFmt();
        }
        String item = "";
        if (StringUtils.isNotBlank(text)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat(text);
            item = sdf1.format(dt);
        }
        List<String> values = Arrays.asList(
                data.getPrefix(),
                item,
                data.getMidfix(),
                StringUtils.leftPad(String.valueOf(num), data.getSequencenumberLen(), '0'),
                data.getSuffix()
        );
        String result = String.join(data.getSeparator() != null ? data.getSeparator() : "", values);
        data.setCurMaxDate(dt);
        data.setCurMaxSequenceNumber(num);
        update(data);
        return result;
    }

    /**
     * 查询sequenceType的数量
     *
     * @param sequenceType 流水号类型
     * @return 数量
     */
    @Override
    public Long getSequenceTypeCount(String sequenceType) {
        QueryWrapper<SysSequenceNumberEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysSequenceNumberEntity::getSequenceType, sequenceType);
        Long cnt = selectCount(qry);
        return cnt == null ? 0 : cnt;
    }

    /**
     * 获取序列号（自动增长）
     *
     * @param sequenceType 水水好类别
     * @return 流水号
     */
    @Override
    public String getSeqNum(String sequenceType) {
        List<String> list = this.getSeqNumNew(sequenceType, 1, "");
        return list.stream().findFirst().orElse("");
    }

    public List<String> getSeqNumNew(String sequenceType) {
        return this.getSeqNumNew(sequenceType, 1, "");
    }

    public List<String> getSeqNumNew(String sequenceType, Integer count) {
        return this.getSeqNumNew(sequenceType, count, "");
    }

    public List<String> getSeqNumNew(String sequenceType, Integer count, String midKey) {
        List<String> result = new ArrayList<>();
        String typeName = datasourceAppConfig.getDriverclassname();
        try {
            switch (typeName) {
                case "System.Data.OracleClient":
                case "Oracle.DataAccess.Client":
                    break;
                case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                    Map<String, Object> sqlmap = createCondition(sequenceType, count, midKey);
                    result = sysSequenceNumberDao.getSeqNumNewBySqlServer(sqlmap);
                    break;
                case "com.mysql.cj.jdbc.Driver":
                default:
                    Map<String, Object> map = createCondition(sequenceType, count, midKey);
                    result = sysSequenceNumberDao.getSeqNumNewByMysql(map);
                    break;
            }
        } catch (Exception exception) {

        }
        return result;
    }

    private Map<String, Object> createCondition(String sequenceType, Integer count, String midKey) {
        Date dt = new Date();
        Map<String, Object> map = new HashMap<>(10);
        map.put("sequenceType", sequenceType);
        map.put("sequenceNum", count);
        map.put("midKey", midKey);
        map.put("vdate", dt);
        return map;
    }
}