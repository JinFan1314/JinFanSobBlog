package net.sunofbeach.blog.controller.portal;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Comment;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 评论api
 * @author: JinFan
 * @create: 2020-09-15 22:26
 **/
@Slf4j
@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {

    @Resource
    private CommentService commentService;

    /**
     * description  TODO:提交评论
     * date         2020/9/15 22:18
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PostMapping
    public ResponseResult postComment(@RequestBody Comment comment){
        return commentService.postComment(comment);
    }

    /**
     * description  TODO:根据评论id删除评论
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       commentId:评论ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId){
        return commentService.deleteCommentById(commentId);
    }

    /**
     * description  TODO:获取文章的评论
     * date         2020/9/16 16:45
     * @author      jinFan
     * @param       articleId：文章ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComments(@PathVariable("articleId") String articleId,
                                       @PathVariable("page") int page,
                                       @PathVariable("size") int size){
        return commentService.listCommentsByArticleId(articleId,page,size);
    }


}
