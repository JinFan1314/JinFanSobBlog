package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Labels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: SobBlogSystem
 * @description: Label的dao层
 * @author: JinFan
 * @create: 2020-10-30 15:59
 **/
public interface LabelDao extends JpaRepository<Labels,String>, JpaSpecificationExecutor<Labels> {

    Labels findOneByName(String label);

    /**
     * description  TODO:根据名称修改个数
     * date         2020/10/30 16:37
     * @author      jinFan
     * @param       labelName:标签名称
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_labels` set `count` = `count` + 1 where `name` = ?")
    int updateCountByName(String labelName);
}
