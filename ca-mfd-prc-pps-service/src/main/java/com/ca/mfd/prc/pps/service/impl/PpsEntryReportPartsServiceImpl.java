package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;
import com.ca.mfd.prc.pps.entity.PpsRinTimeConfigEntity;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.extend.IPpsEntryPartsExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsEntryReportPartsMapper;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryReportPartsService;
import com.ca.mfd.prc.pps.service.IPpsProcessRelationService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 报工单-零部件服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsEntryReportPartsServiceImpl extends AbstractCrudServiceImpl<IPpsEntryReportPartsMapper, PpsEntryReportPartsEntity> implements IPpsEntryReportPartsService {

    private static final Logger logger = LoggerFactory.getLogger(PpsEntryReportPartsServiceImpl.class);
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IPpsEntryPartsExtendService ppsEntryPartsExtendService;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IPpsProcessRelationService ppsProcessRelationService;

    private final Map<String, Map<String, String>> orderDic = Maps.newHashMapWithExpectedSize(3);
    {
        Map<String, String> mpVehicle = new LinkedHashMap<>();
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryNo), "生产工单号");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getBarcode), "条码");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationCode), "工位编码");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationName), "工位名称");

        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessCode), "工序编码");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessName), "工序名称");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryReportCount), "报工数量");
        mpVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getAttribute1), "报工结果");
        //1 合格  2 待检 3 不合格 4 待质检  5质检合格（传递给as后） 6质检不合格（传递给as后） 7 报废
        orderDic.put(RptPartsSheetTableName.MachiningRpt, mpVehicle);
