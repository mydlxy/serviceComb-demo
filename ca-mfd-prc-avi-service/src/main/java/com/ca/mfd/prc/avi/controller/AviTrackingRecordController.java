package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.dto.AviOperDto;
import com.ca.mfd.prc.avi.dto.SnAviTrackingRecordDTO;
import com.ca.mfd.prc.avi.dto.SnAviTrackingRecordVO;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseSetEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.avi.enums.AviTrackingEnum;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmAviServiceProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordOperService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.basedto.ExportByDcModel;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品过点信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("avitrackingrecord")
@Tag(name = "产品过点信息")
public class AviTrackingRecordController extends BaseController<AviTrackingRecordEntity> {

    @Autowired
    //private IPpsOrderService ppsOrderService;
    PpsOrderProvider ppsOrderProvider;
    private final IAviTrackingRecordService aviTrackingRecordService;
    @Autowired
    IAviTrackingRecordOperService aviTrackingRecordOperService;

    @Autowired
    //private IPmAviService pmAviService;
    private PmAviServiceProvider pmAviServiceProvider;

    @Autowired
    public AviTrackingRecordController(IAviTrackingRecordService aviTrackingRecordService) {
        this.crudService = aviTrackingRecordService;
        this.aviTrackingRecordService = aviTrackingRecordService;
    }

    @Operation(summary = "获取AVI站点")
    @PostMapping("getaviinfos")
    public ResultVO getAviInfos() {
        ResultVO<List<AviInfoDTO>> result = new ResultVO<>();
        result.setMessage("获取AVI站点成功");
        List<AviInfoDTO> avisData = pmAviServiceProvider.getAviInfos();
        return result.ok(avisData);
    }

    @Operation(summary = "手动过点数据")
    @PostMapping("savemanualpoint")
    public ResultVO<PpsOrderEntity> saveManualPoint(String tpsCode, String aviId) {
        return aviTrackingRecordService.saveManualPointWeb(tpsCode, aviId);
    }

    @Operation(summary = "第三方过点数据")
    @PostMapping("savethirdpointdata")
    public ResultVO<String> saveThirdPointData(String sn, String aviCode, Integer aviType, Integer avitrackingenum, Boolean isProcess, Date passTime) {
        aviTrackingRecordService.saveThirdPointData(sn, aviCode, aviType, AviTrackingEnum.fromInt(avitrackingenum, null), isProcess, passTime);
        aviTrackingRecordService.saveChange();
        return new ResultVO<String>().ok("");
    }

    @PostMapping("edit")
    @Operation(summary = "更新")
    @LogOperation("更新")
    @Override
    public ResultVO edit(@RequestBody AviTrackingRecordEntity data) {
        ResultVO<AviTrackingRecordEntity> result = new ResultVO<>();
        result.setMessage("保存成功");
        if (data == null || StringUtils.isBlank(data.getSn())) {
            throw new InkelinkException("条码不存在.");
        }
        PpsOrderEntity ppsOrder = ppsOrderProvider.getPpsOrderBySnOrBarcode(data.getSn().trim());
        if (ppsOrder == null || StringUtils.isBlank(ppsOrder.getSn())) {
            throw new InkelinkException("条码不存在");
        }
        data.setSn(ppsOrder.getSn());
        if (!data.getIsProcess()) {
            // 根据前端回传生成行为
            for (AviOperDto item : data.getAviOperList()) {
                AviTrackingRecordOperEntity vo = new AviTrackingRecordOperEntity();
                vo.setSn(data.getSn());
                vo.setInsertDt(data.getInsertDt());
                vo.setAction(item.getAction());
                vo.setIsProcess(false);
                vo.setOrderCategory(data.getOrderCategory());
                vo.setLineCode(data.getLineCode());
                vo.setAviCode(data.getAviCode());
                vo.setAviName(data.getAviName());
                vo.setPrcAviOperId(item.getPrcAviOperId());
                vo.setWorkshopCode(data.getWorkshopCode());
                vo.setProcessCount(0);
                vo.setRemark("");
                aviTrackingRecordOperService.insert(vo);
                data.setIsProcess(true);
            }
        }
        if (data.getId() <= 0) {
            crudService.save(data);
        } else {
            crudService.update(data);
        }
        //添加AS过点记录
        if (data.getMode() != 3) {
            aviTrackingRecordService.saveAsPointData(data.getSn(), data.getAviCode(), data.getAviTrackIngRecordType());
        }
        crudService.saveChange();
        return result.ok(data);
    }

    @PostMapping("getvehicepagedata")
    @Operation(summary = "获取分页数据")
    public ResultVO getVehicePageData(@RequestBody PageDataDto model) {
        ResultVO<PageData<AviTrackingRecordEntity>> result = new ResultVO<>();
        result.setMessage("获取AVI站点成功");
        PageData<AviTrackingRecordEntity> page = new PageData<>(new ArrayList<>(), 0);
        page.setPageIndex(model.getPageIndex());
        page.setPageSize(model.getPageSize());
        aviTrackingRecordService.getVehiceOrderPageDatas(model.getConditions(), model.getSorts(), page);
        return result.ok(page);
    }

    /**
     * 查询产品过点信息
     *
     * @param snInfo 查询条件
     * @return 查询产品过点信息
     */
    @PostMapping("getsnavitrackingrecord")
    @Operation(summary = "查询产品过点信息")
    public ResultVO<List<SnAviTrackingRecordVO>> getSnAviTrackingRecord(@RequestBody SnAviTrackingRecordDTO snInfo) {
        ResultVO<List<SnAviTrackingRecordVO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        result.setData(new ArrayList<>());
        Boolean isSnEmpty = snInfo == null || snInfo.getSn() == null || snInfo.getSn().size() == 0;
        Boolean isAviEmpty = snInfo.getPmAviCode() == null || snInfo.getPmAviCode().size() == 0;

        if (isSnEmpty || isAviEmpty) {
            return result;
        }
        List<SnAviTrackingRecordVO> aviInfo = aviTrackingRecordService.getEntityBySnAndAviCode(snInfo.getSn(), snInfo.getPmAviCode());
        return result.ok(aviInfo);
    }


    @PostMapping("getpagedata")
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<AviTrackingRecordEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<AviTrackingRecordEntity> pageData = crudService.page(model);
        pageData.getDatas().stream().forEach(s -> {
            s.setAttribute1(s.getAviCode());
        });
        PageData<AviTrackingRecordEntity> page = pageData;
        return new ResultVO<PageData<AviTrackingRecordEntity>>().ok(page, "获取数据成功");
    }

    @PostMapping("/provider/getdata")
    @Operation(summary = "根据条件获取分页数据")
    public ResultVO<List<AviTrackingRecordEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(aviTrackingRecordService.getData(conditions));
    }

    @PostMapping(value = "veexportbydc", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "dc-导出(整车)")
    public void veExportbydc(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        aviTrackingRecordService.setExcelColumnNames(model.getField());
        if( model.getConditions()==null){
            model.setConditions(new ArrayList<>());
        }
        model.getConditions().add(new ConditionDto("orderCategory","1", ConditionOper.Equal));
        aviTrackingRecordService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

    @PostMapping(value = "baexportbydc", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "dc-导出(电池)")
    public void baExportbydc(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        aviTrackingRecordService.setExcelColumnNames(model.getField());
        if( model.getConditions()==null){
            model.setConditions(new ArrayList<>());
        }
        model.getConditions().add(new ConditionDto("orderCategory","2", ConditionOper.Equal));
        aviTrackingRecordService.export(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }
}