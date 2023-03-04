package com.mars.system.service;

import com.mars.system.domain.SysUserPost;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author mars
 */
public interface ISysUserPostService {

    /**
     * 根据岗位ID查询岗位用户信息
     *
     * @param postId
     * @return
     */
    public List<SysUserPost> selectUserPostByPostId(String postId);

    /**
     * 根据用户获取用户的岗位信息
     * @param userId
     * @return
     */
    SysUserPost selectUserPostByUserId(String userId);
}
