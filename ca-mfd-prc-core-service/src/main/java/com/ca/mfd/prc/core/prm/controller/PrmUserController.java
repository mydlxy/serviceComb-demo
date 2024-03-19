package com.ca.mfd.prc.core.prm.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.ChangePasswordModel;
import com.ca.mfd.prc.core.prm.dto.PrmUserVO;
import com.ca.mfd.prc.core.prm.dto.UpdatePassword;
import com.ca.mfd.prc.core.prm.dto.UserDTO;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("ucenter/prmuser")
@Tag(name = "用户表")
public class PrmUserController extends BaseController<PrmUserEntity> {

    private final IPrmUserService prmUserService;
    protected Map<String, String> excelColumnNames = new LinkedHashMap<>();
    @Autowired
    private IdentityHelper identityHelper;

    {
        excelColumnNames.put("Code", "工号");
        excelColumnNames.put("UserName", "用户名");
        excelColumnNames.put("NickName", "姓名");
        excelColumnNames.put("Phone", "联系电话");
        excelColumnNames.put("Email", "邮箱");
        excelColumnNames.put("RoleName", "角色");
        excelColumnNames.put("DepartmentName", "部门");
    }

    @Autowired
    public PrmUserController(IPrmUserService prmUserService) {
        this.crudService = prmUserService;
        this.prmUserService = prmUserService;
    }

