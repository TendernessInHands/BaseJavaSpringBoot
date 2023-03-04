package com.mars.framework.web.service;

import com.mars.common.core.domain.entity.SysUser;
import com.mars.system.service.ISysMenuService;
import com.mars.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 用户权限处理
 *
 * @author mars
 */
@Component
public class SysPermissionService {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user, String isApp) {
        Set<String> perms = new TreeSet<String>();
        // 管理员拥有所有权限
        String one = "1";
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            if (!one.equals(isApp) || null == isApp) {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            } else {
                perms.addAll(menuService.selectMenuPermsByUserIdForApp(user.getUserId()));
            }
        }
        return perms;
    }
}
