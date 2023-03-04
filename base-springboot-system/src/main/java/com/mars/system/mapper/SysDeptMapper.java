package com.mars.system.mapper;

import com.mars.common.core.domain.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author mars
 */
public interface SysDeptMapper {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(@Param("roleId") String roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 根据部门名称获取部门信息
     * @param deptName
     * @return
     */
    SysDept selectDeptByName(String deptName);

    /**
     * 获取所有部门
     * @return
     */
    List<SysDept> selectAllDeptList();

    /**
     * 根据机构ID获取部门列表
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCode(String orgCode);

    /**
     * 获取所有机构编码
     * @return
     */
    List<SysDept> selectDeptOrgCode();

    /**
     * 根据机构编码和父级ID获取所有部门
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCodeAndParentId(String orgCode);

    /**
     * 获取部门列表
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCodeForExport(String orgCode);

    /**
     * 根据机构编码获取机构信息
     * @param orgCode
     * @return
     */
    SysDept selectOrgByOrgCode(String orgCode);

    /**
     * 获取所有机构列表
     * @return
     */
    List<SysDept> selectAllOrg();

    /**
     * 根据岗位查询部门管理数据
     *
     * @param post 岗位信息
     * @param orgCode 机构
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptListByPost(@Param("post") String post,@Param("orgCode") String orgCode);

    /**
     * 根据机构编码获取所有部门
     * @param orgCode
     * @return
     */
    List<SysDept> selectDeptByOrgCodeAndIsNotOrg(String orgCode);

    /**
     * 获取所有部门
     * @return
     */
    List<SysDept> getAllDept();
}
