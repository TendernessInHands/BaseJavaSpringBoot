package com.mars.system.service;

import java.util.List;

import com.mars.common.core.domain.TreeSelect;
import com.mars.common.core.domain.entity.SysPosts;
import com.mars.system.domain.SysPost;

/**
 * 岗位信息 服务层
 *
 * @author mars
 */
public interface ISysPostService {
    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    public List<SysPost> selectPostAll();

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    public SysPost selectPostById(String postId);

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    public List<String> selectPostListByUserId(String userId);

    /**
     * 校验岗位名称
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostNameUnique(SysPost post);

    /**
     * 校验岗位编码
     *
     * @param post 岗位信息
     * @return 结果
     */
    public String checkPostCodeUnique(SysPost post);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(String postId);

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    public int deletePostById(String postId);

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deletePostByIds(String[] postIds);

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int insertPost(SysPost post);

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    public int updatePost(SysPost post);

    /**
     * 根据用户获取用户的岗位信息
     *
     * @param userId
     * @return
     */
    List<SysPost> selectPostCodeByUserId(String userId);


    /**
     * 岗位信息构建前端所需要下拉树结构
     *
     * @param posts 岗位列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildPostTreeSelect(List<SysPosts> posts);

    /**
     * 构建前端所需要树结构
     *
     * @param posts
     * @return
     */
    public List<SysPosts> buildPostTree(List<SysPosts> posts);

}
