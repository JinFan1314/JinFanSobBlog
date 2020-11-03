package net.sunofbeach.blog.dao;


import net.sunofbeach.blog.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: Category的Dao
 * @author: JinFan
 * @create: 2020-09-26 14:13
 **/
public interface CategoryDao extends JpaRepository<Category,String>, JpaSpecificationExecutor<Category>  {
    /**
     * description  TODO:根据categoryId获取category
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       categoryId:id
     */
    Category findOneById(String categoryId);

    /**
     * description  TODO:删除:并不是真正的删除
     * date         2020/9/26 16:19
     * @author      jinFan
     * @param       categoryId:id
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_categories` set `status` = '0' where `id` =? ")
    int deleteAllByUpdateState(String categoryId);

    /**
     * description  TODO:查询分类是否存在
     * date         2020/9/26 16:34
     * @author      jinFan
     * @param       name:分类名称
     * @return      net.sunbeam.blog.pojo.Category
     */
    Category findOneByName(String name);

    @Modifying
    @Query(nativeQuery = true,value = "select * from `tb_categories` where `status` =? order by `create_time` desc ")
    List<Category> listCategoriesByState(String status);


}
