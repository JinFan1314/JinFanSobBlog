package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.Category;
import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: 分类的services
 * @author: JinFan
 * @create: 2020-09-26 14:08
 **/
public interface CategoryServices {

    /**
     * description  TODO:添加分类
     * <p>
     *     需要管理员权限
     * </p>
     * date         2020/9/15 21:17
     * @author      jinFan
     * @param       category:分类
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult addCategory(Category category);

    /**
     * description  TODO:根据categoryId获取category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getCategory(String categoryId);

    /**
     * description  TODO:获取分类列表
     * date         2020/9/15 21:30
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listCategories();

    /**
     * description  TODO:根据categoryId修改category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @param       category:分类
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateCategory(String categoryId, Category category);

    /**
     * description  TODO:根据categoryId删除category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteCategoryById(String categoryId);
}
