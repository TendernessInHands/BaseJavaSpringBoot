package com.mars.system.domain.vo;

import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.system.domain.SysUserOnline;

import java.util.List;
import java.util.Map;

/**
 * 新在线用户VO
 * @author mars
 */
public class NewSysUserOnlineVo {
    private List<SysRole> sysRole;

    private SysUserOnline sysUserOnline;

    private SysUser sysUser;

    private List<Map<String, Object>> menuList;

    public List<Map<String, Object>> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Map<String, Object>> menuList) {
        this.menuList = menuList;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public SysUserOnline getSysUserOnline() {
        return sysUserOnline;
    }

    public void setSysUserOnline(SysUserOnline sysUserOnline) {
        this.sysUserOnline = sysUserOnline;
    }

    public List<SysRole> getSysRole() {
        return sysRole;
    }

    public void setSysRole(List<SysRole> sysRole) {
        this.sysRole = sysRole;
    }

    @Override
    public String toString() {
        return "SysUserOnlineVo{" +
                "sysRole=" + sysRole +
                ", sysUserOnline=" + sysUserOnline +
                '}';
    }
}
