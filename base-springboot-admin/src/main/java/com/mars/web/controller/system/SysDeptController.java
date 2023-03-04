package com.mars.web.controller.system;

import com.mars.common.annotation.Log;
import com.mars.common.constant.UserConstants;
import com.mars.common.core.controller.BaseController;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysDept;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.core.domain.entity.vo.SysDeptChild;
import com.mars.common.enums.BusinessType;
import com.mars.common.utils.*;
import com.mars.framework.web.service.TokenService;
import com.mars.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 * @author mars
 */
@RestController
@RequestMapping("/system/dept")
@Api("部门管理")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) String deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().equals(deptId) || ArrayUtils.contains(LocalStringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable String deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
//        dept.setType(Constants.TYPE_1);
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") String roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getNickname());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        SysDept paramDept = new SysDept();
        paramDept.setDeptName(dept.getDeptName());
        paramDept.setOrgCode(dept.getOrgCode());
        List<SysDept> deptList = deptService.selectDeptList(paramDept);
        if (null != deptList && deptList.size() > 1) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        if (dept.getParentId().equals(dept.getDeptId())) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (LocalStringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getNickname());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable String deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/addFirstDept")
    @ApiOperation("新增一级机构")
    public AjaxResult addFirstDept(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getNickname());
        return toAjax(deptService.addFirstDept(dept));
    }

    @GetMapping("/getAllOrg")
    @ApiOperation("新增一级机构")
    public AjaxResult getAllOrg() {
        List<SysDept> deptList = deptService.getAllOrg();
        return new AjaxResult(200, "查询成功", deptList);
    }

    @GetMapping("/getDeptByOrgCode")
    @ApiOperation("通过机构编码获取机构")
    public AjaxResult getDeptByOrgCode() {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        return new AjaxResult(200, "成功", deptService.selectDeptByOrgCode(user.getOrgCode()));
    }

    /**
     * 根据岗位获取部门下拉树列表
     */
    @GetMapping("/treeselectByPost")
    @ApiOperation("根据岗位获取部门下拉树列表")
    public AjaxResult treeselectByPost(@RequestParam String post) {
        List<SysDept> depts = deptService.selectDeptListByPost(post);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }


    @GetMapping("/getLoginUserDept")
    @ApiOperation("获取当前登录用户部门和机构")
    public AjaxResult getLoginUserDept() {
        return deptService.getLoginUserDept();
    }

    @GetMapping("/getLoginUserDeptAndIsNotOrg")
    @ApiOperation("获取当前登录用户部门和机构")
    public AjaxResult getLoginUserDeptAndIsNotOrg() {
        List<SysDept> deptList = deptService.selectDeptByOrgCodeAndIsNotOrg(tokenService.getOrgCode(ServletUtils.getRequest()));
        return new AjaxResult(200, "查询成功", deptList);
    }

    /**
     * 根据用户ID获取用户所属部门信息
     */
    @GetMapping("/getDeptByUserId")
    public AjaxResult getDeptByUserId(@RequestParam String userId) {
        SysDept dept = deptService.getDeptByUserId(userId);
        return new AjaxResult(200, "成功", dept);
    }

    @GetMapping("/treeList")
    public AjaxResult treeList() {
        List<SysDeptChild> treeList = deptService.treeList();
        return new AjaxResult(200, "成功", treeList);
    }
}
