package com.mars.system.service.impl;

import com.mars.system.domain.SysUserPost;
import com.mars.system.mapper.SysUserPostMapper;
import com.mars.system.service.ISysUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务层处理
 *
 * @author mars
 */
@Service
public class SysUserPostServiceImpl implements ISysUserPostService {

    @Autowired
    private SysUserPostMapper sysUserPostMapper;


    @Override
    public List<SysUserPost> selectUserPostByPostId(String postId) {

        return sysUserPostMapper.selectUserPostByPostId(postId);

    }

    @Override
    public SysUserPost selectUserPostByUserId(String userId) {
        return sysUserPostMapper.selectUserPostByUserId(userId);
    }
}
