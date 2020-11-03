package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: SobBlogSystem
 * @description: RefreshToken的Dao
 * @author: JinFan
 * @create: 2020-09-21 01:44
 **/
public interface RefreshTokenDao extends JpaRepository<RefreshToken,String>, JpaSpecificationExecutor<RefreshToken> {

    /**
     * description  TODO:根据tokenKey查询
     * date         2020/9/21 2:33
     * @author      jinFan
     * @param       cookie:cookie
     * @return      net.sunroof.blog.pojo.RefreshToken
     */
    RefreshToken findOneByTokenKey(String cookie);

    
    /**
     * description  TODO:根据userId删除refreshToken
     * date         2020/9/21 14:11
     * @author      jinFan
     * @param       userId:userId
     * @return      int
     */
    int deleteAllByUserId(String userId);

    /**
     * description  TODO:删除refreshToken
     * date         2020/9/26 13:27
     * @author      jinFan
     * @param       tokenKey :tokenKey
     */
    void deleteAllByTokenKey(String tokenKey);
}
