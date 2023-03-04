package com.mars.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mars.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author mars
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     *  获取客户列表
     * @param sysUser
     * @return
     */
    public List<SysUser> selectCustomerUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(String userId);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectCustomerUserById(String userId);


    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(String userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(String[] userIds);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phoneNumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phoneNumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);



    /**
     *查询用户详情
     * @param postId
     * @return
     */
    List<SysUser> selectUserPostByPostId(Long postId);

    /**
     *查询用户昵称
     * @param deptId
     * @return
     */
    List<SysUser> selectUserListByDeptId(String deptId);

    /**
     *查询用户详情
     * @param paramUser
     * @return
     */
    List<SysUser> selectUserByUserNameAndPassword(SysUser paramUser);

    /**
     *查询用户列表
     * @param roleKey
     * @return
     */
    List<SysUser> selectUserListByPostCode(String roleKey);

    /**
     * 查询用户详情
     * @param postCode
     * @param deptId
     * @param orgCode
     * @return
     */
    List<SysUser> selectUserNameByPostCode(@Param("postCode") String postCode, @Param("deptId") String deptId, @Param("orgCode") String orgCode);

    /**
     *查询所有部门领导
     * @param orgCode
     * @param deptId
     * @return
     */
    List<SysUser> getAllDeptLeader(@Param("orgCode") String orgCode, @Param("deptId") String deptId);

    /**
     *通过部门编码和部门岗位获取所有用户
     * @param post
     * @param deptId
     * @param orgCode
     * @return
     */
    List<SysUser> getUserByPost(@Param("post") String post, @Param("deptId") String deptId, @Param("orgCode") String orgCode);

    /**
     *通过部门编码和部门id获取所有用户
     * @param post
     * @param orgCode
     * @param deptId
     * @return
     */
    List<SysUser> getUserByPostForOrg(@Param("post") String post, @Param("orgCode") String orgCode, @Param("deptId") String deptId);

    /**
     *通过角色编码和机构编码获取用户昵称
     * @return
     */
    List<SysUser> getAllUser();

    /**
     *  根据角色编码获取所有角色
     * @param roleCode
     * @return
     */
    List<SysUser> getUserListByRoleCode(@Param("roleCode") String roleCode);
}
