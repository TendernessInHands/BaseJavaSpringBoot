package com.mars.system.service.impl;

import com.mars.common.annotation.DataScope;
import com.mars.common.constant.Constants;
import com.mars.common.constant.UserConstants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.TreeSelect;
import com.mars.common.core.domain.entity.SysDept;
import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.core.domain.entity.vo.SysDeptChild;
import com.mars.common.exception.CustomException;
import com.mars.common.utils.LocalStringUtils;
import com.mars.common.utils.ServletUtils;
import com.mars.common.utils.SnowflakeIdWorker;
import com.mars.system.mapper.*;
import com.mars.system.service.ISysDeptService;
import com.mars.web.SysTokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author mars
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysTokenService tokenService;

    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserMapper userMapper;


    /**
     * 常量
     */
    private static final String SYSTEM_ADMIN = "system_admin";
    private static final String TYPE = "1";
    private static final String TYPE2 = "2";
    private static final int COUNT = 10000;


    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<String> tempList = new ArrayList<String>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDept dept = (SysDept) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    @Override
    public List<Integer> selectDeptListByRoleId(String roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(String deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(String deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(String deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(String deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept) {
        String deptId = LocalStringUtils.isNull(dept.getDeptId()) ? String.valueOf(-1L) : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (LocalStringUtils.isNotNull(info) && !info.getDeptId().equals(deptId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        String orgCode = "";
        SysDept info = deptMapper.selectDeptById(String.valueOf(dept.getParentId()));
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new CustomException("部门停用，不允许新增");
        }
        //机构  需要生产机构编码
        if (TYPE.equals(dept.getType())) {
            for (int i = 0; i < COUNT; i++) {
                String newOrgCode = String.valueOf((int) (Math.random() * 900 + 100));
                List<SysDept> deptList = deptMapper.selectDeptByOrgCode(newOrgCode);
                if (null == deptList || deptList.size() <= 0) {
                    orgCode = newOrgCode;
                    break;
                }
            }
            dept.setOrgCode(orgCode);
        } else {
            //部门
            orgCode = info.getOrgCode();
            dept.setOrgCode(orgCode);
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        if (LocalStringUtils.isEmpty(dept.getDeptId())) {
            dept.setDeptId(SnowflakeIdWorker.getSnowId());
        }
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(String.valueOf(dept.getParentId()));
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (LocalStringUtils.isNotNull(newParentDept) && LocalStringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            if (TYPE2.equals(dept.getType())) {
                dept.setOrgCode(newParentDept.getOrgCode());
            }
            updateDeptChildren(String.valueOf(dept.getDeptId()), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept) {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(String deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(String deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (LocalStringUtils.isNotNull(n.getParentId()) && n.getParentId().equals(t.getDeptId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    @Override
    public List<SysDept> selectDeptByOrgCode(String orgCode) {
        List<SysDept> deptList = deptMapper.selectDeptByOrgCode(orgCode);
        return deptList;
    }

    @Override
    public List<SysDept> selectDeptByOrgCodeAndIsNotOrg(String orgCode) {
        List<SysDept> deptList = deptMapper.selectDeptByOrgCodeAndIsNotOrg(orgCode);
        return deptList;
    }

    @Override
    public List<SysDept> selectDeptByOrgCodeForExport(String orgCode) {
        List<SysDept> deptList = deptMapper.selectDeptByOrgCodeForExport(orgCode);
        return deptList;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int addFirstDept(SysDept dept) {
        dept.setParentId("0");
        dept.setAncestors("0");
        List<SysDept> orgCodeList = deptMapper.selectDeptOrgCode();
        String orgCode = "";
        if (null != orgCodeList && orgCodeList.size() > 0) {
            orgCode = orgCodeList.get(0).getOrgCode();
        }
        String newOrgCode = String.valueOf(Integer.parseInt(orgCode) + 111);
        dept.setDelFlag("0");
        dept.setOrgCode(newOrgCode);

        dept.setDeptId(newOrgCode + SnowflakeIdWorker.getSnowId());
        return deptMapper.insertDept(dept);
    }

    @Override
    public List<SysDept> getAllOrg() {
        List<SysDept> deptList = deptMapper.selectAllOrg();
        return deptList;
    }

    @Override
    public SysDept selectOrgByOrgCode(String orgCode) {
        return deptMapper.selectOrgByOrgCode(orgCode);
    }

    @Override
    public List<SysDept> selectDeptListByPost(String post) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        String orgCode = "";
        if (!user.isAdmin()) {
            orgCode = tokenService.getOrgCode(ServletUtils.getRequest());
        }
        return deptMapper.selectDeptListByPost(post, orgCode);
    }

    @Override
    public AjaxResult getLoginUserDept() {
        HashMap<String, String> map = new HashMap<>(16);
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        SysDept dept = deptMapper.selectDeptById(user.getDeptId());
        map.put("deptId", dept.getDeptId());
        map.put("deptName", dept.getDeptName());
        SysDept org = deptMapper.selectOrgByOrgCode(user.getOrgCode());
        map.put("orgId", org.getDeptId());
        map.put("orgName", org.getDeptName());
        return new AjaxResult(200, "成功", map);
    }

    @Override
    public Map<String, String> getMapAllOrg() {
        List<SysDept> list = deptMapper.selectAllOrg();
        Map<String, String> map = new HashMap(16);
        for (int i = 0; i < list.size(); i++) {
            SysDept sysDept = list.get(i);
            map.put(sysDept.getOrgCode(), sysDept.getDeptName());
        }
        return map;
    }

    @Override
    public List<SysDept> getAllDept() {
        return deptMapper.getAllDept();
    }


    @Override
    public SysDept getDeptByUserId(String userId) {
        SysUser user = userMapper.selectUserById(userId);
        SysDept dept = deptMapper.selectDeptById(user.getDeptId());
        return dept;
    }

    @Override
    public List<SysDept> getDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    public List<SysDeptChild> treeList() {
        List<SysDeptChild> returnList = new ArrayList<>();
        SysDept paramDept = new SysDept();
        paramDept.setDelFlag(Constants.UNDELETE);
        paramDept.setType(Constants.STRING_1);
        paramDept.setParentId(Constants.PARENT_ID);
        List<SysDept> parentDeptList = deptMapper.selectDeptList(paramDept);
        paramDept.setType(Constants.STRING_2);
        paramDept.setParentId(null);
        List<SysDept> deptList = deptMapper.selectDeptList(paramDept);
        if (LocalStringUtils.isNotEmpty(parentDeptList)) {
            for (SysDept dept : parentDeptList) {
                SysDeptChild deptChild = new SysDeptChild();
                BeanUtils.copyProperties(dept, deptChild);
                returnList.add(deptChild);
            }
        }
        if (LocalStringUtils.isNotEmpty(deptList)) {
            Map<String, List<SysDept>> deptMap = deptList.stream().collect(Collectors.groupingBy(SysDept::getParentId));
            if (LocalStringUtils.isNotEmpty(returnList)) {
                for (SysDeptChild deptChild : returnList) {
                    if (LocalStringUtils.isNotEmpty(deptMap.get(deptChild.getDeptId()))) {
                        List<SysDept> childDeptList = deptMap.get(deptChild.getDeptId());
                        ArrayList<SysDeptChild> childList = new ArrayList<>();
                        for (SysDept dept : childDeptList) {
                            SysDeptChild child = new SysDeptChild();
                            BeanUtils.copyProperties(dept, child);
                            childList.add(child);
                        }
                        deptChild.setChildList(childList);
                    }
                }
            }
        }
        return returnList;
    }
}
