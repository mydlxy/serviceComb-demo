package com.ca.mfd.prc.pm.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.validator.AssertUtils;
import com.ca.mfd.prc.pm.dto.ShopExcelDto;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import com.ca.mfd.prc.pm.service.IPmBopService;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import com.ca.mfd.prc.pm.service.IPmToolService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import com.ca.mfd.prc.pm.service.IPmWorkStationOperBookService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * @author inkelink
 * @Description: 超级BOPController
 * @date 2023年08月30日
 * @变更说明 BY inkelink At 2023年08月30日
 */
@RestController
@RequestMapping("pmbop")
@Tag(name = "超级BOP服务", description = "超级BOP")
public class PmBopController extends BaseController<PmBopEntity> {

    private final IPmBopService pmBopService;
    private final IPmWoService pmWoService;
    private final IPmWorkstationMaterialService pmWorkstationMaterialService;
    private final IPmToolService pmToolService;
    private final IPmToolJobService pmToolJobService;
    private final IPmWorkStationOperBookService pmWorkStationOperBookService;
    private final IPmWorkStationService pmWorkStationService;



    @Autowired
    public PmBopController(IPmBopService pmBopService,
                           IPmWoService pmWoService,
                           IPmWorkstationMaterialService pmWorkstationMaterialService,
                           IPmToolService pmToolService,
                           IPmToolJobService pmToolJobService,
                           IPmWorkStationOperBookService pmWorkStationOperBookService,
                           IPmWorkStationService pmWorkStationService
    ) {
        this.crudService = pmBopService;
        this.pmBopService = pmBopService;
        this.pmWoService = pmWoService;
        this.pmWorkstationMaterialService = pmWorkstationMaterialService;
        this.pmToolService = pmToolService;
        this.pmToolJobService = pmToolJobService;
        this.pmWorkStationOperBookService = pmWorkStationOperBookService;
        this.pmWorkStationService = pmWorkStationService;
    }

//    @Override
//    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Operation(summary = "更新")
//    public ResultVO edit(@RequestBody @Valid PmBopEntity dto) {
//        List<Long> workstationIds = dto.getWorkstationIds();
//        if(workstationIds!= null && !workstationIds.isEmpty()){
//            dto.getWorkstationIds().clear();
//            List<PmBopEntity> dtosMustBeInsert = new ArrayList<>(dto.getWorkstationIds().size());
//            List<PmBopEntity> dtosMustBeUpdate = new ArrayList<>(1);
//            for(Long workstationId : workstationIds){
//                PmBopEntity newPmBopEntity = new PmBopEntity();
//                BeanUtils.copyProperties(dto,newPmBopEntity);
//                if(newPmBopEntity.getPrcPmWorkstationId().equals(workstationId)
//                    && newPmBopEntity.getId() != null && newPmBopEntity.getId() != 0){
//                    dtosMustBeUpdate.add(newPmBopEntity);
//                }else{
//                    newPmBopEntity.setId(0L);
//                    newPmBopEntity.setPrcPmWorkstationId(workstationId);
//                    dtosMustBeInsert.add(newPmBopEntity);
//                }
//            }
//            dtosMustBeInsert.addAll(dtosMustBeUpdate);
//            if(!dtosMustBeInsert.isEmpty()){
//                for(PmBopEntity pmBopEntity : dtosMustBeInsert){
//                    doEdit(pmBopEntity);
//                }
//            }
//        }else{
//            doEdit(dto);
//        }
//        pmBopService.saveChange();
//        return new ResultVO<PmBopEntity>().ok(dto, "保存成功");
//    }
    @Override
    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO edit(@RequestBody @Valid PmBopEntity dto) {
        doEdit(dto);
        pmBopService.saveChange();
       return new ResultVO<PmBopEntity>().ok(dto, "保存成功");
    }