//报工单号 条码 报工工位 名称 工序编码 工序编码  结果 报工数量

        Map<String, String> staVehicle = new LinkedHashMap<>();
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryNo), "生产工单号");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getBarcode), "条码");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationCode), "工位编码");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationName), "工位名称");

        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessCode), "工序编码");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessName), "工序名称");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryReportCount), "报工数量");
        staVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getAttribute1), "报工结果");
        //1 合格  2 待检 3 不合格 4 待质检  5质检合格（传递给as后） 6质检不合格（传递给as后） 7 报废
        orderDic.put(RptPartsSheetTableName.StampingRpt, staVehicle);

        Map<String, String> preVehicle = new LinkedHashMap<>();
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryNo), "生产工单号");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getBarcode), "条码");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationCode), "工位编码");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationName), "工位名称");

        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessCode), "工序编码");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessName), "工序名称");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryReportCount), "报工数量");
        preVehicle.put(MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getAttribute1), "报工结果");
        //1 合格  2 待检 3 不合格 4 待质检  5质检合格（传递给as后） 6质检不合格（传递给as后） 7 报废
        orderDic.put(RptPartsSheetTableName.PressureCastingRpt, preVehicle);
    }

    @Override
    public PpsEntryReportPartsEntity getByBarcodeAndCatory(String barCode, Integer orderCategory) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PpsEntryReportPartsEntity::getOrderCategory, orderCategory)
                .eq(PpsEntryReportPartsEntity::getBarcode, barCode)
                .orderByDesc(PpsEntryReportPartsEntity::getCreationDate);
        return getTopDatas(1, wrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryReportPartsEntity getFirstByBarcode(String barCode) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PpsEntryReportPartsEntity::getBarcode, barCode)
                .orderByDesc(PpsEntryReportPartsEntity::getCreationDate);
        return getTopDatas(1, wrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryReportPartsEntity getFirstByEntryNo(String entryNo) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PpsEntryReportPartsEntity::getEntryNo, entryNo);
        return getTopDatas(1, wrapper).stream().findFirst().orElse(null);
    }

    @Override
    public List<PpsEntryReportPartsEntity> getByEntryNo(String entryNo) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PpsEntryReportPartsEntity::getEntryNo, entryNo);
        return selectList(wrapper);
    }

    /**
     * 根据分类获取前20条
     *
     * @param orderCategory
     * @return 列表
     */
    @Override
    public List<PpsEntryReportPartsEntity> getTopDataByOrderCategory(int orderCategory) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryReportPartsEntity> lambdaQueryWrapper = wrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryReportPartsEntity::getIsPassAvi, false);
        lambdaQueryWrapper.eq(PpsEntryReportPartsEntity::getOrderCategory, orderCategory);
        lambdaQueryWrapper.ne(PpsEntryReportPartsEntity::getReportType, 2);
        lambdaQueryWrapper.ne(PpsEntryReportPartsEntity::getReportType, 4);
        lambdaQueryWrapper.orderByAsc(PpsEntryReportPartsEntity::getCreationDate);
        return this.getTopDatas(20, wrapper);
    }

    /**
     * 根据分类获取
     *
     * @param orderCategory
     * @param rprtNos
     * @return
     */
    @Override
    public List<PpsEntryReportPartsEntity> getRecordByOrderCategory(int orderCategory, List<String> rprtNos) {
        QueryWrapper<PpsEntryReportPartsEntity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryReportPartsEntity> lambdaQueryWrapper = wrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryReportPartsEntity::getOrderCategory, orderCategory);
        lambdaQueryWrapper.in(PpsEntryReportPartsEntity::getEntryReportNo, rprtNos);
        return selectList(wrapper);
    }


    @Override
    public void updateIsPassAviById(Long id) {
        UpdateWrapper<PpsEntryReportPartsEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryReportPartsEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.set(PpsEntryReportPartsEntity::getIsPassAvi, true);
        lambdaUpdateWrapper.eq(PpsEntryReportPartsEntity::getId, id);
        this.update(wrapper);
    }
    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDic.get(fileName));
        Map<String, String> fieldParam = getExcelColumnNames();
        InkelinkExcelUtils.getImportTemplate(fieldParam, fileName, response);
    }

  /*  @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        ConditionDto orderCategory = conditions.stream().filter(c -> "orderCategory".equals(c.getColumnName())).findFirst().orElse(null);
        if (orderCategory == null) {
            throw new InkelinkException("需要传入orderCategory参数区分报工车间");
        }
        String enumOrder;

        if (String.valueOf(OrderCategoryEnum.PressureCasting.code()).equals(orderCategory.getValue())) {
            enumOrder = RptPartsSheetTableName.PressureCastingRpt;
        } else if (String.valueOf(OrderCategoryEnum.Machining.code()).equals(orderCategory.getValue())) {
            enumOrder = RptPartsSheetTableName.MachiningRpt;
        } else if (String.valueOf(OrderCategoryEnum.Stamping.code()).equals(orderCategory.getValue())) {
            enumOrder = RptPartsSheetTableName.StampingRpt;
        } else if (String.valueOf(OrderCategoryEnum.CoverPlate.code()).equals(orderCategory.getValue())) {
            enumOrder = RptPartsSheetTableName.CoverPlateRpt;
        } else {
            enumOrder = RptPartsSheetTableName.PressureCastingRpt;
        }
        super.setExcelColumnNames(orderDic.get(enumOrder));
        super.export(conditions, sorts, enumOrder, response);
    }*/

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        /*for (Map<String, Object> data : datas) {
            if (data.containsKey("estimatedStartDt") && data.get("estimatedStartDt") != null) {
                data.put("estimatedStartDt", DateUtils.format((Date) data.get("estimatedStartDt"), "yyyy-MM-dd HH:mm:ss"));
            }
            if (data.containsKey("estimatedEndDt") && data.get("estimatedEndDt") != null) {
                data.put("estimatedEndDt", DateUtils.format((Date) data.get("estimatedEndDt"), "yyyy-MM-dd HH:mm:ss"));
            }
        }*/
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        //验证必填
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryNo), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getBarcode), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationCode), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getWorkstationName), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessName), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getProcessCode), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getAttribute1), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsEntryReportPartsEntity::getEntryReportCount), i + 1);
        }
    }

    void validExcelDataRequire(Map<String, String> fieldParam, Map<String, String> data, String col, int rowIndex) {
        String columnName = fieldParam.get(col);
        String val = data.get(col);
        if (StringUtils.isBlank(val)) {
            throw new InkelinkException("第“" + rowIndex + "”行，“" + columnName + "”列：不能为空");
        }
    }

    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                orderDic.keySet().toArray(new String[orderDic.keySet().size()]));

        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue() == null || item.getValue().size() <= 0) {
                continue;
            }
            validImportDatas(item.getValue(), orderDic.get(item.getKey()));
            List<PpsEntryReportPartsEntity> dataEts = convertExcelDataToEntity(item.getValue());
            // excelPm数据导入
            switch (item.getKey()) {
                case RptPartsSheetTableName.CoverPlateRpt: {
                    importExcelDatas(OrderCategoryEnum.CoverPlate.code(), dataEts);
                }
                break;
                case RptPartsSheetTableName.StampingRpt: {
                    importExcelDatas(OrderCategoryEnum.Stamping.code(), dataEts);
                }
                break;
                case RptPartsSheetTableName.MachiningRpt: {
                    importExcelDatas(OrderCategoryEnum.Machining.code(), dataEts);
                }
                break;
                case RptPartsSheetTableName.PressureCastingRpt: {
                    importExcelDatas(OrderCategoryEnum.PressureCasting.code(), dataEts);
                }
                break;
                default:
                    break;
            }
        }
    }

    private void importExcelDatas(Integer orderCategory, List<PpsEntryReportPartsEntity> dataEts) {
        if (dataEts == null || dataEts.isEmpty()) {
            return;
        }
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        List<PpsEntryReportPartsEntity> partsEntities = new ArrayList<>();
        for (PpsEntryReportPartsEntity et : dataEts) {

            PmWorkStationEntity workStationInfo =
                    pmall.getStations().stream().filter(s -> StringUtils.equalsIgnoreCase(s.getWorkstationCode(), et.getWorkstationCode())).findFirst().orElse(null);
            if (workStationInfo == null) {
                throw new InkelinkException("工位编码[" + et.getWorkstationCode() + "]无效");
            }
            PmLineEntity line = pmall.getLines().stream().filter(s -> Objects.equals(s.getId(), workStationInfo.getPrcPmLineId()))
                    .findFirst().orElse(null);
            PmWorkShopEntity shop = pmall.getShops().stream().filter(s -> Objects.equals(s.getId(), line.getPrcPmWorkshopId()))
                    .findFirst().orElse(null);
            PpsEntryPartsEntity entry = ppsEntryPartsExtendService.getByEntryNo(et.getEntryNo());
            if (entry == null) {
                throw new InkelinkException("报工单号[" + et.getEntryNo() + "]无效");
            }
            List<PpsProcessRelationEntity> rels = ppsProcessRelationService.getListByOrderCategory(orderCategory, et.getProcessCode());
            if (rels == null || rels.isEmpty()) {
                throw new InkelinkException("工序编码[" + et.getProcessCode() + "]无效");
            }
            QueryWrapper<PpsEntryReportPartsEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<PpsEntryReportPartsEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(PpsEntryReportPartsEntity::getBarcode, et.getBarcode())
                    .eq(PpsEntryReportPartsEntity::getOrderCategory, orderCategory);
            PpsEntryReportPartsEntity partsEntity = getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
            if (partsEntity != null) {
                partsEntity.setProcessCode(et.getProcessCode());
                partsEntity.setProcessName(et.getProcessName());
                updateById(partsEntity);
                continue;
            }
            //获取唯一码KEY
            String snKey = "entryReportNo";
            switch (orderCategory) {
                //压铸
                case 3:
                    snKey = "entryReportNo_Cast";
                    break;
                //机加工
                case 4:
                    snKey = "entryReportNo_Machining";
                    break;
                //冲压
                case 5:
                    snKey = "entryReportNo_Stamping";
                    break;
                //电池盖板
                case 6:
                    snKey = "entryReportNo_CoverPlate";
                    break;
                default:
                    break;
            }
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("EntryNo", et.getEntryNo());
            String entryReportNo = sysSnConfigProvider.createSnBypara(snKey, paraMap);
            if (StringUtils.isBlank(entryReportNo)) {
                throw new InkelinkException("条码[" + et.getBarcode() + "]生成报工单号失败");
            }

            et.setEntryReportNo(entryReportNo);
            et.setPlanNo(entry.getPlanNo());
            et.setMaterialNo(entry.getMaterialNo());
            et.setMaterialCn(entry.getMaterialCn());
            //PROCESS_CODE
            et.setPrcPmShcCalendarId(0L);

            //1合格,2待检,3不合格,4待质检,5质检合格,6质检不合格,7报废
            Integer reportType = 0;
            switch (et.getAttribute1()) {
                case "合格":
                    reportType = 1;
                    break;
                case "待检":
                    reportType = 2;
                    break;
                case "不合格":
                    reportType = 3;
                    break;
                case "待质检":
                    reportType = 4;
                    break;
                case "质检合格":
                    reportType = 5;
                    break;
                case "质检不合格":
                    reportType = 6;
                    break;
                case "报废":
                    reportType = 7;
                    break;
                default:
                    break;
            }
            if (reportType == 0) {
                throw new InkelinkException("报工结果[" + et.getAttribute1() + "]无效");
            }
            et.setReportType(reportType);
            et.setEntryReportType(1);  //1生产 2 返修
            et.setOrderCategory(orderCategory);
            et.setWorkshopCode(shop.getWorkshopCode());
            et.setWorkshopName(shop.getWorkshopName());
            et.setLineCode(line.getLineCode());
            et.setLineName(line.getLineName());
            //ENTRY_REPORT_COUNT
            et.setIsPassAvi(false);
            et.setIsBindCarrier(false);
            //PRC_PM_SHC_CALENDAR_ID
            partsEntities.add(et);
        }
        this.insertBatch(partsEntities);
    }

    protected class RptPartsSheetTableName {
        public static final String PressureCastingRpt = "压铸报工";
        public static final String MachiningRpt = "机加报工";
        public static final String StampingRpt = "冲压报工";
        public static final String CoverPlateRpt = "盖板报工";
    }
}