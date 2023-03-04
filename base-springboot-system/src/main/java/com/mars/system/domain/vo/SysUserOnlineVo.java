package com.mars.system.domain.vo;

import com.mars.common.core.domain.entity.SysMenu;
import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.system.domain.SysUserOnline;

import java.util.List;

/**
 * 在线用户
 * @author mars
 */
public class SysUserOnlineVo {
    private List<SysRole> sysRole;

    private List<SysMenu> sysMenu;

    private SysUserOnline sysUserOnline;

    private SysUser sysUser;

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

    public List<SysMenu> getSysMenu() {
        return sysMenu;
    }

    public void setSysMenu(List<SysMenu> sysMenu) {
        this.sysMenu = sysMenu;
    }

    @Override
    public String toString() {
        return "SysUserOnlineVo{" +
                "sysRole=" + sysRole +
                ", sysMenu=" + sysMenu +
                ", sysUserOnline=" + sysUserOnline +
                '}';
    }
}
