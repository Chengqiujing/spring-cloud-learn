package com.chengqj.study.security.service;

import com.chengqj.study.security.entity.Blog;

import java.util.List;

/**
 * @Author chengqj
 * @Date 2021/1/27 23:41
 * @Desc
 */
public interface IBlogService {
    List<Blog> getBlogs();

    void deleteBlog(long id);
}
