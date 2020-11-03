package net.sunofbeach.blog.dao;
import net.sunofbeach.blog.pojo.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: SobBlogSystem
 * @description: imageDao
 * @author: JinFan
 * @create: 2020-09-28 15:27
 **/
public interface ImageDao extends JpaRepository<Images,String>, JpaSpecificationExecutor<Images>  {
    
    /**
     * description  TODO:根据id查询
     * date         2020/9/28 18:27
     * @author      jinFan
     * @param       imageId:id
     * @return      net.sunofbeach.blog.pojo.Images
     */
    Images findOneById(String imageId);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_images` set `state` = '0' where `id` = ?")
    int deleteImagesByState(String imageId);
}
