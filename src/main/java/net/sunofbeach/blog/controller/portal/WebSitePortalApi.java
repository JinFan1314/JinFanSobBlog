package net.sunofbeach.blog.controller.portal;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.CategoryServices;
import net.sunofbeach.blog.services.FriendLinkServices;
import net.sunofbeach.blog.services.LooperService;
import net.sunofbeach.blog.services.WebSizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 门户网站信息
 * @author: JinFan
 * @create: 2020-09-16 16:49
 **/
@Slf4j
@RestController
@RequestMapping("/portal/web_site_info")
public class WebSitePortalApi {

    @Resource
    private CategoryServices categoryServices;

    @Resource
    private WebSizeService webSizeService;

    @Resource
    private LooperService looperService;

    @Resource
    private FriendLinkServices friendLinkServices;

    /**
     * description  TODO:获取分类
     * date         2020/9/16 16:52
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/categories")
    public ResponseResult getCategories(){
        return categoryServices.listCategories();
    }

    /**
     * description  TODO:获取网站的标题
     * date         2020/9/16 16:53
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return webSizeService.getWebSizeTitle();
    }

    /**
     * description  TODO:获取网站访问统计数
     * date         2020/9/16 16:53
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
        return webSizeService.getWebSizeViewCount();
    }

    /**
     * description  TODO；获取网站的seo
     * date         2020/9/16 16:56
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/seo")
    public ResponseResult getWebSizeSeoInfo(){
        return  webSizeService.getSeoInfo();
    }

    /**
     * description  TODO:根据轮播图id获取修轮播图
     * date         2020/9/16 16:56
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/loop")
    public ResponseResult getLooper(){
        return looperService.listLooper();
    }

    /**
     * description  TODO:根据友情连接id获取连接
     * date         2020/9/16 16:56
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/friend_link")
    public ResponseResult getFriendsLink(){
        return friendLinkServices.listFriendsLinks();
    }

}
