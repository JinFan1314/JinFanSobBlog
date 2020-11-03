package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Article;
import net.sunofbeach.blog.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: 文章Dao
 * @author: JinFan
 * @create: 2020-09-29 18:00
 **/
public interface ArticleNoContentDao extends JpaRepository<ArticleNoContent,String> , JpaSpecificationExecutor<ArticleNoContent> {

    /**
     * description  TODO:根据id查询文章
     * date         2020/9/29 18:53
     * @author      jinFan
     * @param       articleId:articleId
     * @return      Article
     */
    ArticleNoContent findOneById(String articleId);
    
    /**
     * description  TODO:
     * date         2020/10/1 14:05
     * @author      jinFan
     * @param       originalArticleId:文章id
     * @return      java.util.List<net.sunofbeach.blog.pojo.ArticleNoContent>
     */
    @Modifying
    @Query(nativeQuery = true,value = "select * from `tb_article` where `labels` like ? and `id` != ? and(`state` = '1' or `state` = '3') limit ?")
    List<ArticleNoContent> listArticleByLikeLabel(String label,String originalArticleId,int size);

    /**
     * description  TODO:查询最新的文章
     * date         2020/10/1 17:09
     * @author      jinFan
     * @param       size:个数
     * @param       originalArticleId:文章id
     * @return      java.util.List<net.sunofbeach.blog.pojo.ArticleNoContent>
     */
    @Modifying
    @Query(nativeQuery = true,value = "select *from `tb_article`where `id`! = ? and(`state` = '1' or `state` = '3') order by `create_time` limit ?")
    List<ArticleNoContent> listLastedArticleBySize(String originalArticleId,int size);
}
