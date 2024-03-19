package com.ca.mfd.prc.core.report.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.report.entity.RptPrintQueueEntity;
import com.ca.mfd.prc.core.report.service.IRptPrintQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author inkelink
 * @Description: 报表打印队列Controller
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@RestController
@RequestMapping("report/rptprintqueue")
@Tag(name = "报表打印队列服务", description = "报表打印队列")
public class RptPrintQueueController extends BaseController<RptPrintQueueEntity> {
    private static final Logger logger = LoggerFactory.getLogger(RptPrintQueueController.class);
    private final IRptPrintQueueService rptPrintQueueService;
    @Value("${inkelink.core.rptfile.path:}")
    private String rptFilePath;

    @Autowired
    public RptPrintQueueController(IRptPrintQueueService rptPrintQueueService) {
        this.crudService = rptPrintQueueService;
        this.rptPrintQueueService = rptPrintQueueService;
    }

    @Operation(summary = "重新打印")
    @PostMapping("reprint")
    public ResultVO<String> reprint(@RequestBody IdsModel model) {
        if (model == null || model.getIds() == null || model.getIds().length <= 0) {
            throw new InkelinkException("请选择要重新打印的数据");
        }
        String[] ids = model.getIds();
        List<Long> queueList = new ArrayList<>();
        for (String item : ids) {
            Long id = ConvertUtils.stringToLong(item);
            queueList.add(id);
        }
        List<RptPrintQueueEntity> queue = rptPrintQueueService.getDataListByIds(2, queueList);
        if (model.getIds().length != queue.size()) {
            throw new InkelinkException("选择的数据中包含不需要重新打印的数据");
        }
        rptPrintQueueService.updateDatasByIds(queueList);
        rptPrintQueueService.saveChange();
        return new ResultVO<String>().ok("重打成功", "重打成功");
    }

   /* @Operation(summary = "重定向预览")
    @GetMapping("rptredirectpre")
    public void rptredirectpre(String _u, HttpServletResponse response) throws IOException {
        String path = rptFilePath + "ureport/preview?_u=" + _u;
        response.sendRedirect(path);
        //   return "redirect:" + path;
    }*/


    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<RptPrintQueueEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<RptPrintQueueEntity> page = crudService.page(model);
        if (!CollectionUtils.isEmpty(page.getDatas())) {
            //http://10.23.1.47/UReport2/ureport/designer?_u=file:aaa.ureport.xml
            //   "http://10.23.1.47/UReport2/ureport/preview?_u=file:aaa.ureport.xml";
            //    String editPath = rptFilePath + "ureport/designer?_u=file:";
            String previewPath = rptFilePath + "ureport/preview?_u=";

            page.getDatas().stream().forEach(c -> {
               /* try {
                    c.setEditPath(editPath + URLEncoder.encode(c.getPath(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                String param = StringEscapeUtils.unescapeJava(c.getParamaters());
                StringBuilder paramPath = new StringBuilder();
                String uparam = null;
                try {
                    if (StringUtils.hasText(param)) {
                        param = JSONUtil.formatJsonStr(param);
                        int start = param.indexOf("\"");
                        int end = param.lastIndexOf("\"");
                        int len = param.length();
                        if (start == 0 && end == len - 1) {
                            param = param.substring(1, param.length() - 1);
                        }
                        JSONObject jsonObject = JSONUtil.parseObj(param);
                        Set<String> keys = jsonObject.keySet();
                        if (!CollectionUtils.isEmpty(keys)) {
                            for (String str : keys) {
                                Object value = jsonObject.get(str);
                                paramPath.append("&").append(str).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
                            }
                        }
                    }
                    uparam = "file:" + URLEncoder.encode(c.getPath(), "UTF-8") + paramPath;
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                }
                c.setPreviewPath(previewPath + uparam);

                //  String uparam = URLEncoder.encode("file:" + c.getPath() + paramPath, "UTF-8");
                c.setUParamPath(uparam);
                // c.setUParamPath("file:" + URLEncoder.encode(c.getPath(), "UTF-8") + paramPath);
            });
        }

        return new ResultVO<PageData<RptPrintQueueEntity>>().ok(page, "获取数据成功");
    }

}