package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Comment;
import net.sunofbeach.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @program: SobBlogSystem
 * @description: 评论api
 * @author: JinFan
 * @create: 2020-09-15 21:41
 **/
@Slf4j
@RestController
@RequestMapping("/admin/comment")
public class CommentAdminApi {

    /**
     * description  TODO:根据评论id删除评论
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       commentId:评论ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId){
        return null;
    }


    /**
     * description  TODO:获取评论列表
     * date         2020/9/15 22:04
     * @author      jinFan
     * @param       page:页码
     * @param       size:页数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/list")
    public ResponseResult listComments(@RequestParam("page") int page,@RequestParam("size") int size){
        return null;
    }

    /**
     * description  TODO:置顶评论
     * date         2020/9/15 22:30
     * @author      jinFan
     * @param       commentId:评论id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PutMapping("/top/{commentId}")
    public ResponseResult topComment(@PathVariable("commentId") String commentId){
        return null;
    }
}