    @GetMapping(value = "/removelock")
    @Operation(summary = "用户-解锁")
    public ResultVO removeLock(String username) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("成功解锁");
        UpdateWrapper<PrmUserEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PrmUserEntity::getFrozenDt, DateUtils.addDateMinutes(new Date(), -10))
                .eq(PrmUserEntity::getUserName, username);
        prmUserService.update(upset);
        prmUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "getpagedata")
    @Operation(summary = "兼容ua获取用户列表接口")
    @Override
    public ResultVO<PageData<PrmUserEntity>> getPageData(@RequestBody PageDataDto model) {
        ResultVO<PageData<PrmUserEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        if (model != null) {
            if (model.getConditions() == null) {
                model.setConditions(new ArrayList<>());
            }
            model.getConditions().add(new ConditionDto("userName", Constant.SYSTEM_MANAGER, ConditionOper.Unequal));
            if (model.getPageIndex() == null) {
                model.setPageIndex(1);
            }
            if (model.getPageSize() == null) {
                model.setPageSize(20);
            }
            prmUserService.getDataByPage(model);
        }
        PageData<PrmUserEntity> page = crudService.page(model);
        if (page != null && page.getDatas() != null) {
            for (PrmUserEntity et : page.getDatas()) {
                et.setFullName(et.getNickName() + "/" + et.getUserName());
            }
        }
        return result.ok(page);
    }

    @GetMapping(value = "getbyid")
    @Operation(summary = "兼容ua获取用户信息接口")
    @Override
    public ResultVO<PrmUserEntity> getById(@RequestParam(value = "id") String id) {
        ResultVO<PrmUserEntity> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        PrmUserEntity data = prmUserService.get(id);
        if (data == null) {
            return result.ok(null);
        }
        data.setFullName(data.getNickName() + "/" + data.getUserName());
        return result.ok(data);
    }

    @PostMapping(value = "del")
    @Operation(summary = "删除选中用户")
    @Override
    public ResultVO delete(@RequestBody IdsModel model) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("保存数据成功");
        prmUserService.delete(model.getIds());
        prmUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "saveuseroles")
    @Operation(summary = "兼容ua的用户保存接口")
    public ResultVO save(@RequestBody UserDTO.SaveModel model) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("数据保存成功");

        if (StringUtils.equals(model.getUserName(), Constant.SYSTEM_MANAGER)
                || StringUtils.equals(model.getUserName(), Constant.SYSTEM_ADMINROLE)) {
            result.setMessage("超级账户不允许保存" + model.getUserName());
            return result.ok("");
        }
        prmUserService.save(model.getService());
        return result.ok("");
    }

    @PostMapping(value = "batchpassword")
    @Operation(summary = "批量修改用户密码")
    public ResultVO batchPassword(@RequestBody UserDTO.BatchUserEdit model) {
        ResultVO<String> result = new ResultVO<>();
        result.setMessage("操作成功");

        List<PrmUserEntity> users = prmUserService.getListIds(model.getIds());
        String pass = EncryptionUtils.md5(model.getPassword());
        for (PrmUserEntity item : users) {
            UpdateWrapper<PrmUserEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmUserEntity::getPassword, pass)
                    .eq(PrmUserEntity::getId, item.getId());
            prmUserService.update(upset);
        }
        prmUserService.saveChange();
        return result.ok("");
    }

    @PostMapping(value = "changepassword")
    @Operation(summary = "修改密码")
    public ResultVO changePassword(@RequestBody UpdatePassword model) {
        ResultVO<Boolean> result = new ResultVO<>();
        result.setMessage("更新成功");
        Integer passwordLength = 6;

        //PrmUserEntity user = prmUserService.get(model.getUserID());
        if (StringUtils.isBlank(model.getNewPassword())) {
            throw new InkelinkException("请输入密码");
        }

        if (model.getNewPassword().length() < passwordLength) {
            throw new InkelinkException("密码最短六位");
        }
        prmUserService.setPassword(model.getUserId(), EncryptionUtils.md5(model.getNewPassword()));
        prmUserService.saveChange();
        return result.ok(true);
    }

    @PostMapping(value = "myselfchangepassword")
    @Operation(summary = "修改密码")
    public ResultVO mySelfChangePassword(@RequestBody ChangePasswordModel passwordModel) throws Exception {

        passwordModel.setNewPassword(EncryptionUtils.decryptByAesForCryptoJs(passwordModel.getNewPassword()));
        passwordModel.setOldPassword(EncryptionUtils.decryptByAesForCryptoJs(passwordModel.getOldPassword()));

        ResultVO<Boolean> result = new ResultVO<>();
        result.setMessage("更新成功");

        String userId = identityHelper.getUserId().toString();
        String oldPass = EncryptionUtils.md5(passwordModel.getOldPassword());

        PrmUserEntity user = prmUserService.getFirstByPwdUid(oldPass, userId);

        if (user == null) {
            throw new InkelinkException("原始密码不对");
        }
        if (StringUtils.isBlank(passwordModel.getNewPassword())) {
            throw new InkelinkException("请输入密码");
        }
        Integer passwordLength = 6;
        if (passwordModel.getNewPassword().length() < passwordLength) {
            throw new InkelinkException("密码最短六位");
        }
        String newPass = EncryptionUtils.md5(passwordModel.getNewPassword());
        prmUserService.setPassword(userId, newPass);
        prmUserService.saveChange();

        return result.ok(true);
    }

    @GetMapping(value = "getalldata")
    @Operation(summary = "根据参数获取用户表数据")
    public ResultVO getAllData(String searchParams) {
        ResultVO<List<PrmUserEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<ConditionDto> conditionInfos = new ArrayList<>();
        if (!StringUtils.isBlank(searchParams)) {
            conditionInfos.add(new ConditionDto("NO", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
            conditionInfos.add(new ConditionDto("EN_NAME", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
            conditionInfos.add(new ConditionDto("CN_NAME", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
        }
        return result.ok(prmUserService.getData(conditionInfos));
    }

    @GetMapping(value = "gettopdataall")
    @Operation(summary = "数据查询")
    public ResultVO getTopDataAll(String searchParams) {
        ResultVO<List<PrmUserEntity>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<ConditionDto> conditionInfos = new ArrayList<>();

        if (!StringUtils.isBlank(searchParams)) {
            conditionInfos.add(new ConditionDto("NO", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
            conditionInfos.add(new ConditionDto("EN_NAME", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
            conditionInfos.add(new ConditionDto("CN_NAME", searchParams, ConditionOper.AllLike, ConditionRelation.Or));
        }
        return result.ok(prmUserService.getTopDatas(100, conditionInfos, new ArrayList<>()));
    }

    @GetMapping(value = "getalluser")
    @Operation(summary = "下拉框-查询用户表数据")
    public ResultVO getAllUser() {
        ResultVO<List<PrmUserVO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<PrmUserVO> datas = prmUserService.getAllUser().stream().map(
                entity -> {
                    PrmUserVO et = new PrmUserVO();
                    et.setId(entity.getId());
                    et.setFullName(entity.getNickName() + "/" + entity.getUserName());
                    return et;
                }
        ).collect(Collectors.toList());
        return result.ok(datas);
    }

    @GetMapping(value = "getusertree")
    @Operation(summary = "获取用户数")
    public ResultVO getUserTree(String noteType) {
        ResultVO<List<TreeNode>> result = new ResultVO<>();
        result.setMessage("获取数据成功");

        List<PrmUserEntity> userDatas = prmUserService.getData(null);
        if (userDatas != null) {
            for (PrmUserEntity et : userDatas) {
                et.setCnGroupName(et.getGroupName());
                et.setPassword("");
            }
        }

        userDatas = userDatas.stream().filter(o -> o.getId() != null && o.getId() > 0
                        && !StringUtils.equals(o.getUserName(), Constant.SYSTEM_ADMINROLE)
                        && !StringUtils.equals(o.getUserName(), Constant.SYSTEM_MANAGER))
                .collect(Collectors.toList());

        List<String> positions = new ArrayList();
        String language = identityHelper.getLoginUser().getLanguage();
        String chLanguage = "CN";
        if (StringUtils.equals(language, chLanguage)) {
            positions = userDatas.stream().map(o ->
                            (StringUtils.isBlank(o.getCnGroupName()) ? "其他" : o.getCnGroupName())
                                    + "|" + "*" + o.getId() + "[" + o.getJobNo() + "]" + o.getCnName()).
                    distinct().sorted().collect(Collectors.toList());
        } else {
            positions = userDatas.stream().map(o ->
                            (StringUtils.isBlank(o.getEnGroupName()) ? "Other" : o.getEnGroupName())
                                    + "|" + "*" + o.getId() + "[" + o.getJobNo() + "]" + o.getEnName()).
                    distinct().sorted().collect(Collectors.toList());
        }
        List<TreeNode> rootNodes = new ArrayList<>();

        List<String> groupNames = positions.stream().map(o -> o.split("\\|")[0]).
                distinct().collect(Collectors.toList());
        for (String groupName : groupNames) {
            rootNodes.add(generateTreeNode(groupName, groupName, positions, 0));
        }
        return result.ok(rootNodes);
    }

    /**
     * 生成树节点
     */
    private TreeNode generateTreeNode(String groupName, String position, List<String> positions, int deep) {
        TreeNode node = new TreeNode();
        String str = "*";
        //叶子节点
        if (groupName.startsWith(str)) {
            node = new TreeNode();
            node.setId(groupName.substring(1, 36));
            node.setText(groupName.substring(37));
            node.setLeaf(true);
            node.setIconCls("fa fa-user-circle-o");
            Map extend = new HashMap(5);
            extend.put("groupName", "");
            node.setExtendData(extend);

        } else {
            node = new TreeNode();
            node.setId(UUIDUtils.getGuid());
            node.setText(groupName);
            node.setLeaf(false);
            node.setIconCls("fa fa-th-large");
            node.setChildren(new ArrayList());
            Map extend = new HashMap(5);
            extend.put("groupName", position);
            node.setExtendData(extend);
            //深度加1,获取下一级节点
            deep++;
            List<String> datas = positions.stream().filter(o -> o.startsWith(position + "|")).collect(Collectors.toList());
            int finalDeep = deep;
            List<String> childGroupNames = datas.stream().map(o -> o.split("\\|")[finalDeep]).
                    distinct().collect(Collectors.toList());
            for (String childGroupName : childGroupNames) {
                node.getChildren().add(generateTreeNode(childGroupName, position + "|" + childGroupName, positions, deep));
            }
        }
        return node;
    }

    @PostMapping(value = "getdata")
    @Operation(summary = "获取所有数据")
    @Override
    public ResultVO<List<PrmUserEntity>> page(@RequestBody DataDto model) {
        List<PrmUserEntity> list = prmUserService.list(model).stream().filter(u-> !u.getUserName().equals(Constant.SYSTEM_MANAGER)).collect(Collectors.toList());
        return new ResultVO<List<PrmUserEntity>>().ok(list, "获取数据成功");
    }

}