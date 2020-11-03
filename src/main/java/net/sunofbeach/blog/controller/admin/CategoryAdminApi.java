package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Category;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.CategoryServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: CategoryAdminApi:分类
 * @author: JinFan
 * @create: 2020-09-15 21:13
 **/
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {

    @Resource
    private CategoryServices categoryServices;

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
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryServices.addCategory(category);
    }

    /**
     * description  TODO:根据categoryId删除category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId") String categoryId){
        return categoryServices.deleteCategoryById(categoryId);
    }

    /**
     * description  TODO:根据categoryId修改category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @param       category:分类
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId") String categoryId,
                                         @RequestBody Category category){
        return categoryServices.updateCategory(categoryId,category);
    }

    /**
     * description  TODO:根据categoryId获取category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId") String categoryId){
        return categoryServices.getCategory(categoryId);
    }

    /**
     * description  TODO:获取分类列表
     * date         2020/9/15 21:30
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listCategories(){
        return categoryServices.listCategories();
    }
}
