package com.ca.mfd.prc.core.report.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.*;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import com.ca.mfd.prc.core.report.dto.AppConfig;
import com.ca.mfd.prc.core.report.dto.PrinterStatusItems;
import com.ca.mfd.prc.core.report.dto.ReportPrinterDyc;
import com.ca.mfd.prc.core.report.dto.StatusCheckItem;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;
import com.ca.mfd.prc.core.report.service.IRptPrinterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("report/reportprint")
@Tag(name = "打印状态更新定时调度", description = "打印状态更新定时调度")
public class ReportPrintController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(ReportPrintController.class);
    @Autowired
    private RestTemplateUtil _httpHelper;
    private final ConcurrentMap<String, Date> exceptionDic = new ConcurrentHashMap<>();

    @GetMapping(value = "excute")
    @Operation(summary = "执行打印状态更新服务")
    public ResultVO<String> excute() {
        try {
            reportPrint();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResultVO<String>().ok("", "处理成功");
    }
    private void reportPrint() {
        IRptPrinterService rptPrinterService = SpringContextUtils.getBean(IRptPrinterService.class);
        String resultStr = getReportConfigure();
        AppConfig appConfig = new AppConfig(resultStr);
        List<RptPrinterEntity> lists = rptPrinterService.getListAll();
        List<ReportPrinterDyc> datas = lists.stream().map(s -> {
            ReportPrinterDyc info = new ReportPrinterDyc();
            info.setId(s.getId());
            info.setPrintName(s.getPrintName());
            info.setIp(s.getIp());
            info.setModel(s.getModel());
            return info;
        }).collect(Collectors.toList());

        if (appConfig.getPrint() != null && appConfig.getPrint().getPrinter() != null && appConfig.getPrint().getPrinter().size() > 0) {
            String[] array = appConfig.getPrint().getPrinter().toArray(new String[appConfig.getPrint().getPrinter().size()]);
            datas = datas.stream().filter(s -> ArrayUtil.containsIgnoreCase(array, s.getPrintName())).collect(Collectors.toList());
        }
        try {
            List<String> ips = datas.stream().map(ReportPrinterDyc::getIp).distinct().collect(Collectors.toList());
            for (String ip : ips) {
                try {
                    ReportPrinterDyc data = datas.stream().filter(s -> StringUtils.equals(s.getIp(), ip)).findFirst().orElse(null);
                    String key = appConfig.getPrint().getStatusCheck().keySet().stream()
                            .filter(s -> {
                                if (data != null) {
                                    return s.equalsIgnoreCase(data.getModel());
                                }
                                return false;
                            }).findFirst().orElse("");
                    PrinterStatusItems result = tryGetPrinterStatus(data, appConfig);
                    if (!result.getResult()) {
                        Date logDt = exceptionDic.getOrDefault(data.getIp(), null);
                        if (logDt != null) {
                            //Date logDt = exceptionDic.get(data.getIp());
                            if (logDt.before(DateUtils.addDateHours(new Date(), -1))) {
                                exceptionDic.put(data.getIp(), new Date());
                                if (StringUtils.isNotBlank(key) && appConfig.getPrint().getStatusCheck().get(key).getIgnoreErrs().contains(result.getMessage())) {
                                    logger.info(data.getIp(), "打印机【" + data.getPrintName() + "】:打印机状态异常" + result.getMessage());
                                } else {
                                    logger.error(data.getIp(), "打印机【" + data.getPrintName() + "】:打印机状态异常" + result.getMessage());
                                }
                            }
                        } else {
                            exceptionDic.put(data.getIp(), new Date());
                            if (StringUtils.isNotBlank(key) && appConfig.getPrint().getStatusCheck().get(key).getIgnoreErrs().contains(result.getMessage())) {
                                logger.info(data.getIp(), "打印机【" + data.getPrintName() + "】:打印机状态异常" + result.getMessage());
                            } else {
                                logger.error(data.getIp(), "打印机【" + data.getPrintName() + "】:打印机状态异常" + result.getMessage());
                            }
                        }
                        logger.error(data.getIp(), "打印机【" + data.getPrintName() + "】:打印机状态异常[" + result.getMessage() + "]");
                    } else {
                        exceptionDic.remove(data.getIp());
                    }
                    for (ReportPrinterDyc item : datas.stream().filter(s -> StringUtils.equals(s.getIp(), ip)).collect(Collectors.toList())) {
                        rptPrinterService.updateQueuePrintStatus(result.getMessage(), item.getId());
                    }
                    rptPrinterService.saveChange();
                } catch (Exception ex) {
                    logger.info(ex.getMessage(), ex);
                }
            }
        } catch (Exception ex){
            logger.info(ex.getMessage(), ex);
        }
    }

    private PrinterStatusItems tryGetPrinterStatus(ReportPrinterDyc data, AppConfig appConfig) {
        // AppConfig appConfig = new AppConfig();
        String key = appConfig.getPrint().getStatusCheck().keySet().stream()
                .filter(s -> {
                    if (data != null) {
                        return s.equalsIgnoreCase(data.getModel());
                    }
                    return false;
                }).findFirst().orElse("");
        if (StringUtils.isNotBlank(key)) {
            StatusCheckItem item = appConfig.getPrint().getStatusCheck().get(key);
            String statusText = "未知";
            try {
                if (data != null) {
                    boolean prep = NetworkUtils.ping(data.getIp());
                    if (!prep) {
                        return getResultItems(false, "离线");
                    }
                    ResponseEntity requestTxt = _httpHelper.getStringForResponse(data.getIp() + item.getPath());
                    if (!requestTxt.getStatusCode().is2xxSuccessful() || requestTxt.getBody() == null) {
                        return getResultItems(false, "状态获取失败");
                    }
                    String html = requestTxt.getBody().toString();
                    if (StringUtils.isBlank(html)) {
                        return getResultItems(false, "状态获取失败");
                    }
                    Document document = Jsoup.parse(html);
                    for (String xpath : item.getXpaths()) {
                        Elements node = document.select(xpath);
                        if (node.isEmpty()) {
                            continue;
                        }
                        statusText = node.text().trim();
                        for (String str : item.getReplaces()) {
                            statusText = StrUtil.replace(statusText, str, "", true);
                        }
                        String[] array = item.getOks().toArray(new String[item.getOks().size()]);
                        if (StringUtils.isNotBlank(statusText) && ArrayUtil.containsIgnoreCase(array, statusText)) {
                            return getResultItems(true, statusText);
                        }
                    }
                    return getResultItems(false, statusText);
                }
            } catch (Exception ex) {
                statusText = "异常";
                logger.info(data.getIp() + ",QueueId：（" + data.getIp() + "),尝试获取打印机(" + data.getPrintName() + "状态:" + statusText, ex);
            }
            logger.info(data.getIp() + ",QueueId：（" + data.getIp() + "),尝试获取打印机(" + data.getPrintName() + "状态:" + statusText);
            return getResultItems(false, statusText);
        }
        try {
            if (data != null) {
                boolean prep = NetworkUtils.ping(data.getIp());
                if (prep) {
                    logger.info(data.getIp(), "QueueId:(" + data.getId() + "),尝试获取打印机(" + data.getPrintName() + "),状态:就绪");
                    return getResultItems(true, "就绪");
                }
                logger.info(data.getIp(), "QueueId:(" + data.getId() + "),尝试获取打印机(" + data.getPrintName() + "),状态:离线");
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        }
        return getResultItems(false, "未知");
    }

    private PrinterStatusItems getResultItems(Boolean result, String message) {
        PrinterStatusItems resItems = new PrinterStatusItems();
        resItems.setResult(result);
        resItems.setMessage(message);
        return resItems;
    }


    private String getReportConfigure() {
        String resultStr = StringUtils.EMPTY;
        ISysConfigurationService sysConfigurationService = SpringContextUtils.getBean(ISysConfigurationService.class);
        List<SysConfigurationEntity> list = sysConfigurationService.getSysConfigurations("ReportConfigure");
        if (!CollectionUtils.isEmpty(list)) {
            SysConfigurationEntity info = list.get(0);
            if (!Objects.isNull(info)) {
                resultStr = info.getValue();
            }
        }
        return resultStr;
    }
}