    @PostMapping(value = "editbop", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO editBop(@RequestBody @Valid PmBopEntity dto) {
        ClassUtil.defaultValue(dto);
        Long id = dto.getId();
        pmBopService.validDataUnique(dto.getId(), "PRC_PM_WORKSTATION_ID", String.valueOf(dto.getPrcPmWorkstationId()),
                "已经存在相同工步编号的数据,同工位工步编号唯一",
                "PROCESS_STEP",String.valueOf(dto.getProcessStep()),
                "FEATURE_CODE",dto.getFeatureCode());
        if (id == null || id == 0) {
            pmBopService.save(dto);
        } else {
            pmBopService.update(dto);
        }
        delBopWithProcessStepIsBlank(dto);
        delOperBook(dto.getPrcPmWorkstationId(),dto.getProcessStep(),dto.getFeatureCode());
        if(StringUtils.isNotBlank(dto.getActionImage())){
            String[] actionImages = dto.getActionImage().split(",");
            int index = 0;
            for(String eachImage : actionImages){
                PmBopEntity newDto = new PmBopEntity();
                BeanUtils.copyProperties(dto,newDto);
                newDto.setActionImage(eachImage);
                saveOperBook(newDto,1,++index);
            }
        }
        if(StringUtils.isNotBlank(dto.getMaterialImage())){
            String[] materialImages = dto.getMaterialImage().split(",");
            int index = 0;
            for(String eachImage : materialImages){
                PmBopEntity newDto = new PmBopEntity();
                BeanUtils.copyProperties(dto,newDto);
                newDto.setMaterialImage(eachImage);
                saveOperBook(newDto,2,++index);
            }
        }
        pmBopService.saveChange();
        return new ResultVO<PmBopEntity>().ok(dto, "保存成功");
    }

    private void delBopWithProcessStepIsBlank(PmBopEntity dto){
        UpdateWrapper<PmBopEntity> uw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmBopEntity> luw = uw.lambda();
        luw.set(PmBopEntity :: getIsDelete,true);
        luw.eq(PmBopEntity :: getPrcPmWorkstationId,dto.getPrcPmWorkstationId());
        luw.eq(PmBopEntity :: getProcessNo,dto.getProcessNo());
        luw.eq(PmBopEntity :: getFeatureCode,dto.getFeatureCode());
        luw.eq(PmBopEntity :: getIsDelete,false);
        luw.eq(PmBopEntity :: getProcessStep,"");
        pmBopService.update(uw);
    }

    private PmBopEntity doEdit(PmBopEntity dto){
        ClassUtil.defaultValue(dto);
        Long id = dto.getId();
        if (id == null || id == 0) {
            pmBopService.save(dto);
        } else {
            pmBopService.update(dto);
        }
        syncWoMaterialToolJob(dto);
        return dto;
    }

//    private void setForeignId(PmBopEntity dto,Long id){
//        PmWorkStationEntity pmWorkStationEntity = this.pmWorkStationService.get(id);
//        if(pmWorkStationEntity == null || pmWorkStationEntity.getIsDelete()){
//            throw new InkelinkException(String.format("工位id[%s]对应工位不存在或已删除",id));
//        }
//        dto.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
//        dto.setPrcPmWorkshopId(pmWorkStationEntity.getPrcPmWorkshopId());
//    }

    private void syncWoMaterialToolJob(PmBopEntity dto){
        PmToolEntity tool = null;
        PmWoEntity wo = null;
        if(StringUtils.isNotBlank(dto.getWoCode())){
            wo = saveWo(dto);
        }
        if(StringUtils.isNotBlank(dto.getMaterialNo())){
            saveMaterial(dto);
        }
        if(StringUtils.isNotBlank(dto.getToolCode())){
            tool = saveTool(dto);
        }
        if(StringUtils.isNotBlank(dto.getJob())
            && tool != null && wo != null){
            saveToolJob(dto, tool, wo);
        }
        if(StringUtils.isNotBlank(dto.getActionImage())){
            saveOperBook(dto,1,0);
        }
        if(StringUtils.isNotBlank(dto.getMaterialImage())){
            saveOperBook(dto,2,0);
        }
    }

//    private void syncWoMaterialToolJob(PmBopEntity dto){
//        switch (dto.getModelType()){
//            case WO:
//                saveWo(dto);
//                this.pmBopService.saveChange();
//                saveTool(dto);
//                this.pmBopService.saveChange();
//                saveToolJob(dto);
//                break;
//            case MATERIAL:
//                saveMaterial(dto);
//                break;
//            default:
//                //saveOperBook(dto);
//        }
//    }

    private PmWoEntity saveWo(PmBopEntity dto){
        if(StringUtils.isBlank(dto.getWoCode())){
            throw new InkelinkException("工艺编码不能为空");
        }
        PmWoEntity wo = new PmWoEntity();
        Map<String,String> mapping = pmBopService.getWoBopMapping();
        mapping.put("prcPmWorkstationId","prcPmWorkstationId");
        ClassUtil.copeVal(dto,wo,mapping);
        wo.setPrcPmWorkshopId(dto.getPrcPmWorkshopId());
        wo.setPrcPmLineId(dto.getPrcPmLineId());
        wo.setPrcPmWorkstationId(dto.getPrcPmWorkstationId());
        ClassUtil.validNullByNullAnnotation(wo);
        List<PmWoEntity> wos = pmWoService.getPmWoEntityByWoCode(wo.getPrcPmWorkstationId(),wo.getWoCode(),false);
        if(wos.isEmpty()){
            pmWoService.save(wo);
        }else{
            PmWoEntity existsWo = wos.get(0);
            Long id = existsWo.getId();
            BeanUtils.copyProperties(wo,existsWo);
            existsWo.setId(id);
            pmWoService.update(existsWo);
            wo.setId(id);
        }
        return wo;

    }

    private void saveMaterial(PmBopEntity dto){
        if(StringUtils.isBlank(dto.getMaterialNo())){
            throw new InkelinkException("请配置物料号");
        }
        PmWorkstationMaterialEntity material = new PmWorkstationMaterialEntity();
        Map<String,String> mapping = pmBopService.getMaterialBopMapping();
        mapping.put("prcPmWorkstationId","prcPmWorkstationId");
        mapping.remove("materialNum");
        //mapping.put("materialNum", "materialQuantity");
        ClassUtil.copeVal(dto,material,mapping);

        material.setMaterialNum(Double.valueOf(dto.getMaterialQuantity()));
        material.setPrcPmWorkshopId(dto.getPrcPmWorkshopId());
        material.setPrcPmLineId(dto.getPrcPmLineId());
        material.setPrcPmWorkstationId(dto.getPrcPmWorkstationId());
        ClassUtil.validNullByNullAnnotation(material);
        List<PmWorkstationMaterialEntity> materials = pmWorkstationMaterialService.getPmWorkstationEntityByMaterialNo(material.getPrcPmWorkstationId()
                ,material.getMaterialNo(),false);
        if(materials.isEmpty()){
            pmWorkstationMaterialService.save(material);
        }else{
            PmWorkstationMaterialEntity existsMaterial = materials.get(0);
            Long id = existsMaterial.getId();
            BeanUtils.copyProperties(material,existsMaterial);
            existsMaterial.setId(id);
            pmWorkstationMaterialService.update(existsMaterial);
        }
    }

    private PmToolEntity saveTool(PmBopEntity dto){
        if(StringUtils.isBlank(dto.getToolCode())){
            throw new InkelinkException("工具编码不能为空");
        }
        PmToolEntity tool = new PmToolEntity();
        Map<String,String> mapping = pmBopService.getToolBopMapping();
        mapping.put("prcPmWorkstationId","prcPmWorkstationId");
        ClassUtil.copeVal(dto,tool,mapping);
        tool.setPrcPmWorkshopId(dto.getPrcPmWorkshopId());
        tool.setPrcPmLineId(dto.getPrcPmLineId());
        tool.setPrcPmWorkstationId(dto.getPrcPmWorkstationId());
        ClassUtil.validNullByNullAnnotation(tool);
        List<PmToolEntity> tools = pmToolService.getPmToolEntityByToolCode(tool.getPrcPmWorkstationId()
                ,tool.getToolCode(),false);
        if(tools.isEmpty()){
            pmToolService.save(tool);
        }else{
            PmToolEntity existsTool = tools.get(0);
            Long id = existsTool.getId();
            BeanUtils.copyProperties(tool,existsTool);
            existsTool.setId(id);
            pmToolService.update(existsTool);
            tool.setId(id);
        }
        return tool;
    }

    private void saveOperBook(PmBopEntity dto,int bookType,int displayNo){
        PmWorkstationOperBookEntity operBook = new PmWorkstationOperBookEntity();
        Map<String,String> mapping = pmBopService.getOperBookBopMapping();
        mapping.put("prcPmWorkstationId","prcPmWorkstationId");
        if(bookType == 1){
            mapping.put("woBookPath","actionImage");
        }else{
            mapping.put("woBookPath","materialImage");
        }
        ClassUtil.copeVal(dto,operBook,mapping);
        operBook.setPrcPmWorkshopId(dto.getPrcPmWorkshopId());
        operBook.setPrcPmLineId(dto.getPrcPmLineId());
        operBook.setPrcPmWorkstationId(dto.getPrcPmWorkstationId());
        operBook.setWoBookType(bookType);
        operBook.setDisplayNo(displayNo);
        operBook.setProcessStep(dto.getProcessStep());
        if(bookType == 1){
            operBook.setWoBookName("操作图册");
        }else{
            operBook.setWoBookName("零件图册");
        }
        ClassUtil.validNullByNullAnnotation(operBook);
        pmWorkStationOperBookService.save(operBook);
//        List<PmWorkstationOperBookEntity> operBookEntities = pmWorkStationOperBookService.getBookList(operBook.getWoBookType(),dto.getPrcPmWorkstationId(), operBook.getPrcPmProcessNo());
//        if(operBookEntities.isEmpty()){
//            pmWorkStationOperBookService.save(operBook);
//        }else{
//            PmWorkstationOperBookEntity existsBookEntity = operBookEntities.get(0);
//            Long id = existsBookEntity.getId();
//            BeanUtils.copyProperties(operBook,existsBookEntity);
//            existsBookEntity.setId(id);
//            pmWorkStationOperBookService.update(existsBookEntity);
//        }
    }

    private void delOperBook(Long workstationId,String processStep,String featureCode){
        ConditionDto workstationIdCondition = new ConditionDto("prcPmWorkstationId", String.valueOf(workstationId),ConditionOper.Equal);
        ConditionDto processStepCondition = new ConditionDto("processStep", processStep,ConditionOper.Equal);
        ConditionDto featureCodeCondition = new ConditionDto("featureCode", featureCode,ConditionOper.Equal);
        pmWorkStationOperBookService.delete(Arrays.asList(workstationIdCondition,processStepCondition,featureCodeCondition));
    }



    private void saveToolJob(PmBopEntity dto,PmToolEntity tool,PmWoEntity wo){
        PmToolJobEntity job = new PmToolJobEntity();
        Map<String,String> mapping = pmBopService.getJobBopMapping();
        mapping.put("prcPmWorkstationId","prcPmWorkstationId");
        ClassUtil.copeVal(dto,job,mapping);
//        List<PmToolEntity> pmTools = pmToolService.getPmToolEntityByToolCode(job.getPrcPmWorkstationId(),
//                job.getToolCode(),false);
//        if(pmTools.isEmpty()){
//            throw new InkelinkException(String.format("工具号[%s]不存在",job.getToolCode()));
//        }
       // PmToolEntity tool = pmTools.get(0);
        job.setPrcPmToolId(tool.getId());
        job.setPrcPmWorkshopId(dto.getPrcPmWorkshopId());
        job.setPrcPmLineId(dto.getPrcPmLineId());
        job.setPrcPmWorkstationId(dto.getPrcPmWorkstationId());
//        List<PmWoEntity> pmWos = pmWoService.getPmWoEntityByWoCode(job.getPrcPmWorkstationId(),
//                job.getWoCode(),false);
//        if(pmWos.isEmpty()){
//            throw new InkelinkException(String.format("操作编码[%s]不存在",job.getWoCode()));
//        }
//        PmWoEntity wo = pmWos.get(0);
        if(wo.getWoType() != 1){
            throw new InkelinkException(String.format("操作编码[%s]对应的操作类型不是[SCR-拧紧]类型",job.getWoCode()));
        }
        job.setPmWoId(wo.getId());
        List<PmToolJobEntity> jobs = pmToolJobService.getPmToolJobEntityByJobNo(job.getPrcPmToolId()
                ,job.getJobNo(),false);
        ClassUtil.validNullByNullAnnotation(job);
        if(jobs.isEmpty()){
            pmToolJobService.save(job);
        }else{
            PmToolJobEntity existsJob = jobs.get(0);
            Long id = existsJob.getId();
            BeanUtils.copyProperties(job,existsJob);
            existsJob.setId(id);
            pmToolJobService.update(existsJob);
        }
    }



    @GetMapping(value = "getbyworkstationid", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取工位下bop")
    public List<PmBopEntity> getByWorkStationId(Long stationId){
        return this.pmBopService.getByWorkStationId(stationId);
    }

    @PostMapping(value = "getpagedatagroupbyprocessno", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<PmBopEntity>> getpagedataGroupByProcessNo(@RequestBody PageDataDto model) {
        PageData<PmBopEntity> page = pmBopService.pageGroup(model);
        return new ResultVO<PageData<PmBopEntity>>().ok(page, "获取数据成功");
    }

    @PostMapping(value = "editbyprocessno", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新工序")
    public ResultVO<?> editByProcessNo(@RequestBody @Valid PmBopEntity dto) {
        pmBopService.updateByProcessNo(dto);
        pmBopService.saveChange();
        return new ResultVO<>().ok(dto, "保存成功");
    }

    @PostMapping(value = "delbyprocessno", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "删除工序")
    public ResultVO<?> delByProcessno(@RequestBody @Valid PmBopEntity dto) {
        //效验数据
        pmBopService.delByProcessNo(dto);
        pmBopService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @Override
    @PostMapping(value = "import", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "导入")
    public ResultVO importExcel(MultipartHttpServletRequest req) throws Exception {
        MultipartFile file = req.getFile(req.getFileNames().next());
        pmBopService.importExcel(file.getInputStream());
        return new ResultVO<String>().ok("", "导入数据成功");
    }

    @Operation(summary = "导出")
    @Parameter(name = "shopCode", description = "车间编码")
    @GetMapping(value = "exportbop", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void export(String shopCode, HttpServletResponse response) throws IOException {
        pmBopService.export(shopCode, response);
    }

    @Operation(summary = "导出")
    @Parameter(name = "shopCode", description = "车间编码")
    @PostMapping(value = "exportbop", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void export(@RequestBody ShopExcelDto shopExcelDto , HttpServletResponse response) throws IOException {
        pmBopService.export(shopExcelDto.getShopCode(), response);
    }



}