package com.mars.system.service;

import java.util.List;
import java.util.Map;

import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.TreeSelect;
import com.mars.common.core.domain.entity.SysDept;
import com.mars.common.core.domain.entity.vo.SysDeptChild;

/**
 * 部门管理 服务层
 *
 * @author mars
 */
public interface ISysDeptService {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 构建前端所需要树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(String roleId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 根据机构ID获取部门列表
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCode(String orgCode);

    /**
     * 新增部门信息
     * @param dept
     * @return
     */
    int addFirstDept(SysDept dept);

    /**
     * 获取部门列表
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCodeForExport(String orgCode);

    /**
     * 获取所有机构列表
     * @return
     */
    List<SysDept> getAllOrg();

    /**
     *  根据机构编码获取机构信息
     * @param orgCode
     * @return
     */
    SysDept selectOrgByOrgCode(String orgCode);

    /**
     * 根据岗位查询部门管理数据
     *
     * @param post 岗位信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptListByPost(String post);

    /**
     * 获取登录用户部门
     * @return
     */
    AjaxResult getLoginUserDept();

    /**
     *  根据机构编码获取所有部门
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCodeAndIsNotOrg(String orgCode);

    /**
     * 获取所有机构列表
     * @return
     */
    Map<String,String> getMapAllOrg();

    /**
     * 获取所有部门列表
     * @return
     */
    List<SysDept> getAllDept();

    /**
     * 根据用户ID获取部门信息
     * @param userId
     * @return
     */
    SysDept getDeptByUserId(String userId);

    /**
     * 首页获取组织机构列表
     * @param dept
     * @return
     */
    List<SysDept> getDeptList(SysDept dept);

    /**
     *  机构部门树
     * @return
     */
    List<SysDeptChild> treeList();

}
