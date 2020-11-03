package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Article;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 文章Api
 * @author: JinFan
 * @create: 2020-09-15 21:35
 **/
@Slf4j
@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {

    @Resource
    private ArticleService articleService;


    /**
     * description  TODO;添加文章
     * date         2020/9/15 22:06
     * @author      jinFan
     * @param       article:文章
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult postArticle(@RequestBody Article article){
        return articleService.postArticle(article);
    }

    /**
     * description  TODO:根据文章id删除文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId") String articleId){
        return articleService.deleteArticle(articleId);
    }

    /**
     * description  TODO:根据文章id修改文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @param       article:文章
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId,@RequestBody Article article){
        return articleService.updateArticle(articleId,article);
    }

    /**
     * description  TODO:根据文章id获取文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId){
        return articleService.getArticleById(articleId);
    }


    /**
     * description  TODO:获取文章列表
     * date         2020/9/15 22:04
     * @param page :页码
     * @param size :页数
     * @param keywords:管关键字
     * @param categoryId:分类id
     * @param state:状态
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page") int page,@PathVariable("size") int size,
                                       @RequestParam(value = "state",required = false) String state,
                                       @RequestParam(value = "keywords",required = false) String keywords,
                                       @RequestParam(value = "categoryId",required = false) String categoryId){
        return articleService.listArticles(page,size,state,keywords,categoryId);
    }

    /**
     * description  TODO:修改状态
     * date         2020/9/15 22:13
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/state/{articleId}")
    public ResponseResult deleteArticleByUpdateState(@PathVariable("articleId") String articleId){
        return articleService.deleteArticleByUpdateState(articleId);
    }

    /**
     * description  TODO:修改状态(置顶)
     * date         2020/9/15 22:13
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult updateTopArticleState(@PathVariable("articleId") String articleId){
        return articleService.updateTopArticleStateTop(articleId);
    }
}
