package com.chengqj.study.security.service.impl;

import com.chengqj.study.security.entity.Blog;
import com.chengqj.study.security.service.IBlogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author chengqj
 * @Date 2021/1/27 23:45
 * @Desc
 */
@Service
public class BlogService implements IBlogService {
    private List<Blog> list = new ArrayList<>();

    public BlogService() {
        list.add(new Blog(1L, "Spring in action", "good!"));
        list.add(new Blog(2L, "Spring boot in action", "nice!"));
    }

    @Override
    public List<Blog> getBlogs() {
        return list;
    }

    @Override
    public void deleteBlog(long id) {
        Iterator<Blog> iterator = list.iterator();
        while (iterator.hasNext()) {
            Blog blog = (Blog) iterator.next();
            if (blog.getId() == id) {
                iterator.remove();
            }
        }
    }
}
