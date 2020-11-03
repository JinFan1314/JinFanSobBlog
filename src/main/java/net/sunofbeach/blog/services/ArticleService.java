package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.Article;
import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: Article的service
 * @author: JinFan
 * @create: 2020-09-29 18:01
 **/
public interface ArticleService {
    /**
     * description  TODO;添加文章
     * date         2020/9/15 22:06
     * @author      jinFan
     * @param       article:文章
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult postArticle(Article article);

    /**
     * description  TODO:根据文章id删除文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteArticle(String articleId);

    /**
     * description  TODO:根据文章id修改文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @param       article:文章
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateArticle(String articleId, Article article);

    /**
     * description  TODO:根据文章id获取文章
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getArticleById(String articleId);

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
    ResponseResult listArticles(int page, int size,String state, String keywords, String categoryId);

    /**
     * description  TODO:修改状态
     * date         2020/9/15 22:13
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteArticleByUpdateState(String articleId);

    /**
     * description  TODO:修改状态(置顶)
     * date         2020/9/15 22:13
     * @author      jinFan
     * @param       articleId:文章id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateTopArticleStateTop(String articleId);

    /**
     * description  TODO:获取置顶文章
     * date         2020/10/1 13:04
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult ListTopArticles();

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
    ResponseResult listRecommendArticle(String articleId, int size);

    /**
     * description  TODO:通过标签获取文章列表
     * date         2020/10/1 16:50
     * @author      jinFan
     * @param       label:标签
     * @param       page:页码
     * @param       size:个数,每页的个数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listArticlesByLabel(int page, int size, String label);

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
    ResponseResult listLabels(int size);
}
