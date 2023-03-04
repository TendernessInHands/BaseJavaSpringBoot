package com.mars.system.mapper;

import java.util.List;
//
//import com.mars.system.domain.SysMenu;
//import com.mars.system.domain.SysRoles;
//import com.mars.system.domain.vo.SysUserOnlineVo;
import com.mars.common.core.domain.entity.SysMenu;
import com.mars.common.core.domain.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import com.mars.system.domain.SysUserRole;

/**
 * 用户与角色关联表 数据层
 *
 * @author mars
 */
public interface SysUserRoleMapper {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserRoleByUserId(String userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserRole(String[] ids);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(String roleId);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 根据Uid获取用户角色
     * @param uid
     * @return
     */
    List<SysRole> selectUserRole(String uid);

    /**
     * 根据角色ID获取菜单
     * @param rid
     * @return
     */
    List<SysMenu> selectRoleMenu(String rid);

    /**
     * 新根据角色获取菜单信息
     * @param rid
     * @return
     */
    List<SysMenu> newSelectRoleParentMenu(String rid);

    /**
     * 根据角色
     * @param roleId
     * @param parentId
     * @return
     */
    List<SysMenu> newSelectRoleSonMenu(@Param("roleId") String roleId,@Param("parentId") String parentId);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
