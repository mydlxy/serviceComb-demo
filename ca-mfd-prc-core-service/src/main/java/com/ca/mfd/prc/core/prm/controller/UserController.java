package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.dto.UserView;
import com.ca.mfd.prc.core.prm.dto.UserViewVb;
import com.ca.mfd.prc.core.prm.dto.UserViewVc;
import com.ca.mfd.prc.core.prm.dto.UserViewVd;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.prm.utils.LoginLockVerificationUtils;
import com.ca.mfd.prc.core.prm.service.ISessionService;
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

import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户
 *
 * @author inkelink ${email}
 * @date 2023-07-24
 */
@RestController
@RequestMapping("member/ucenter/user")
@Tag(name = "用户")
public class UserController extends BaseApiController {

    @Autowired
    private IPrmUserService prmUserService;
    @Autowired
    private ISessionService sessionService;

    @GetMapping(value = "getusertoken")
    @Operation(summary = "获取用户授权信息")
    public ResultVO getUserToken(String sessionGuid) {
        LocalSession data = sessionService.getLocalSession(sessionGuid);
        return new ResultVO<LocalSession>().ok(data, "获取数据成功");
    }

    @PostMapping(value = "delete")
    @Operation(summary = "删除选中用户")
    public ResultVO delete(@RequestBody IdsModel model) {
        prmUserService.delete(model.getIds());
        prmUserService.saveChange();

        return new ResultVO<String>().ok("", "保存数据成功");
    }

    @GetMapping(value = "name")
    @Operation(summary = "下拉框-获取用户列表")
    public ResultVO name() {
        //不要显示超级管理员
        List<PrmUserEntity> dbList = prmUserService.getAllNomallUsers();
        List<UserView> list = dbList.stream().map(c -> {
            UserView et = new UserView();
            et.setId(c.getId().toString());
            et.setName(c.getNickName() + "/" + c.getUserName() + "/" + c.getJobNo());
            et.setJobNo(c.getJobNo());
            return et;
        }).collect(Collectors.toList());

        return new ResultVO<List<UserView>>().ok(list, "获取数据成功");
    }

    @GetMapping(value = "select")
    @Operation(summary = "根据用户类型查询用户列表")
    public ResultVO select(@RequestParam(value = "userType", required = false) Integer userType) {
        if (userType == null) {
            userType = 0;
        }
        //不要显示超级管理员
        List<PrmUserEntity> dbList = prmUserService.getAllNomallUsers();
        if (userType > 0) {
            Integer finalUserType = userType;
            dbList = dbList.stream().filter(w -> finalUserType.equals(w.getUserType()))
                    .collect(Collectors.toList());
        }
        List<UserViewVb> list = dbList.stream().map(c -> {
            UserViewVb et = new UserViewVb();
            et.setId(c.getId().toString());
            et.setUserName(c.getUserName());
            et.setCode(c.getJobNo());
            et.setNickName(c.getNickName());
            et.setEnName(c.getEnName());
            et.setCnName(c.getCnName());
            return et;
        }).collect(Collectors.toList());

        return new ResultVO<List<UserViewVb>>().ok(list, "获取数据成功");
    }

    @GetMapping(value = "quary")
    @Operation(summary = "根据参数获取下拉框用户列表")
    public ResultVO quary(String quary) {
        if (StringUtils.isBlank(quary)) {
            quary = "";
        }
        quary = quary.split("/")[0];
        List<PrmUserEntity> list = prmUserService.getListLa(quary);
        List<UserViewVc> data = list.stream().map(c -> {
            UserViewVc et = new UserViewVc();
            et.setId(c.getId().toString());
            et.setName(c.getNickName() + "/" + c.getUserName());
            et.setPhone(c.getPhone());
            et.setEmail(c.getEmail());
            return et;
        }).collect(Collectors.toList());
        return new ResultVO<List<UserViewVc>>().ok(data, "获取数据成功");
    }

    @GetMapping(value = "unlockuserlogin")
    @Operation(summary = "接触登陆用用户锁定")
    public ResultVO unLockUserLogin(String userName) {
        LoginLockVerificationUtils.verificationisLock(userName);
        return new ResultVO<String>().ok("", "解除锁定成功");
    }

    @GetMapping(value = "getuserquary")
    @Operation(summary = "查询用户列表")
    public ResultVO getUserQuary(@RequestParam(value = "quary", required = false) String quary
            , @RequestParam(value = "size", required = false) Integer size) {
        if (StringUtils.isBlank(quary)) {
            quary = "";
        }
        quary = quary.split("/")[0];
        Integer limit = size == null ? 10 : size;
        List<PrmUserEntity> list = prmUserService.getListLa(quary);
        List<UserViewVd> data = list.stream().map(c -> {
            UserViewVd et = new UserViewVd();
            et.setId(c.getId().toString());
            et.setName(c.getNickName() + "/" + c.getUserName());
            et.setNo(c.getJobNo());
            et.setNickName(c.getNickName());
            et.setUserName(c.getUserName());

            et.setPhone(c.getPhone());
            et.setEmail(c.getEmail());
            return et;
        }).limit(limit).collect(Collectors.toList());
        return new ResultVO<List<UserViewVd>>().ok(data, "获取数据成功");
    }

    @GetMapping(value = "allquary")
    @Operation(summary = "查询所有用户列表")
    public ResultVO allQuary() {
        List<PrmUserEntity> dbList = prmUserService.getListLaOrder();
        List<UserViewVc> data = dbList.stream().map(c -> {
            UserViewVc et = new UserViewVc();
            et.setId(c.getId().toString());
            et.setName(c.getNickName() + "/" + c.getUserName());
            et.setPhone(c.getPhone());
            et.setEmail(c.getEmail());
            return et;
        }).collect(Collectors.toList());
        return new ResultVO<List<UserViewVc>>().ok(data, "获取数据成功");
    }

}