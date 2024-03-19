package com.ca.mfd.prc.core.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.report.dto.PrintData;
import com.ca.mfd.prc.core.report.dto.ReportPrintQueueDycVO;
import com.ca.mfd.prc.core.report.entity.RptPrintQueueEntity;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.mapper.IRptPrintQueueMapper;
import com.ca.mfd.prc.core.report.service.IRptPrintQueueService;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 报表打印队列服务实现
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Service
public class RptPrintQueueServiceImpl extends AbstractCrudServiceImpl<IRptPrintQueueMapper, RptPrintQueueEntity> implements IRptPrintQueueService {

    @Autowired
    IRptPrinterService rptPrinterService;

    /**
     * 重新打印
     *
     * @param status 状态
     * @param ids    主键集合
     * @return 列表
     */
    @Override
    public List<RptPrintQueueEntity> getDataListByIds(int status, List<Long> ids) {
        QueryWrapper<RptPrintQueueEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrintQueueEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptPrintQueueEntity::getStatus, status);
        lambdaQueryWrapper.in(RptPrintQueueEntity::getId, ids);
        return selectList(queryWrapper);
    }

    /**
     * 根据主键集合更新
     *
     * @param ids 主键集合
     */
    @Override
    public void updateDatasByIds(List<Long> ids) {
        UpdateWrapper<RptPrintQueueEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptPrintQueueEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptPrintQueueEntity::getStatus, 3);
        lambdaUpdateWrapper.eq(RptPrintQueueEntity::getStatus, 2);
        lambdaUpdateWrapper.in(RptPrintQueueEntity::getId, ids);
        update(updateWrapper);
    }

    /**
     * 打印队列数量查询
     *
     * @return 打印队列数量
     */
    @Override
    public Long getQueueQty() {
        QueryWrapper<RptPrintQueueEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrintQueueEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptPrintQueueEntity::getStatus, 1);
        lambdaQueryWrapper.or().eq(RptPrintQueueEntity::getStatus, 3);
        return selectCount(queryWrapper);
    }

    /**
     * 基于打印机返回打印队列
     *
     * @param ips ips
     * @return 列表
     */
    @Override
    public List<ReportPrintQueueDycVO> queueList(List<String> ips) {
        List<RptPrinterEntity> printerList = rptPrinterService.getListByIps(ips);
        List<Long> falseIds = printerList.stream().filter(s -> !s.getIsEnable()).map(RptPrinterEntity::getId).collect(Collectors.toList());
        if (!falseIds.isEmpty()) {
            String printStatus = "禁用";
            rptPrinterService.updatePrintStatusByIds(falseIds, printStatus);
            updateStatusByIds(falseIds);
            rptPrinterService.saveChange();
        }
        List<ReportPrintQueueDycVO> list = new ArrayList<>();
        List<String> trueIps = printerList.stream().filter(RptPrinterEntity::getIsEnable).map(RptPrinterEntity::getIp).distinct().collect(Collectors.toList());
        List<Long> trueIds = printerList.stream().filter(RptPrinterEntity::getIsEnable).map(RptPrinterEntity::getId).collect(Collectors.toList());
        if (!trueIps.isEmpty()) {
            for (String ip : trueIps) {
                List<RptPrintQueueEntity> printList = getListByIpAndStatus(ip);
                List<ReportPrintQueueDycVO> itemlist = printList.stream().map(s -> {
                    ReportPrintQueueDycVO info = new ReportPrintQueueDycVO();
                    info.setId(s.getId());
                    info.setCode(s.getBizCode());
                    info.setReportPrinterId(s.getPrcRptPrinterId());
                    info.setPrintName(s.getPrintName());
                    info.setIp(s.getIp());
                    info.setModel(s.getModel());
                    info.setPrintNumber(s.getPrintNumber());
                    info.setPrintDt(s.getPrintDt());
                    info.setParamaters(s.getParamaters());
                    info.setPath(s.getPath());
                    info.setPrintCount(s.getPrintQty());
                    return info;
                }).collect(Collectors.toList());

                if (!itemlist.isEmpty()) {
                    for (ReportPrintQueueDycVO item : itemlist) {
                        if (!trueIds.contains(item.getReportPrinterId())) {
                            continue;
                        }
                        item.setData(getPrintType(item, printerList));
                        list.add(item);
                    }
                }
            }
        }
        System.out.println(list.size());
        return list;
    }

    private PrintData getPrintType(ReportPrintQueueDycVO item, List<RptPrinterEntity> printerList) {
        RptPrinterEntity printer = printerList.stream().filter(s -> s.getId() == item.getReportPrinterId()).findFirst().orElse(null);
        Map<String, String> parameters = JSON.parseObject(item.getParamaters(), new TypeReference<HashMap<String, String>>() {
        });
        String content = "";
        if (printer != null && printer.getType() == 2) {
            content = parsePropertyTokens(printer.getTplContent(), parameters);
        }
        PrintData printData = new PrintData();
        if (printer != null) {
            printData.setType(printer.getType());
        } else {
            printData.setType(1);
        }
        printData.setContent(content);
        return printData;
    }

    private String parsePropertyTokens(String str, Map<String, String> properties) {
        String open = "${";
        String close = "}";
        if (StringUtils.isBlank(str) || properties == null || properties.size() == 0) {
            return str;
        }
        String newString = str;
        int start = newString.indexOf(open);
        int end = newString.indexOf(close);
        while (start > -1 && end > start) {
            String prepend = newString.substring(0, start);
            String append = newString.substring(end + close.length());

            int index = start + open.length();
            String propName = newString.substring(index, end - index);
            newString = prepend + properties.getOrDefault(propName, propName) + append;
            start = newString.indexOf(open);
            end = newString.indexOf(close);
        }
        return newString;
    }

    private void updateStatusByIds(List<Long> ids) {
        UpdateWrapper<RptPrintQueueEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptPrintQueueEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptPrintQueueEntity::getStatus, 21);
        lambdaUpdateWrapper.in(RptPrintQueueEntity::getId, ids);
        lambdaUpdateWrapper.and(s -> s.eq(RptPrintQueueEntity::getStatus, 1).or().eq(RptPrintQueueEntity::getStatus, 3));
        this.update(updateWrapper);
    }

    /**
     * 根据IP查询
     *
     * @param ip ip
     * @return 列表
     */
    private List<RptPrintQueueEntity> getListByIpAndStatus(String ip) {
        QueryWrapper<RptPrintQueueEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrintQueueEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptPrintQueueEntity::getIp, ip);
        lambdaQueryWrapper.and(s -> s.eq(RptPrintQueueEntity::getStatus, 1).or().eq(RptPrintQueueEntity::getStatus, 3));
        lambdaQueryWrapper.orderByAsc(RptPrintQueueEntity::getPrintDt);
        return getTopDatas(30, queryWrapper);
    }

    /**
     * 更新
     *
     * @param status   状态
     * @param printQty 打印次数
     * @param id       主键
     */
    @Override
    public void updatePrintQtyById(int status, int printQty, Long id) {
        UpdateWrapper<RptPrintQueueEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RptPrintQueueEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RptPrintQueueEntity::getStatus, status);
        lambdaUpdateWrapper.set(RptPrintQueueEntity::getPrintQty, printQty);
        lambdaUpdateWrapper.eq(RptPrintQueueEntity::getId, id);
        this.update(updateWrapper);
    }

    /**
     * 查询待打印、重新打印的数量
     *
     * @return 总数
     */
    @Override
    public Long getCountNumberByStatus() {
        QueryWrapper<RptPrintQueueEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RptPrintQueueEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RptPrintQueueEntity::getStatus, 1);
        lambdaQueryWrapper.or();
        lambdaQueryWrapper.eq(RptPrintQueueEntity::getStatus, 3);
        return selectCount(queryWrapper);
    }
}