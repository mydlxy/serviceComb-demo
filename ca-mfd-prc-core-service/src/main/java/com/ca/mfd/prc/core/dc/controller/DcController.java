package com.ca.mfd.prc.core.dc.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.dc.dto.DcDTO;
import com.ca.mfd.prc.core.dc.dto.DcDetail;
import com.ca.mfd.prc.core.dc.dto.DcDetailVO;
import com.ca.mfd.prc.core.dc.dto.DcTreeData;
import com.ca.mfd.prc.core.prm.dto.PermissionsVo;
import com.ca.mfd.prc.core.prm.dto.TreeItem;
import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;
import com.ca.mfd.prc.core.dc.service.IDcButtonConfigService;
import com.ca.mfd.prc.core.dc.service.IDcPageConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 */
@RestController
@RequestMapping("dc/dc")
@Tag(name = "页面配置")
public class DcController extends BaseApiController {
    @Autowired
    IDcPageConfigService dcPageConfigService;
    @Autowired
    IDcButtonConfigService dcButtonConfigService;


    @GetMapping(value = "/detail")
    @Operation(summary = "页面详情配置")
    public ResultVO detail(String code) {
        //数据结果集
        List<DcDetail> list = dcPageConfigService.getDcDetail(code);

        //筛选数据进行排序去重
        List<DcDetailVO> detailVOList = list.stream().filter(item -> item.getAuthorizationCode().equals(code)).sorted(Comparator.comparing(DcDetail::getDisplayNo))
                .map(c -> new DcDetailVO(c.getId(), c.getPageKey(), c.getPageName(), c.getAuthorizationCode(), "1")).distinct().collect(Collectors.toList());

        //返回前端对象list
        List<DcDetailVO> rtList = new ArrayList<>();
        for (DcDetailVO detailVO : detailVOList) {
            rtList.add(detailVO);
            //按钮数据
            List<DcDetailVO> pageBtn = list.stream().filter(item -> item.getId().equals(detailVO.getId()) && item.getButtonAuthorizationCode().equals(code)).sorted(Comparator.comparing(DcDetail::getDisplayNo))
                    .map(c -> new DcDetailVO(c.getButtonId(), c.getButtonKey(), c.getPageName() + ">" + c.getButtonName(), c.getButtonAuthorizationCode(), "2")).distinct().collect(Collectors.toList());
            if (pageBtn.size() > 0) {
                rtList.addAll(pageBtn);
            }
        }

        List<String> ids = rtList.stream().filter(e -> "2".equals(e.getType())).map(e -> e.getId()).collect(Collectors.toList());
        List<DcDetailVO> btnPermissions = list.stream().filter(item -> item.getButtonAuthorizationCode().equals(code) && !ids.contains(item.getButtonId())).sorted(Comparator.comparing(DcDetail::getDisplayNo))
                .map(c -> new DcDetailVO(c.getButtonId(), c.getButtonKey(), c.getPageName() + ">" + c.getButtonName(), c.getButtonAuthorizationCode(), "2")).distinct().collect(Collectors.toList());
        if (btnPermissions.size() > 0) {
            rtList.addAll(btnPermissions);
        }
        PermissionsVo vo = new PermissionsVo();
        vo.setPermissions(rtList);

        ResultVO<PermissionsVo> result = new ResultVO<>();
        result.setMessage("操作成功");
        return result.ok(vo);
    }

    @GetMapping(value = "pageconfig/tree")
    @Operation(summary = "获取页面配置树")
    public ResultVO pageConfigTree(String keyword) {
        List<DcPageConfigEntity> pages = dcPageConfigService.getListByKey(keyword);
        List<DcButtonConfigEntity> buttons = dcButtonConfigService.getData(null);

        List<TreeItem> prmList = new ArrayList<>();
        for (DcPageConfigEntity item : pages) {
            String modelName = StringUtils.isBlank(item.getPageName()) ? "默认" : item.getPageName();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            List<TreeItem> children = buttons.stream().filter(c -> Objects.equals(c.getPrcDcPageConfigId(), item.getId()))
                    .sorted(Comparator.comparing(c -> c.getDisplayNo())).map(btn -> {
                        TreeItem node = new TreeItem();
                        node.setKey(btn.getId().toString());
                        node.setLabel(btn.getButtonName());
                        node.setIsPrm(true);
                        node.setIcon("pi pi-file");

                        DcTreeData treeData = new DcTreeData();
                        treeData.setId(btn.getId().toString());
                        treeData.setKey(btn.getButtonKey());
                        treeData.setName(btn.getButtonName());
                        treeData.setColor(btn.getButtonColor());
                        treeData.setAuthorizationCode(btn.getAuthorizationCode());
                        treeData.setType(2);
                        node.setData(treeData);
                        return node;
                    }).collect(Collectors.toList());

            TreeItem pageNode = new TreeItem();
            pageNode.setKey(item.getId().toString());
            pageNode.setLabel(item.getPageName());
            pageNode.setIsPrm(true);
            pageNode.setIcon("pi pi-folder");
            DcTreeData data = new DcTreeData();
            data.setId(item.getId().toString());
            data.setKey(item.getPageKey());
            data.setName(item.getPageName());
            data.setAuthorizationCode(item.getAuthorizationCode());
            data.setType(1);
            pageNode.setData(data);
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(0, pageNode);
            initNodeLc(prmList, modelList, 0, children);
        }

        ResultVO<List<TreeItem>> result = new ResultVO<>();
        return result.ok(prmList);
    }

