package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: looper的dao层
 * @author: JinFan
 * @create: 2020-09-29 14:55
 **/
public interface LooperDao extends JpaRepository<Looper,String>, JpaSpecificationExecutor<Looper> {
    
    /**
     * description  TODO:根据ID获取looper
     * date         2020/9/29 15:20
     * @author      jinFan
     * @param       looperId:looperId
     * @return      net.sunbeam.blog.pojo.Looper
     */
    Looper findOneById(String looperId);

    /**
     * description  TODO:删除looper
     * date         2020/9/30 14:10
     * @author      jinFan
     * @param       looperId:looperId
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update `tb_looper` set `state` = '0' where `id` = ?")
    int deleteLooperByUpdateState(String looperId);

    /**
     * description  TODO:根据状态查询
     * date         2020/9/30 13:59
     * @author      jinFan
     * @param       state:状态
     * @return      java.util.List<net.sunbeam.blog.pojo.Looper>
     */
    @Modifying
    @Query(nativeQuery = true,value = "select * from `tb_looper` where `state` = ? order by `create_time` desc ")
    List<Looper> listLooperByState(String state);

    /**
     * description  TODO:判断looper是否存在
     * date         2020/10/1 0:52
     * @author      jinFan
     * @param       title:title
     * @return      net.sunofbeach.blog.pojo.Looper
     */
    Looper findOneByTitle(String title);
}
