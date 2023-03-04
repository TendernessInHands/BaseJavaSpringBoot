package com.mars.system.service.impl;


import com.mars.common.core.domain.entity.SysMenu;
import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.model.LoginUser;
import com.mars.common.utils.LocalStringUtils;
import com.mars.system.domain.SysUserOnline;
import com.mars.system.domain.vo.NewSysUserOnlineVo;
import com.mars.system.domain.vo.SysUserOnlineVo;
import com.mars.system.mapper.SysUserRoleMapper;
import com.mars.system.service.ISysUserOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 在线用户 服务层处理
 *
 * @author mars
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (LocalStringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
        if (LocalStringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (LocalStringUtils.equals(ipaddr, user.getIpaddr()) && LocalStringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (LocalStringUtils.isNull(user) || LocalStringUtils.isNull(user.getUser())) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        if (LocalStringUtils.isNotNull(user.getUser().getDept())) {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }
        return sysUserOnline;
    }

    @Override
    public SysUserOnlineVo selectUserRole(String uid) {
        SysUserOnlineVo sysUserOnlineVo = new SysUserOnlineVo();
        List<SysRole> sysRoleList = sysUserRoleMapper.selectUserRole(uid);
        sysUserOnlineVo.setSysRole(sysRoleList);
        for (SysRole sysRole : sysRoleList) {
            List<SysMenu> sysMenus = sysUserRoleMapper.selectRoleMenu(sysRole.getRoleId());
            sysUserOnlineVo.setSysMenu(sysMenus);
        }
        return sysUserOnlineVo;
    }

    @Override
    public NewSysUserOnlineVo newSelectUserRole(String uid) {
        NewSysUserOnlineVo sysUserOnlineVo = new NewSysUserOnlineVo();
        List<SysRole> sysRoleList = sysUserRoleMapper.selectUserRole(uid);
        sysUserOnlineVo.setSysRole(sysRoleList);
//        ArrayList<Map<String, Object>> menuList = new ArrayList<>();
//        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>(16);
//        for (SysRole sysRole : sysRoleList) {
//            //获取此角色拥有的所有父级菜单
//            List<SysMenu> parentMenuList = sysUserRoleMapper.newSelectRoleParentMenu(sysRole.getRoleId());
//            if (null != parentMenuList && parentMenuList.size() > 0){
//                for (SysMenu parentMenu : parentMenuList) {
//                    HashMap<String, Object> menuMap = new HashMap<>(16);
//                    menuMap.put("menuParentTitle",parentMenu.getMenuName());
//                    HashMap<String, Object> parentMenuMap = new HashMap<>(16);
//                    ArrayList<Map<String, Object>> menuResultList = new ArrayList<>();
//                    parentMenuMap.put("menuName",parentMenu.getMenuName());
//                    parentMenuMap.put("menuIcon",parentMenu.getMenuIcon());
//                    parentMenuMap.put("routerUrl",parentMenu.getPath());
//                    menuResultList.add(parentMenuMap);
//                    //获取父级菜单下的所有子菜单
//                    List<SysMenu> sonMenuList = sysUserRoleMapper.newSelectRoleSonMenu(sysRole.getRoleId(),parentMenu.getMenuId());
//                    if (null != sonMenuList && sonMenuList.size() > 0){
//                        HashMap<String, Object> sonMenuMap = new HashMap<>(16);
//                        for (SysMenu sonMenu : sonMenuList) {
//                            sonMenuMap.put("menuName",sonMenu.getMenuName());
//                            sonMenuMap.put("menuIcon",sonMenu.getMenuIcon());
//                            sonMenuMap.put("routerUrl",sonMenu.getPath());
//                            menuResultList.add(sonMenuMap);
//                        }
//                    }
//                    menuMap.put("menuList",menuResultList);
//                    menuList.add(menuMap);
//                }
//            }
//        }
//        sysUserOnlineVo.setMenuList(menuList);
        return sysUserOnlineVo;
    }

}
