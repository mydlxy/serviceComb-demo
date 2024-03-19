package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.basedto.ExportModel;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.MaintainDataParaInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkStationListInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkstationParaInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyWpEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyWpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author inkelink
 * @Description: 常用缺陷Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdefectanomalywp")
@Tag(name = "常用缺陷服务", description = "常用缺陷")
public class PqsDefectAnomalyWpController extends BaseController<PqsDefectAnomalyWpEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PqsDefectAnomalyWpController.class);
    private final IPqsDefectAnomalyWpService pqsDefectAnomalyWpService;

    @Autowired
    public PqsDefectAnomalyWpController(IPqsDefectAnomalyWpService pqsDefectAnomalyWpService) {
        this.crudService = pqsDefectAnomalyWpService;
        this.pqsDefectAnomalyWpService = pqsDefectAnomalyWpService;
    }

    @GetMapping("getalldatas")
    @Operation(summary = "获取所有常用缺陷")
    public ResultVO<List<PqsDefectAnomalyWpEntity>> getAllDatas() {
        return new ResultVO<List<PqsDefectAnomalyWpEntity>>().ok(pqsDefectAnomalyWpService.getAllDatas(), "获取数据成功");
    }

    /**
     * 维护数据
     *
     * @param info
     * @return
     */
    @PostMapping("maintaindata")
    @Operation(summary = "维护数据")
    public ResultVO<String> maintainData(@RequestBody MaintainDataParaInfo info) {
        pqsDefectAnomalyWpService.maintainData(info);
        pqsDefectAnomalyWpService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取工位信息
     *
     * @param info 条件参数
     * @return
     */
    @PostMapping("getworkstationlist")
    @Operation(summary = "获取工位信息")
    public ResultVO<PageData<WpAnomalyWorkStationListInfo>> getWorkstationList(@RequestBody WpAnomalyWorkstationParaInfo info) {
        ResultVO<PageData<WpAnomalyWorkStationListInfo>> result = new ResultVO<>();
        PageData<WpAnomalyWorkStationListInfo> data = pqsDefectAnomalyWpService.getWorkStationList(info);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "getwpdefectanomalydatatemplate", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取常用缺陷导入模板(定制)")
    public void getWpDefectAnomalyDataTemplate(String fileName, HttpServletResponse response) throws Exception {
        crudService.getImportTemplate(fileName, response);
    }

    /**
     * 导入常用缺陷配置
     *
     * @param req
     * @return
     */
    @PostMapping(value = "importwodefectanomalydata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入常用缺陷配置")
    public ResultVO importWoDefectAnomalyData(MultipartHttpServletRequest req) {
        try {
            MultipartFile file = req.getFile(req.getFileNames().next());
            crudService.importExcel(file.getInputStream());
            crudService.saveChange();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultVO<String>().error("导入数据失败");
        }
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    /**
     * 导出
     *
     * @param model
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "exportdefectanomalywpdata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "定制导出")
    public void exportDefectAnomalyWpData(ExportModel model, HttpServletResponse response) throws Exception {
        crudService.setExcelColumnNames(new HashMap(0));
        crudService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }
}