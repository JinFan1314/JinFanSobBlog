package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.UserNoPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: SobBlogSystem
 * @description: 用户没有密码d的Dao
 * @author: JinFan
 * @create: 2020-09-25 19:43
 **/
public interface UserNoPasswordDao extends JpaRepository<UserNoPassword,String>, JpaSpecificationExecutor<UserNoPassword> {
}
