package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: SobBlogSystem
 * @description: comment的Dao
 * @author: JinFan
 * @create: 2020-09-20 15:45
 **/
public interface CommentDao extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {
    
    /**
     * description  TODO:根据评论id查询评论
     * date         2020/11/3 16:49
     * @author      jinFan
     * @param       commentId:评论ID
     * @return      net.sunofbeach.blog.pojo.Comment
     */
    Comment findOneById(String commentId);
}
