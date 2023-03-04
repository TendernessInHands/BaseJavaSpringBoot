package com.mars.system.service;

import com.mars.common.core.domain.model.LoginUser;
import com.mars.system.domain.SysUserOnline;
import com.mars.system.domain.vo.NewSysUserOnlineVo;
import com.mars.system.domain.vo.SysUserOnlineVo;
//import com.mars.system.domain.vo.SysUserOnlineVo;


/**
 * 在线用户 服务层
 *
 * @author mars
 */
public interface ISysUserOnlineService {
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr   登录地址
     * @param userName 用户名称
     * @param user     用户信息
     * @return 在线用户信息
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);


    /**
     * 根据Uid获取用户角色
     * @param uid
     * @return
     */
    SysUserOnlineVo selectUserRole(String uid);

    /**
     * 根据Uid获取用户角色
     * @param uid
     * @return
     */
    NewSysUserOnlineVo newSelectUserRole(String uid);

}
