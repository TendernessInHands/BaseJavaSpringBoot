package com.mars.system.mapper;

import java.util.List;
import com.mars.system.domain.SysUserPost;

/**
 * 用户与岗位关联表 数据层
 * 
 * @author mars
 */
public interface SysUserPostMapper {
    /**
     * 通过用户ID删除用户和岗位关联
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserPostByUserId(String userId);

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(String postId);

    /**
     * 批量删除用户和岗位关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserPost(Long[] ids);

    /**
     * 批量新增用户岗位信息
     * 
     * @param userPostList 用户角色列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);

    /**
     * 根据用户获取用户的岗位信息
     * @param userId
     * @return
     */
    SysUserPost selectUserPostByUserId(String userId);

    /**
     * 通过岗位ID 查询用户岗位信息
     *
     * @param postId
     * @return
     */
    public List<SysUserPost> selectUserPostByPostId(String postId);

}
