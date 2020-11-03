package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: SobBlogSystem
 * @description: userDao类
 * @author: JinFan
 * @create: 2020-09-17 12:37
 **/
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    /**
     * description  TODO:根据用户名查询用户
     * date         2020/9/18 17:26
     * @author      jinFan
     * @param       userName:用户名
     * @return      net.sunroof.blog.pojo.User
     */
    User findOneByUserName(String userName);

    /**
     * description  TODO:根据邮箱查询用户
     * date         2020/9/18 18:57
     * @author      jinFan
     * @param       email:邮箱四肢
     * @return      net.sunroof.blog.pojo.User
     */
    User findOneByEmail(String email);

    /*
     * description  TODO:根据userId查询
     * date         2020/9/21 2:42
     * @author      jinFan
     * @param       userId
     * @return      net.sunroof.blog.pojo.User
     */
    User findOneById(String userId);

    /**
     * description  TODO:删除用户
     * date         2020/9/24 20:08
     * @author      jinFan
     * @param       userId:用户id
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `state` = '0' where `id` = ?")
    int deleteUserByState(String userId);

    /**
     * description  TODO:修改密码
     * date         2020/9/26 11:33
     * @author      jinFan
     * @param       password:密码
     * @param       email:email
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `password` = ? where `email` = ?")
    int updateUserPasswordByEmail(String password, String email);

    /**
     * description  TODO:修改邮箱
     * date         2020/9/26 11:34
     * @author      jinFan
     * @param       email:email
     * @param       id:id
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_user` set `email` = ? where `id` = ?")
    int updateEmailById(String email, String id);
}
