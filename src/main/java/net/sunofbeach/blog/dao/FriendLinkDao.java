package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: 友情链接dao
 * @author: JinFan
 * @create: 2020-09-26 17:42
 **/
public interface FriendLinkDao extends JpaRepository<Friends,String>, JpaSpecificationExecutor<Friends> {

    /**
     * description  TODO:根据ID查询
     * date         2020/9/26 18:20
     * @author      jinFan
     * @param       friendId:id
     * @return      net.sunbeam.blog.pojo.Friends
     */
    Friends findOneById(String friendId);

    /**
     * description  TODO:根据ID删除
     * date         2020/9/26 18:32
     * @author      jinFan
     * @param       friendId:id
     * @return      int
     */
    int deleteAllById(String friendId);

    /**
     * description  TODO::根据状态查询
     * date         2020/9/30 13:59
     * @author      jinFan
     * @param       state:状态
     * @return      java.util.List<net.sunbeam.blog.pojo.Friends>
     */
    @Modifying
    @Query(nativeQuery = true,value = "select * from `tb_friends` where  `state` = ? order by `create_time` desc ")
    List<Friends> listFriendLinkByState(String state);

    /**
     * description  TODO:判断友情链接是否存在
     * date         2020/10/1 0:44
     * @author      jinFan
     * @param       name:链接名称
     * @return      net.sunofbeach.blog.pojo.Friends
     */
    Friends findOneByName(String name);
}