    void initNodeLc(List<TreeItem> nodes, List<String> modelList, int index, List<TreeItem> children) {
        String name = modelList.get(index);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), name))
                .findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(name);
            Map mp = new LinkedHashMap();
            mp.put("id", UUIDUtils.getEmpty());
            mp.put("pageName", path);
            node.setData(mp);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLc(node.getChildren(), modelList, index, children);
        } else {
            node.getChildren().addAll(children);
        }
    }

    @GetMapping(value = "/pageconfig/permission/tree")
    @Operation(summary = "根据权限代码获取按钮配置数据")
    public ResultVO pageConfigPermissionTree(String code) {
        List<DcDetail> dcPermission = dcPageConfigService.getDcDetail(code);
        List<SortDto> sortsCfg = new ArrayList<>();
        sortsCfg.add(new SortDto("", ConditionDirection.ASC));
        List<DcPageConfigEntity> permissionAll = dcPageConfigService.getData(null, sortsCfg, false);
        List<DcButtonConfigEntity> buttons = dcButtonConfigService.getData(null);

        List<TreeItem> permissions = new ArrayList<>();

        for (DcPageConfigEntity item : permissionAll) {
            String modelName = StringUtils.isBlank(item.getPageName()) ? "默认" : item.getPageName();
            List<String> modelList = ArraysUtils.splitNoEmpty(modelName, "->");

            List<TreeItem> children = buttons.stream().filter(c -> Objects.equals(c.getPrcDcPageConfigId(), item.getId()))
                    .sorted(Comparator.comparing(c -> c.getDisplayNo())).map(btn -> {
                        TreeItem node = new TreeItem();
                        node.setKey(btn.getId().toString());
                        node.setLabel(modelName + "->" + btn.getButtonName());
                        node.setIsPrm(true);
                        node.setIcon("pi pi-file");
                        node.setIsSelected(dcPermission.stream().filter(w -> w.getButtonId().equals(btn.getId().toString())
                                && StringUtils.equals(w.getButtonAuthorizationCode(), code)).findFirst().orElse(null) != null
                        );
                        DcTreeData treeData = new DcTreeData();
                        treeData.setId(btn.getId().toString());
                        treeData.setKey(btn.getButtonKey());
                        treeData.setName(btn.getButtonName());
                        treeData.setColor(btn.getButtonColor());
                        treeData.setAuthorizationCode(btn.getAuthorizationCode());
                        treeData.setType(2);
                        node.setData(treeData);
                        return node;
                    }).collect(Collectors.toList());

            String label = modelName;
            DcDetail prm = dcPermission.stream().filter(w -> w.getId().equals(item.getId().toString())
                    && w.getButtonAuthorizationCode().equals(code)).findFirst().orElse(null);
            TreeItem pageNode = new TreeItem();
            pageNode.setKey(item.getId().toString());
            pageNode.setLabel(label);
            pageNode.setIsPrm(true);
            pageNode.setIcon("pi pi-folder");
            pageNode.setIsSelected(prm != null);
            DcTreeData data = new DcTreeData();
            data.setId(item.getId().toString());
            data.setKey(item.getPageKey());
            data.setName(item.getPageName());
            data.setAuthorizationCode(item.getAuthorizationCode());
            data.setType(1);
            pageNode.setData(data);
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(0, pageNode);
            initNodeLb(permissions, modelList, 0, children);
        }

        List<Map> selections = new ArrayList<>();
        selectionNodeLb(permissions, selections);

        ResultVO<Map> result = new ResultVO<>();
        Map resMp = new LinkedHashMap();
        resMp.put("permissions", permissions);
        resMp.put("selections", selections);
        return result.ok(resMp);
    }

    void initNodeLb(List<TreeItem> nodes, List<String> modelList, int index, List<TreeItem> children) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= index; i++) {
            sb.append(modelList.get(i));
            sb.append("->");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        String label = sb.toString();
        String path = sb.toString();
        String key = EncryptionUtils.md5(path);
        TreeItem node = nodes.stream().filter(w -> StringUtils.equals(w.getLabel(), label)).findFirst().orElse(null);
        if (node == null) {
            node = new TreeItem();
            node.setKey(key);
            node.setLabel(label);
            Map data = new LinkedHashMap();
            data.put("id", Constant.DEFAULT_ID);
            data.put("pageName", path);
            node.setData(data);
            nodes.add(node);
        }
        index++;
        if (modelList.size() > index) {
            initNodeLb(node.getChildren(), modelList, index, children);
        } else {
            node.getChildren().addAll(children);
        }
    }

    void selectionNodeLb(List<TreeItem> permissions, List<Map> selections) {
        for (TreeItem item : permissions) {
            if (item.getIsSelected()) {
                Map mp = new HashMap(5);
                mp.put("icon", item.getIcon());
                mp.put("key", item.getKey());
                mp.put("isPrm", item.getIsPrm());
                mp.put("data", item.getData());
                selections.add(mp);
            }
            if (item.getChildren().size() > 0) {
                selectionNodeLb(item.getChildren(), selections);
            }
        }
    }

    @PostMapping(value = "/pageconfig/save")
    @Operation(summary = "根据权限代码绑定页面")
    public ResultVO pageConfigSave(@RequestBody DcDTO.PrmModel model) {
        if (model.getDatas().isEmpty() || model.getDatas() == null) {
            model.setDatas(new ArrayList<>());
        }
        List<DcDTO.ItemModel> pages = model.getDatas().stream().filter(w -> w.getType().equals(1)).collect(Collectors.toList());
        //根据权限代码获取页面数据
        List<DcPageConfigEntity> pateList = dcPageConfigService.getListByCode(model.getCode());
        for (DcDTO.ItemModel item : pages) {
            if (StringUtils.isBlank(item.getId())) {
                continue;
            }
            DcPageConfigEntity data = pateList.stream().filter(w -> w.getId().equals(item.getId())).findFirst().orElse(null);
            if (data == null) {
                dcPageConfigService.update(new LambdaUpdateWrapper<DcPageConfigEntity>().eq(DcPageConfigEntity::getId, item.getId()).set(DcPageConfigEntity::getAuthorizationCode, model.getCode()));
            } else {
                pateList.remove(data);
            }
        }
        if (pateList.size() > 0) {
            List<Long> ids = pateList.stream().map(w -> w.getId()).collect(Collectors.toList());
            dcPageConfigService.update(new LambdaUpdateWrapper<DcPageConfigEntity>().in(DcPageConfigEntity::getId, ids).set(DcPageConfigEntity::getAuthorizationCode, ""));
        }

        List<DcDTO.ItemModel> buttons = model.getDatas().stream().filter(w -> w.getType() == 2).collect(Collectors.toList());
        List<DcButtonConfigEntity> btnList = dcButtonConfigService.getListByCode(model.getCode());
        for (DcDTO.ItemModel item : buttons) {
            if (StringUtils.isBlank(item.getId())) {
                continue;
            }
            DcButtonConfigEntity data = btnList.stream().filter(w -> w.getId().equals(item.getId())).findFirst().orElse(null);
            if (data == null) {
                dcButtonConfigService.update(new LambdaUpdateWrapper<DcButtonConfigEntity>().eq(DcButtonConfigEntity::getId, item.getId()).set(DcButtonConfigEntity::getAuthorizationCode, model.getCode()));
            } else {
                btnList.remove(data);
            }
        }

        if (btnList.size() > 0) {
            List<Long> ids = btnList.stream().map(w -> w.getId()).collect(Collectors.toList());
            dcButtonConfigService.update(new LambdaUpdateWrapper<DcButtonConfigEntity>().in(DcButtonConfigEntity::getId, ids).set(DcButtonConfigEntity::getAuthorizationCode, ""));
        }

        dcPageConfigService.saveChange();
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("操作成功");
        return result.ok("");
    }

    @PostMapping(value = "/pageconfig/del")
    @Operation(summary = "接口权限删除绑定")
    public ResultVO pageConfigDel(@RequestBody DcDTO.DelModel model) {
        if (model.getIds().size() < 1) {
            throw new InkelinkException("没有可以操作的数据");
        }
        List<String> ids = model.getIds();
        if (model.getType().equals(1)) {
            dcPageConfigService.update(new LambdaUpdateWrapper<DcPageConfigEntity>().in(DcPageConfigEntity::getId, ids).set(DcPageConfigEntity::getAuthorizationCode, ""));
            dcPageConfigService.saveChange();
        } else if (model.getType().equals(2)) {
            dcButtonConfigService.update(new LambdaUpdateWrapper<DcButtonConfigEntity>().in(DcButtonConfigEntity::getId, ids).set(DcButtonConfigEntity::getAuthorizationCode, ""));
            dcButtonConfigService.saveChange();
        }
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("操作成功");
        return result.ok("");
    }
}
