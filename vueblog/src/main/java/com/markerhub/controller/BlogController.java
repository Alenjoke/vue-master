package com.markerhub.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.service.BlogService;
import com.markerhub.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2021-03-18
 */
@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage,
                       @RequestParam(defaultValue = "5") Integer size) {

        Page page = new Page(currentPage, size);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));

        return Result.success(pageData);
    }

    @GetMapping("/blogs/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {

        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blogs/edit")
    public Result edit(@Validated @RequestBody Blog blog) {


        Blog tmp = null;
        if (blog.getId() != null) {
            //编辑状态
            tmp = blogService.getById(blog.getId());
            Assert.isTrue(tmp.getUserId() == ShiroUtil.getProfile().getId(), "没有权限编辑");
        } else {
            //添加状态
            tmp = new Blog();
            tmp.setUserId(ShiroUtil.getProfile().getId());
            tmp.setCreated(LocalDateTime.now());
            tmp.setStatus(0);

        }

        BeanUtil.copyProperties(blog, tmp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(tmp);

        return Result.success(null);
    }

    @PostMapping("/blogs/delete")
    public Result delete(@RequestBody Blog blog) {
        Blog tmp = blogService.getById(blog.getId());
        Assert.notNull(tmp, "该博客不存在！");
        Assert.isTrue(tmp.getUserId() == ShiroUtil.getProfile().getId(), "没有权限编辑");

        //进行删除操作
        blogService.removeById(tmp.getId());
        return Result.success("删除成功");
    }
}
