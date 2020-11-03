package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.Comment;
import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: 评论的service
 * @author: JinFan
 * @create: 2020-11-02 16:32
 **/
public interface CommentService {

    /**
     * description  TODO:提交评论
     * date         2020/9/15 22:18
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult postComment(Comment comment);

    /**
     * description  TODO:获取文章的评论
     * date         2020/9/16 16:45
     * @author      jinFan
     * @param       articleId：文章ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listCommentsByArticleId(String articleId, int page, int size);

    /**
     * description  TODO:根据评论id删除评论
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       commentId:评论ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteCommentById(String commentId);
}
