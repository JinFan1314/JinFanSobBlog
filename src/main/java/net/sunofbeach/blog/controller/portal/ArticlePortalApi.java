package net.sunofbeach.blog.controller.portal;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.ArticleService;
import net.sunofbeach.blog.util.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 门户文章的api
 * @author: JinFan
 * @create: 2020-09-16 16:31
 **/
@Slf4j
@RestController
@RequestMapping("/portal/article")
public class ArticlePortalApi {

    @Resource
    private ArticleService articleService;

    /**
     * description  TODO:获取文章列表(必须是已发布的)
     * date         2020/9/16 16:34
     * @author      jinFan
     * @param       page:页码
     * @param       size:个数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticle(@PathVariable("page") int page,@PathVariable("size") int size){
        return articleService.listArticles(page,size,null,null, Constants.Article.STATE_PUBLISH);
    }
    /**
     * description  TODO:根据categoryId获取文章列表
     * date         2020/9/16 16:37
     * @author      jinFan
     * @param       categoryId:categoryId
     * @param       page:页码
     * @param       size:个数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/list/{categoryId}/{page}/{size}")
    public ResponseResult listArticleByCategoryId(@PathVariable("categoryId") String categoryId,
                                                  @PathVariable("page") int page,
                                                  @PathVariable("size") int size){
        return articleService.listArticles(page,size,null,categoryId,Constants.Article.STATE_PUBLISH);

    }

    /**
     * description  TODO:获取文章的Detail:详情
     * 只允许那置顶的和已发布的
     * 其他的获取需要权限
     * date         2020/9/16 16:40
     * @author      jinFan
     * @param       articleId:articleId
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/{articleId}")
    public ResponseResult getArticleDetail(@PathVariable("articleId") String articleId ){
        return articleService.getArticleById(articleId);
    }

    /**
     * description  TODO；获取文章recommend
     * <p>
     *     通过文章标签计算匹配度
     *     从里面随机哪一个标签 --- >每次获取的文章不那么雷同
     *     通过标签去查询类似的文章,所包含的次标签的文章
     *     如果没有相关文章,就获取最新的文章
     *
     * </p>
     * date         2020/9/16 16:41
     * @author      jinFan
     * @param       articleId：文章的id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticles(@PathVariable("articleId") String articleId,
                                                @PathVariable("size") int size){
        return articleService.listRecommendArticle(articleId,size);
    }

    /**
     * description  TODO:获取文章列表,通过标签
     * date         2020/10/1 13:04
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/top")
    public ResponseResult getTopArticles(){
        return articleService.ListTopArticles();
    }

    /**
     * description  TODO:通过标签获取文章列表
     * date         2020/10/1 16:50
     * @author      jinFan
     * @param       label:标签
     * @param       page:页码
     * @param       size:个数,每页的个数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/list/label/{label}/{page}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label") String label,
                                      @PathVariable("page") int page,
                                      @PathVariable("size") int size){
        return articleService.listArticlesByLabel(page,size,label);
    }

    /**
     * description  TODO:获取标签云,用户点击标签,就会通过标签获取相关的文章
     * <p>
     *     任意用户
     * </p>
     * date         2020/10/1 13:21
     * @author      jinFan
     * @param       size:个数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/labels/{size}")
    public ResponseResult getLabels(@PathVariable("size") int size){
        return articleService.listLabels(size);
    }

}
