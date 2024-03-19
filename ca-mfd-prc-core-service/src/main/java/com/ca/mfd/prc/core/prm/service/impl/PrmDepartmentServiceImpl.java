package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.comparer.FlatPositionComparer;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.PrmDepartMentNode;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmDepartmentMapper;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentJoinUserService;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmDepartmentServiceImpl extends AbstractCrudServiceImpl<IPrmDepartmentMapper, PrmDepartmentEntity> implements IPrmDepartmentService {

    @Autowired
    private IPrmDepartmentJoinUserService prmDepartmentJoinUserService;

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        for (Serializable item : idList) {
            QueryWrapper<PrmDepartmentJoinUserEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, item);
            PrmDepartmentJoinUserEntity model = prmDepartmentJoinUserService.getTopDatas(1, qry).stream().findFirst().orElse(null);

            if (model != null) {
                QueryWrapper<PrmDepartmentEntity> qryDepart = new QueryWrapper<>();
                qryDepart.lambda().eq(PrmDepartmentEntity::getId, item);
                PrmDepartmentEntity demodel = selectList(qryDepart).stream().findFirst().orElse(null);
                throw new InkelinkException("部门" + demodel.getDepartmentName() + "存在用户不能删除!");
            }

        }
        UpdateWrapper<PrmDepartmentJoinUserEntity> upset = new UpdateWrapper<>();
        upset.lambda().in(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, idList);
        prmDepartmentJoinUserService.delete(upset);
    }

    /**
     * 获取可用的部门
     *
     * @param orgName 名字
     * @return 部门集合
     */
    public List<PrmDepartmentEntity> getPrmDepartment(String orgName) {
        QueryWrapper<PrmDepartmentEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmDepartmentEntity> qryLamd = qry.lambda();
        qryLamd.orderByAsc(PrmDepartmentEntity::getSeqNumber);
        if (!StringUtils.isBlank(orgName)) {
            qryLamd.eq(PrmDepartmentEntity::getOrganizationName, orgName);
        }
        return selectList(qry);
    }

    /**
     * 获取下拉部门
     *
     * @param orgName 名字
     * @return 部门集合
     */
    @Override
    public List<ComboInfoDTO> getDropDownPrmDepartment(String orgName) {
        try {
            List<PrmDepartmentEntity> departments = getPrmDepartment(orgName);
            List<ComboInfoDTO> nodes = new ArrayList<>();
            //顶级部门
            List<PrmDepartmentEntity> rootdepart = departments.stream().
                    filter(o -> o.getPosition().split("\\.").length == 1)
                    .collect(Collectors.toList());
            for (PrmDepartmentEntity department : rootdepart) {
                getDownDepartNode(departments, department, nodes, UUIDUtils.getEmpty());
            }
            //throw new Exception("sss");
            return nodes;
        } catch (Exception e) {
            throw e;
        }
    }

    private void getDownDepartNode(List<PrmDepartmentEntity> depatrs, PrmDepartmentEntity depatr, List<ComboInfoDTO> nodes, Serializable id) {
        ComboInfoDTO nd = new ComboInfoDTO();
        nd.setValue(depatr.getId().toString());
        nd.setText(StringUtils.rightPad("|", depatr.getPosition().split("\\.").length, '_') + depatr.getDepartmentName());
        nodes.add(nd);
        List<PrmDepartmentEntity> childMenuItems = depatrs.stream()
                .filter(o -> o.getPosition().split("\\.").length
                        == (depatr.getPosition().split("\\.").length + 1)
                        && o.getPosition().startsWith(depatr.getPosition() + ".")).collect(Collectors.toList());
        if (childMenuItems.size() > 0) {
            for (PrmDepartmentEntity childMenuItem : childMenuItems) {
                getDownDepartNode(depatrs, childMenuItem, nodes, childMenuItem.getId());
            }
        }
    }

    /**
     * 获取部门树
     *
     * @param orgName 名字
     * @return 部门集合
     */
    @Override
    public List<PrmDepartMentNode> getPrmDepartmentTree(String orgName) {
        try {
            List<PrmDepartmentEntity> departments = getPrmDepartment(orgName);
            List<PrmDepartMentNode> nodes = new ArrayList<>();
            //顶级部门
            List<PrmDepartmentEntity> rootdepart = departments.stream().
                    filter(o -> o.getPosition().split("\\.").length == 1).collect(Collectors.toList());
            for (PrmDepartmentEntity department : rootdepart) {
                PrmDepartMentNode node = getDepartNode(departments, department, UUIDUtils.getEmpty());
                nodes.add(node);
            }
            //throw new Exception("sss");
            return nodes;
        } catch (Exception e) {

            throw e;
        }
    }

    private PrmDepartMentNode getDepartNode(List<PrmDepartmentEntity> depatrs, PrmDepartmentEntity depatr, Serializable id) {
        PrmDepartMentNode node = new PrmDepartMentNode();
        node.setId(depatr.getId().toString());
        node.setDepartmentCode(depatr.getDepartmentCode());
        node.setIcon("&#xe63f;");
        node.setSelect(false);
        node.setIsEnable(depatr.getIsEnable());
        node.setParentId(id.toString());
        node.setOrganizationName(depatr.getOrganizationName());
        node.setDepartmentName(depatr.getDepartmentName());
        node.setRemark(depatr.getRemark());
        node.setSeqNumber(depatr.getSeqNumber());
        if (node.getChildren() == null) {
            node.setChildren(new ArrayList<>());
        }

        List<PrmDepartmentEntity> childMenuItems = depatrs.stream().
                filter(o -> o.getPosition().split("\\.").length == (depatr.getPosition().split("\\.").length + 1)
                        && o.getPosition().startsWith(depatr.getPosition() + ".")).collect(Collectors.toList());
        if (childMenuItems.size() > 0) {
            for (PrmDepartmentEntity childMenuItem : childMenuItems) {
                PrmDepartMentNode childNode = getDepartNode(depatrs, childMenuItem, node.getId());
                if (childNode != null) {
                    node.getChildren().add(childNode);
                }
            }
        }
        return node;
    }

    /**
     * 保存部门数据
     *
     * @param models  部门集合
     * @param orgName 组织机构
     */
    @Override
    public void save(List<PrmDepartmentEntity> models, String orgName) {
        List<PrmDepartmentEntity> datas = new ArrayList<>();

        //生成Position
        generationPosition(models, "", datas);

        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("ORGANIZATION_NAME", orgName, ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        List<PrmDepartmentEntity> existDatas = getData(conditions).stream().sorted(Comparator
                        .comparing(PrmDepartmentEntity::getPosition, new FlatPositionComparer()))
                .collect(Collectors.toList());
        //需要新增的数据
        List<PrmDepartmentEntity> addDatas = datas.stream().filter(o ->
                        !existDatas.stream().anyMatch(p -> Objects.equals(p.getId(), o.getId())))
                .collect(Collectors.toList());
        for (PrmDepartmentEntity addData : addDatas) {
            insert(addData);
        }
        //需要更新的数据
        List<PrmDepartmentEntity> updateDatas = datas.stream().filter(o ->
                existDatas.stream().anyMatch(p -> Objects.equals(p.getId(), o.getId()))).collect(Collectors.toList());
        for (PrmDepartmentEntity updateData : updateDatas) {
            PrmDepartmentEntity existData = existDatas.stream().filter(o -> Objects.equals(o.getId(), updateData.getId()))
                    .findFirst().orElse(null);
            existData.setDepartmentName(updateData.getDepartmentName());
            existData.setDepartmentCode(updateData.getDepartmentCode());
            existData.setIsEnable(updateData.getIsEnable());
            existData.setPosition(updateData.getPosition());
            existData.setRemark(updateData.getRemark());
            existData.setOrganizationName(updateData.getOrganizationName());
            update(existData);
        }
        //需要删除的数据
        List<PrmDepartmentEntity> delDatas = existDatas.stream().filter(o ->
                        !datas.stream().anyMatch(p -> Objects.equals(p.getId(), o.getId())))
                .collect(Collectors.toList());
        if (delDatas.size() > 0) {
            List<Serializable> listGuid = delDatas.stream().map(o -> o.getId()).collect(Collectors.toList());
            delete(listGuid.toArray(new Serializable[0]));
        }
    }

    private void generationPosition(List<PrmDepartmentEntity> models, String parentPosition, List<PrmDepartmentEntity> datas) {
        parentPosition = StringUtils.isBlank(parentPosition) ? "" : parentPosition + ".";
        int i = 1;
        for (PrmDepartmentEntity model : models) {
            model.setPosition(parentPosition + i);
            i++;
            if (model.getChildren() != null && model.getChildren().size() > 0) {
                generationPosition(model.getChildren(), model.getPosition(), datas);
            }
            datas.add(model);
        }
    }
}