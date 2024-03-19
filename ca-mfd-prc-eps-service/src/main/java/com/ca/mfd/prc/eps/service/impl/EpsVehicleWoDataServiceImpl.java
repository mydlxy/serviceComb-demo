package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.eps.dto.EpsVehicleWoDataTrcInfo;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoDataMapper;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工艺数据
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleWoDataServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoDataMapper, EpsVehicleWoDataEntity> implements IEpsVehicleWoDataService {

    @Autowired
    private IEpsVehicleWoDataMapper epsVehicleWoDataMapper;

    @Override
    public PageData<EpsVehicleWoDataEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize) {
        PageData<EpsVehicleWoDataEntity> page = new PageData<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        //将SN字段查询，由默认的 AllLike 改为 Equal
        if (conditions != null) {
            for (ConditionDto c : conditions) {
                if (StringUtils.equalsIgnoreCase(c.getColumnName(), "sn")) {
                    c.setOperator(ConditionOper.Equal);
                }
            }
        }
        getPageVehicleDatas(conditions, sorts, page);
        return page;
    }

    private void getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, PageData<EpsVehicleWoDataEntity> page) {

        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName(MpSqlUtils.getColumnName(EpsVehicleWoDataEntity::getLastUpdateDate));
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        boolean isDelete = false;
        for (ConditionDto c : conditions) {
            if (StringUtils.equalsIgnoreCase(c.getColumnName(), "IsDelete")) {
                c.setColumnName("isDelete");
                isDelete = true;
            }
            if (StringUtils.equalsIgnoreCase(c.getColumnName(), "IS_DELETE")) {
                c.setColumnName("isDelete");
                isDelete = true;
            }
        }
        if (!isDelete) {
            conditions.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        }

        //EpsVehicleWoDataEntity.class,
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(EpsVehicleWoDataEntity.class, conditions, "");
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(EpsVehicleWoDataEntity.class, sorts, "");

        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("where_a", conditionCons);
        map.put("order", sortsNew);

        Page<EpsVehicleWoDataEntity> mpage = new Page<>(page.getPageIndex(), page.getPageSize());
        Page<EpsVehicleWoDataEntity> pdata = epsVehicleWoDataMapper.getPageVehicleDatas(mpage, map);
        page.setDatas(pdata.getRecords());
        page.setTotal((int) pdata.getTotal());

    }

    /**
     * 分页信息（获取追溯）
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return Page<EpsVehicleWoDataTrcInfo>
     */
    @Override
    public PageData<EpsVehicleWoDataTrcInfo> getPageTrcVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize) {

        PageData<EpsVehicleWoDataTrcInfo> page = new PageData<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("creationDate");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
       /* boolean isDelete = false;
        for (ConditionDto c : conditions) {
            if (StringUtils.equalsIgnoreCase(c.getColumnName(), "IsDelete")) {
                c.setColumnName("isDelete");
                isDelete = true;
            }
            if (StringUtils.equalsIgnoreCase(c.getColumnName(), "IS_DELETE")) {
                c.setColumnName("isDelete");
                isDelete = true;
            }
        }
        if (!isDelete) {
            conditions.add(new ConditionDto("isDelete", "0", ConditionOper.Equal));
        }*/

        //List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(EpsVehicleWoDataEntity.class, conditions, "");
        //List<SortDto> sortsNew = MpSqlUtils.filtrationSort(EpsVehicleWoDataEntity.class, sorts, "");

        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("where_a", conditions);
        map.put("order", sorts);

        Page<EpsVehicleWoDataTrcInfo> mpage = new Page<>(page.getPageIndex(), page.getPageSize());
        Page<EpsVehicleWoDataTrcInfo> pdata = epsVehicleWoDataMapper.getPageTrcVehicleDatas(mpage, map);
        page.setDatas(pdata.getRecords());
        page.setTotal((int) pdata.getTotal());
        return page;
    }

    @Override
    public void exportTrcVehicleDatas(Map<String, String> fieldParam,List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        boolean isSimpleDate = false;
        //Map<String, String> fieldParam = getExcelColumnNames();

        PageData<EpsVehicleWoDataTrcInfo> page = this.getPageTrcVehicleDatas(conditions, sorts, 1, Integer.MAX_VALUE);
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(page.getDatas(), isSimpleDate);
        mapList.forEach(data -> {
            // 导出处理时间格式化
            if (data.containsKey("creationDate") && data.getOrDefault("creationDate", null) != null) {
                data.put("creationDate", DateUtils.format((Date) data.get("creationDate"), DateUtils.DATE_TIME_PATTERN));
            }
            // 状态处理 0 未知 1 OK 2 NG 3 Bypass
            if (data.containsKey("result") && data.getOrDefault("result", null) != null) {
                switch (String.valueOf(data.get("result"))) {
                    case "0":
                        data.put("result", "未知");
                        break;
                    case "1":
                        data.put("result", "OK");
                        break;
                    case "2":
                        data.put("result", "NG");
                        break;
                    case "3":
                        data.put("result", "Bypass");
                        break;
                    default:
                        break;
                }
            }
        });
        dealExcelDatas(mapList);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }


    /**
     * 获取自定义采集数据
     *
     * @param vehicleWoDataId
     * @return
     */
    @Override
    public List<EpsVehicleEqumentDataEntity> getCurrentData(Long vehicleWoDataId) {
        EpsVehicleWoDataEntity woDataInfo = get(vehicleWoDataId);
        if (woDataInfo == null) {
            return new ArrayList<>();
        }
        return epsVehicleWoDataMapper.getCurrentData(woDataInfo.getDataTableName(), vehicleWoDataId);
    }

    @Override
    public EpsVehicleWoDataEntity getBySnAndDecviceName(String sn, String decviceName) {
        QueryWrapper<EpsVehicleWoDataEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoDataEntity::getSn, sn).eq(EpsVehicleWoDataEntity::getDecviceName, decviceName);

        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public List<EpsVehicleWoDataEntity> getByEpsVehicleWoId(Long epsVehicleWoId) {
        QueryWrapper<EpsVehicleWoDataEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoDataEntity::getPrcEpsVehicleWoId, epsVehicleWoId);
        return selectList(qry);
    }
}