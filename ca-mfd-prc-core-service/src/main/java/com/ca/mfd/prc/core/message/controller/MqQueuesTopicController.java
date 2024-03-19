package com.ca.mfd.prc.core.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.validator.AssertUtils;
import com.ca.mfd.prc.core.message.entity.MqQueuesNotesEntity;
import com.ca.mfd.prc.core.message.entity.MqQueuesTopicEntity;
import com.ca.mfd.prc.core.message.service.IMqQueuesNotesService;
import com.ca.mfd.prc.core.message.service.IMqQueuesTopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * MQ主题
 *
 * @author jay.he
 * @date 2023-09-05
 */
@RestController
@RequestMapping("message/mqqueuestopic")
@Tag(name = "MQ主题")
public class MqQueuesTopicController extends BaseController<MqQueuesTopicEntity> {

    @Autowired
    IMqQueuesTopicService mqQueuesTopicService;
    @Autowired
    IMqQueuesNotesService mqQueuesNotesService;

    @Autowired
    public MqQueuesTopicController(IMqQueuesTopicService mqQueuesTopicService) {
        this.crudService = mqQueuesTopicService;
        this.mqQueuesTopicService = mqQueuesTopicService;
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<MqQueuesTopicEntity>> getPageData(@RequestBody PageDataDto model) {
        PageData<MqQueuesTopicEntity> page = crudService.page(model);
        if (CollectionUtils.isEmpty(page.getDatas())) {
            return new ResultVO<PageData<MqQueuesTopicEntity>>().ok(page, "获取数据成功");
        }
        List<String> groupNameList = page.getDatas().stream().map(c -> c.getGroupName()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupNameList)) {
            return new ResultVO<PageData<MqQueuesTopicEntity>>().ok(page, "获取数据成功");
        }
        QueryWrapper<MqQueuesNotesEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(MqQueuesNotesEntity::getGroupName, groupNameList);
        List<MqQueuesNotesEntity> notesEntities = mqQueuesNotesService.getData(queryWrapper, false);
        if (CollectionUtils.isEmpty(notesEntities)) {
            return new ResultVO<PageData<MqQueuesTopicEntity>>().ok(page, "获取数据成功");
        }
        if (!CollectionUtils.isEmpty(notesEntities)) {
            notesEntities.stream().forEach(c -> {
                c.setTopic(c.getClassName());
                c.setChannelName(c.getNamespaceName());
            });
        }
        //通过groupName分组
        Map<String, List<MqQueuesNotesEntity>> inputDataGroup = notesEntities.stream().collect(Collectors.groupingBy(c -> c.getGroupName()));
        Iterator<Map.Entry<String, List<MqQueuesNotesEntity>>> iteratorShopCalendar = inputDataGroup.entrySet().iterator();
        while (iteratorShopCalendar.hasNext()) {
            Map.Entry<String, List<MqQueuesNotesEntity>> entry = iteratorShopCalendar.next();
            String key = entry.getKey();//groupName
            List<MqQueuesNotesEntity> value = entry.getValue();
            for (MqQueuesTopicEntity eachTopic : page.getDatas()) {
                if (eachTopic.getGroupName().equals(key)) {
                    eachTopic.setChildren(value);
                    break;
                }
            }
        }
        return new ResultVO<PageData<MqQueuesTopicEntity>>().ok(page, "获取数据成功");
    }

    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO edit(@RequestBody MqQueuesTopicEntity dto) {
        if (!StringUtils.hasText(dto.getGroupName())) {
            throw new InkelinkException("主题名称不能为空！");
        }
        Long id = crudService.currentModelGetKey(dto);
        QueryWrapper<MqQueuesTopicEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesTopicEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(MqQueuesTopicEntity::getGroupName, dto.getGroupName());
        List<MqQueuesTopicEntity> topicEntities = mqQueuesTopicService.getData(queryWrapper, false);
        MqQueuesTopicEntity dbMqQueuesTopicEntity = null;
        if (!CollectionUtils.isEmpty(topicEntities)) {
            dbMqQueuesTopicEntity = topicEntities.get(0);
        }
        if (id == null || id <= 0) {
            //新增，数据库中不能有同名记录
            if (dbMqQueuesTopicEntity != null) {
                throw new InkelinkException("主题" + dto.getGroupName() + "已经存在！");
            }
            crudService.save(dto);
        } else {
            //更新，主题名称不能跟数据库中其他主题名称一样
            if (dbMqQueuesTopicEntity != null && !dbMqQueuesTopicEntity.getId().equals(dto.getId())) {
                throw new InkelinkException("主题" + dto.getGroupName() + "已经存在！");
            }
            crudService.update(dto);
        }
        crudService.saveChange();
        return new ResultVO<MqQueuesTopicEntity>().ok(dto, "保存成功");
    }


    @PostMapping(value = "del", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "删除")
    public ResultVO delete(@RequestBody IdsModel model) {
        //效验数据
        AssertUtils.isArrayEmpty(model.getIds(), "id");
        QueryWrapper<MqQueuesTopicEntity> queryTopicWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MqQueuesTopicEntity> lambdaQueryTopicWrapper = queryTopicWrapper.lambda();
        lambdaQueryTopicWrapper.in(MqQueuesTopicEntity::getId, Arrays.asList(model.getIds()));
        List<MqQueuesTopicEntity> topicEntities = mqQueuesTopicService.getData(queryTopicWrapper, false);
        if (!CollectionUtils.isEmpty(topicEntities)) {
            for (MqQueuesTopicEntity eachTopic : topicEntities) {
                QueryWrapper<MqQueuesNotesEntity> queryNotesWrapper = new QueryWrapper<>();
                LambdaQueryWrapper<MqQueuesNotesEntity> lambdaQueryNotesWrapper = queryNotesWrapper.lambda();
                lambdaQueryNotesWrapper.eq(MqQueuesNotesEntity::getGroupName, eachTopic.getGroupName());
                List<MqQueuesNotesEntity> notesEntities = mqQueuesNotesService.getData(queryNotesWrapper, false);
                if (!CollectionUtils.isEmpty(notesEntities)) {
                    throw new InkelinkException("主题" + eachTopic.getGroupName() + "下有通道，不能删除！");
                }
            }
        }

        crudService.delete(model.getIds());
        crudService.saveChange();
        return new ResultVO<String>().ok("", "删除成功");
    }

    @GetMapping(value = "gettopicscombox")
    @Operation(summary = "获取所有的主题")
    public ResultVO getTopicsCombox() {
        List<MqQueuesTopicEntity> mqQueuesTopicEntities = mqQueuesTopicService.getData(new QueryWrapper(), false);

        List<ComboInfoDTO> comboInfoDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(mqQueuesTopicEntities)) {
            for (MqQueuesTopicEntity each : mqQueuesTopicEntities) {
                ComboInfoDTO data = new ComboInfoDTO();
                data.setText(each.getGroupName());
                data.setValue(each.getAppId());
                comboInfoDTOS.add(data);
            }
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(comboInfoDTOS, "获取数据成功");
    }


}
