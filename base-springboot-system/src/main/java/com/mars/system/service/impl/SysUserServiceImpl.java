package com.mars.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.common.annotation.DataScope;
import com.mars.common.constant.Constants;
import com.mars.common.constant.UserConstants;
import com.mars.common.core.domain.AjaxResult;
import com.mars.common.core.domain.entity.SysDept;
import com.mars.common.core.domain.entity.SysRole;
import com.mars.common.core.domain.entity.SysUser;
import com.mars.common.exception.CustomException;
import com.mars.common.utils.*;
import com.mars.system.domain.SysPost;
import com.mars.system.domain.SysUserPost;
import com.mars.system.domain.SysUserRole;
import com.mars.system.domain.vo.CustomerVO;
import com.mars.system.mapper.*;
import com.mars.system.service.ISysConfigService;
import com.mars.system.service.ISysUserService;
import com.mars.web.SysTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户 业务层处理
 *
 * @author mars
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysTokenService tokenService;

    public static final String SYS_ADMIN = "sysAdmin";

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        SysUser nowLoginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        if (!nowLoginUser.isAdmin() && !SYS_ADMIN.equals(nowLoginUser.getUserName())) {
            user.setOrgCode(tokenService.getOrgCode(ServletUtils.getRequest()));
        }
        return userMapper.selectUserList(user);
    }

    @Override
    public List<SysUser> selectCustomerUserList(SysUser user) {
        SysUser nowLoginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        if (!nowLoginUser.isAdmin() && !SYS_ADMIN.equals(nowLoginUser.getUserName())) {
            user.setOrgCode(tokenService.getOrgCode(ServletUtils.getRequest()));
        }
        user.setUserType(Constants.STRING_2);
        return userMapper.selectCustomerUserList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(String userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID查询客户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectCustomerUserById(String userId) {
        return userMapper.selectCustomerUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (LocalStringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (LocalStringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        String userId = LocalStringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhoneNumber());
        if (LocalStringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        String userId = LocalStringUtils.isNull(user.getUserId()) ? "-1" : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (LocalStringUtils.isNotNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        if (LocalStringUtils.isNotNull(user.getUserId()) && user.isAdmin() && !loginUser.getUserId().equals(user.getUserId())) {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        try {
            //新增机构信息
            SysDept dept = sysDeptMapper.selectDeptById(user.getDeptId());
            //如果是机构使用本机构
            user.setOrgCode(dept.getOrgCode());
            user.setUserId(SnowflakeIdWorker.getSnowId());
            user.setUserType(UserConstants.USER_TYPE_1);
            // 新增用户信息
            int rows = userMapper.insertUser(user);
            // 新增用户岗位关联
            insertUserPost(user);
            // 新增用户与角色管理
            insertUserRole(user);

            return rows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user) {
        String userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        //新增机构信息
        SysDept dept = sysDeptMapper.selectDeptById(user.getDeptId());
        if (!Constants.STRING_1.equals(dept.getParentId())) {
            SysDept org = sysDeptMapper.selectDeptById(dept.getParentId());
            if (null != org) {
                user.setOrgCode(org.getOrgCode());
            } else {
                user.setOrgCode("0");
            }
        } else {
            user.setOrgCode(dept.getOrgCode());
        }
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 解锁
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserLock(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        try {
            return userMapper.updateUserAvatar(userName, avatar) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        String[] roles = user.getRoleIds();
        if (LocalStringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (String roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        String[] posts = user.getPostIds();
        if (LocalStringUtils.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (String postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(String userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String[] userIds) {
        for (String userId : userIds) {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
        if (LocalStringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList) {
            try {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (LocalStringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                LOGGER.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "数据已全部导入成功！共 " + successNum + " 条");
        }
        return successMsg.toString();
    }


    @Override
    public AjaxResult updateUserOrgCode() {
        SysUser user = null;
        try {
            //获取所有用户
            List<SysUser> userList = userMapper.selectUserList(new SysUser());
            if (null != userList && userList.size() > 0) {
                for (int i = 0; i < userList.size(); i++) {
                    user = userList.get(i);
                    SysDept dept = sysDeptMapper.selectDeptById(user.getDeptId());
                    if (null != dept && !"".equals(dept.getOrgCode())) {
                        //获取父类机构
                        SysDept parentDept = sysDeptMapper.selectDeptById(dept.getParentId());
                        user.setOrgCode(parentDept.getOrgCode());
                        userMapper.updateUser(user);
                    }
                }
            }
            return new AjaxResult(200, "111");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AjaxResult(500, "111");
    }

    @Override
    public AjaxResult getDeptUserList() {
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<SysUser> userList = userMapper.selectUserListByDeptId(loginUser.getDeptId());
        return new AjaxResult(200, "成功", userList);
    }

    @Override
    public List<Map<String, String>> selectAllUserByPostCode(String postCode, String deptId) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<SysUser> userList = userMapper.selectUserNameByPostCode(postCode, deptId, null);
        if (null != userList) {
            for (SysUser user : userList) {
                HashMap<String, String> userMap = new HashMap<>(16);
                userMap.put("userName", user.getNickName());
                userMap.put("userId", user.getUserId());
                result.add(userMap);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> selectDeptAllUserByPostCode(String postCode, String orgCode) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<SysUser> userList = userMapper.selectUserNameByPostCode(postCode, null, orgCode);
        if (null != userList) {
            for (SysUser user : userList) {
                HashMap<String, String> userMap = new HashMap<>(16);
                userMap.put("userName", user.getNickName());
                userMap.put("userId", user.getUserId());
                result.add(userMap);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> selectOrgLeaderByPostCode(String postCode, String supplyOrgCode) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<SysUser> userList = userMapper.selectUserNameByPostCode(postCode, null, supplyOrgCode);
        if (null != userList && userList.size() > 0) {
            for (SysUser user : userList) {
                HashMap<String, String> userMap = new HashMap<>(16);
                userMap.put("userName", user.getNickName());
                userMap.put("userId", user.getUserId());
                result.add(userMap);
            }
        }
        return result;
    }

    @Override
    public List<SysUser> selectUserByDeptId(String deptId) {

        return userMapper.selectUserListByDeptId(deptId);

    }

    @Override
    public List<SysUser> selectUsers(SysUser user) {
        /** 中心端无需限制当前机构下的人员 **/

//        SysUser nowLoginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
//        if (!nowLoginUser.isAdmin()){
//            user.setOrgCode(tokenService.getOrgCode(ServletUtils.getRequest()));
//        }
        return userMapper.selectUserList(user);
    }

    @Override
    public AjaxResult getAllDeptLeader() {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<SysUser> userList = userMapper.getAllDeptLeader(user.getOrgCode(), user.getDeptId());
        return new AjaxResult(200, "查询成功", userList);
    }

    @Override
    public AjaxResult getUserByPost(String post, String deptId) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        String orgCode = "";
        if (!user.isAdmin()) {
            orgCode = user.getOrgCode();
        }
        List<SysUser> userList = userMapper.getUserByPost(post, deptId, orgCode);
        return new AjaxResult(200, "查询成功", userList);
    }

    @Override
    public AjaxResult getUserByPostForOrg(String post) {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        List<SysUser> userList = userMapper.getUserByPostForOrg(post, user.getOrgCode(), user.getDeptId());
        return new AjaxResult(200, "查询成功", userList);
    }

    @Override
    public List<SysUser> getAllUser() {
        return userMapper.getAllUser();
    }

    /**
     * 客户管理 - 新增
     *
     * @param customer
     * @return
     */
    @Override
    public AjaxResult insertCustomer(SysUser customer, CustomerVO customerVO) {
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        // 用户名 + 手机号默认用户名
        customer.setUserName(customer.getPhoneNumber());
        customer.setPassword(SecurityUtils.encryptPassword(UserConstants.DEFAULT_PASSWORD));
        customer.setUserType(UserConstants.USER_TYPE_2);
        customer.setOrgCode(loginUser.getOrgCode());
        customer.setDelFlag(Constants.UNDELETE);
        customer.setCreateBy(loginUser.getNickName());
        customer.setCreateTime(new Date());
        return AjaxResult.toAjax(userMapper.insertUser(customer));
    }

    @Override
    public AjaxResult updateCustomer(SysUser customer, CustomerVO customerVO) {
        SysUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        // 用户名 + 手机号默认用户名
        customer.setUserName(customer.getPhoneNumber());
        customer.setPassword(SecurityUtils.encryptPassword(UserConstants.DEFAULT_PASSWORD));
        customer.setUserType(UserConstants.USER_TYPE_2);
        customer.setOrgCode(loginUser.getOrgCode());
        customer.setDelFlag(Constants.UNDELETE);
        customer.setUpdateBy(loginUser.getNickName());
        customer.setUpdateTime(new Date());
        return AjaxResult.toAjax(userMapper.updateUser(customer));
    }

    @Override
    public AjaxResult getUserList() {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper
                .eq(SysUser::getDelFlag, Constants.UNDELETE)
                .eq(SysUser::getOrgCode, user.getOrgCode())
                .eq(SysUser::getUserType, Constants.STRING_1);
        List<SysUser> userList = userMapper.selectList(userWrapper);
        List<Map<String, String>> resultList = new ArrayList<>();
        if (LocalStringUtils.isNotEmpty(userList)) {
            for (SysUser sysUser : userList) {
                Map<String, String> resultMap = new HashMap<>(16);
                resultMap.put("userId", sysUser.getUserId());
                resultMap.put("userName", sysUser.getNickName());
                resultList.add(resultMap);
            }
        }
        return AjaxResult.success("操作成功", resultList);
    }

    @Override
    public AjaxResult getCustomerList() {
        SysUser user = tokenService.getLoginUser(ServletUtils.getRequest()).getUser();
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper
                .eq(SysUser::getDelFlag, Constants.UNDELETE)
                .eq(SysUser::getOrgCode, user.getOrgCode())
                .eq(SysUser::getUserType, Constants.STRING_2);
        List<SysUser> userList = userMapper.selectList(userWrapper);
        List<Map<String, String>> resultList = new ArrayList<>();
        if (LocalStringUtils.isNotEmpty(userList)) {
            for (SysUser sysUser : userList) {
                Map<String, String> resultMap = new HashMap<>(16);
                resultMap.put("userId", sysUser.getUserId());
                resultMap.put("userName", sysUser.getNickName());
                resultList.add(resultMap);
            }
        }
        return AjaxResult.success("操作成功", resultList);
    }
}
