package net.sunofbeach.blog.services;

import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: WebSize的service
 * @author: JinFan
 * @create: 2020-09-29 15:45
 **/
public interface WebSizeService {
    /*
     * description  TODO:获取网站标题
     * date         2020/9/16 14:47
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getWebSizeTitle();

    /**
     * description  TODO:修改网站的标题
     * date         2020/9/16 16:21
     * @author      jinFan
     * @param       title:网站的标题
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateWebSizeTitle(String title);

    /**
     * description  TODO:获取网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getSeoInfo();

    /**
     * description  TODO:提交网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @param       keywords：keywords
     * @param       description：description
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult putSeoInfo(String keywords, String description);

    /**
     * description  TODO:获取网站统计数
     * date         2020/9/16 16:27
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getWebSizeViewCount();
}
