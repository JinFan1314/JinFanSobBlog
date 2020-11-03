package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.WebSizeService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 网站信息api
 * @author: JinFan
 * @create: 2020-09-15 21:37
 **/
@Slf4j
@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoAdminApi {

    @Resource
    private WebSizeService webSizeService;

    /*
     * description  TODO:获取网站标题
     * date         2020/9/16 14:47
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @RequestMapping(value = "/title")
    public ResponseResult getWebSizeTitle(){
        return webSizeService.getWebSizeTitle();
    }

    /**
     * description  TODO:修改网站的标题
     * date         2020/9/16 16:21
     * @author      jinFan
     * @param       title:网站的标题
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PutMapping("/update")
    public ResponseResult updateWebSizeTitle(@RequestParam("title") String title){
        return webSizeService.updateWebSizeTitle(title);
    }

    /**
     * description  TODO:获取网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/getSeoInfo")
    public ResponseResult getSeoInfo(){
        return  webSizeService.getSeoInfo();
    }

    /**
     * description  TODO:提交网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @param       keywords：keywords
     * @param       description：description
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PutMapping("/putSeoInfo")
    public ResponseResult putSeoInfo(@RequestParam("keywords") String keywords,
                                     @RequestParam("description") String description){
        return  webSizeService.putSeoInfo(keywords,description);
    }

    /**
     * description  TODO:获取网站统计数
     * date         2020/9/16 16:27
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @RequestMapping("/view_count")
    public ResponseResult getWebSizeViewCount(){
        return webSizeService.getWebSizeViewCount();
    }




}
