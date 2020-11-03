package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.CategoryDao;
import net.sunofbeach.blog.pojo.Category;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.CategoryServices;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: CategoryServices的实现类
 * @author: JinFan
 * @create: 2020-09-26 14:09
 **/
@Slf4j
@Service
@Transactional
public class CategoryServicesImpl extends BaseServices implements CategoryServices {

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private UserServices userServices;

    /**
     * description  TODO:添加分类
     * <p>
     * 需要管理员权限
     * </p>
     * date         2020/9/15 21:17
     *
     * @param category:分类
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult addCategory(Category category) {
        //检查数据
        Category categoryByName = categoryDao.findOneByName(category.getName());
        if (categoryByName != null) {
            return ResponseResult.FAILED("分类名称已存在");
        }
        //名称
        if (TextUtils.isEmpty(category.getName())) {
            return ResponseResult.FAILED("分类名称不能为空");
        }
        //拼音
        if (TextUtils.isEmpty(category.getPinyin())) {
            return ResponseResult.FAILED("分类拼音不能为空");
        }
        //描述
        if (TextUtils.isEmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类描述不能为空");
        }
        //补全数据
        category.setId(idWorker.nextId() + "");
        category.setStatus("1");
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        //保存数据
        categoryDao.save(category);
        //返回结果
        return ResponseResult.SUCCESS("分类添加成功!");
    }

    /**
     * description  TODO:根据categoryId获取category
     * date         2020/9/15 21:20
     *
     * @param categoryId:id
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getCategory(String categoryId) {
        Category category = categoryDao.findOneById(categoryId);
        if (category == null) {
            return ResponseResult.FAILED("分类不存在!");
        }
        return ResponseResult.SUCCESS("获取分类成功").setDate(category);
    }

    /**
     * description  TODO:获取分类列表
     * date         2020/9/15 21:30
     *
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listCategories() {
        //根据注册日期排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //判断用户角色,普通用户,未登录的用户:只能获取正常的category,管理员可以获取所有
        User user = userServices.checkUser();
        List<Category> categories ;
        if (user == null || Constants.User.ROLE_ADMIN.equals(user.getRoles())) {
            //只能获取正常的category
            categories = categoryDao.listCategoriesByState("1");
        }else {
            //查询:管理员可以获取所有
            categories = categoryDao.findAll(sort);
        }
        //返回结果
        return ResponseResult.SUCCESS("获取分类列表成功!").setDate(categories);
    }

    /**
     * description  TODO:根据categoryId修改category
     * date         2020/9/15 21:20
     *
     * @param categoryId:id
     * @param category:分类
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
        //根据id查找对应的category
        Category categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return ResponseResult.FAILED("分类不存在!");
        }
        //修改数据
        //名称
        String name = category.getName();
        if (!TextUtils.isEmpty(name)) {
            categoryFromDb.setName(name);
        }
        //拼音
        String pinyin = category.getPinyin();
        if (!TextUtils.isEmpty(pinyin)) {
            categoryFromDb.setPinyin(pinyin);
        }
        //描述
        String description = category.getDescription();
        if (!TextUtils.isEmpty(description)) {
            categoryFromDb.setDescription(description);
        }
        //Order
        categoryFromDb.setOrder(category.getOrder());
        //UpdateTime
        categoryFromDb.setUpdateTime(new Date());
        //保存数据
        categoryDao.save(categoryFromDb);
        return ResponseResult.SUCCESS("分类修改成功!");
    }

    /**
     * description  TODO:根据categoryId删除category
     * date         2020/9/15 21:20
     *
     * @param categoryId:id
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteCategoryById(String categoryId) {
        //根据id查找对应的category
        Category categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return ResponseResult.FAILED("分类不存在!");
        }
        //可以删除
        int result = categoryDao.deleteAllByUpdateState(categoryId);
        if (result == 0) {
            return ResponseResult.FAILED("分类删除失败!");
        }
        return ResponseResult.SUCCESS("分类删除成功!");
    }
}
