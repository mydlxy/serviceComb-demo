package com.ca.mfd.prc.core.report.controller;

import com.alibaba.fastjson.JSONArray;
import com.ca.mfd.prc.common.config.RestTemplateConfig;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.RestTemplateUtil;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.core.report.dto.PrintFileDTO;
import com.ca.mfd.prc.core.report.entity.RptFileEntity;
import com.ca.mfd.prc.core.report.service.IRptFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 报表文件存储Controller
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@RestController
@RequestMapping("report/rptfile")
@Tag(name = "报表文件存储服务", description = "报表文件存储")
public class RptFileController extends BaseController<RptFileEntity> {
    private static final Logger logger = LoggerFactory.getLogger(RptFileController.class);

    private final IRptFileService rptFileService;
    @Value("${inkelink.core.rptfile.path:}")
    private String rptFilePath;

    @Autowired
    public RptFileController(IRptFileService rptFileService) {
        this.crudService = rptFileService;
        this.rptFileService = rptFileService;
    }

    @Operation(summary = "获取报表文件下拉列表")
    @PostMapping("getlist")
    public ResultVO<List<ComboInfoDTO>> getlist(String query) {
        if (StringUtils.isBlank(query)) {
            query = "";
        }
        List<ComboInfoDTO> list = new ArrayList<>();
        List<RptFileEntity> lists = rptFileService.getListByName(query);
        list = lists.stream().map(s -> {
            ComboInfoDTO info = new ComboInfoDTO();
            info.setValue(s.getId().toString());
            info.setText(s.getDisplayName());
            return info;
        }).collect(Collectors.toList());
        return new ResultVO<List<ComboInfoDTO>>().ok(list, "保存成功");
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<RptFileEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<RptFileEntity> page = crudService.page(model);
        if (!CollectionUtils.isEmpty(page.getDatas())) {
            //http://10.23.1.47/UReport2/ureport/designer?_u=file:aaa.ureport.xml
            //   "http://10.23.1.47/UReport2/ureport/preview?_u=file:aaa.ureport.xml";
            String editPath = rptFilePath + "ureport/designer?_u=file:";
           // String previewPath = rptFilePath + "ureport/preview?_u=file:";
            page.getDatas().stream().forEach(c -> {
                try {
                    c.setEditPath(editPath + URLEncoder.encode(c.getPath(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                }
               /* try {
                    c.setPreviewPath(previewPath + URLEncoder.encode(c.getPath(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                try {
                    c.setUParamPath("file:" + URLEncoder.encode(c.getPath(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                }
            });
            // page.getDatas().stream().forEach(c -> c.setPreviewPath("file:" + c.getPath()));
        }
        return new ResultVO<PageData<RptFileEntity>>().ok(page, "获取数据成功");
    }


   /* @Operation(summary = "重定向编辑")
    @GetMapping("rptredirectedit")
    public void rptredirectedit(String _u, HttpServletResponse response) throws IOException {
        String path = rptFilePath + "ureport/designer?_u=" + _u;
        response.sendRedirect(path);
        //  return "redirect:" + path;
    }*/


    @Operation(summary = "获取报表文件下拉列表")
    @GetMapping("getrptfilelist")
    public ResultVO<List<ComboInfoDTO>> getrptfilelist() {
        List<ComboInfoDTO> list = new ArrayList<>();

        RestTemplateConfig restTemplateConfig = SpringContextUtils.getBean(RestTemplateConfig.class);
        RestTemplateUtil restTemplateUtil = new RestTemplateUtil(restTemplateConfig.newRestTemplate());

        String path = rptFilePath + "ureport/designer/loadReportProviders";
        HashMap<String, String> mapHeader = new HashMap<>();
        ResponseEntity responseEntity = restTemplateUtil.getStringForResponse(path, mapHeader);

        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(responseEntity.getBody()));
        List<PrintFileDTO> printFiles = jsonArray.toJavaList(PrintFileDTO.class);
        if (!CollectionUtils.isEmpty(printFiles)) {
            for (PrintFileDTO printFileDTO : printFiles) {
                List<PrintFileDTO.ReportFiles> reportFiles = printFileDTO.getReportFiles();
                if (!CollectionUtils.isEmpty(reportFiles)) {
                    for (PrintFileDTO.ReportFiles each : reportFiles) {
                        ComboInfoDTO info = new ComboInfoDTO();
                        info.setValue(each.getName());
                        info.setText(each.getName());
                        if (each.getName().indexOf(".") > 0) {
                            info.setText(each.getName().substring(0, each.getName().indexOf(".")));
                        }
                        list.add(info);
                    }
                }
            }
        }

        return new ResultVO<List<ComboInfoDTO>>().ok(list, "获得信息成功");
    }

    /**
     * 根据参数类型获取参数值
     *
     * @param displayName      配置名称
     * @return 返回path
     */
    @GetMapping(value = "/provider/getbydisplayname")
    @Operation(summary ="根据配置名称查询path")
    public ResultVO<String> getByDisplayName(String displayName) {
        ResultVO<String> result = new ResultVO<>();
        String data = rptFileService.getByDisplayName(displayName);
        return result.ok(data);
    }
}