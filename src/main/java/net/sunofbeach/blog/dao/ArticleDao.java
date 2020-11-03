package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @program: SobBlogSystem
 * @description: 文章Dao
 * @author: JinFan
 * @create: 2020-09-29 18:00
 **/
public interface ArticleDao extends JpaRepository<Article,String> , JpaSpecificationExecutor<Article> {

    /**
     * description  TODO:根据id查询文章
     * date         2020/9/29 18:53
     * @author      jinFan
     * @param       articleId:articleId
     * @return      Article
     */
    Article findOneById(String articleId);


    /**
     * description  TODO:修改文章状态
     * date         2020/9/29 19:04
     * @author      jinFan
     * @param       articleId:文章id
     * @return      int
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_article` set `state` = '0' where `id` = ?")
    int deleteArticleByState(String articleId);

    /**
     * description  TODO:删除文章
     * date         2020/9/30 4:00
     * @author      jinFan
     * @param       articleId:文章id
     * @return      int
     */
    int deleteAllById(String articleId);

    /**
     * description  TODO:置顶文章
     * date         2020/9/30 4:17
     * @author      jinFan
     * @param       articleId:文章id
     * @return      void
     */
    @Modifying
    @Query(nativeQuery = true,value = "update  `tb_article` set `state` = '3' where `id` = ?")
    int topArticle(String articleId);

    /**
     * description  TODO:获取文章标签
     * date         2020/10/1 13:48
     * @author      jinFan
     * @param       articleId:文章id
     * @return      java.lang.String
     */
    @Modifying
    @Query(nativeQuery = true,value = "select `labels` from `tb_article` where `id` = ?")
    String listArticleLabelsById(String articleId);
}
