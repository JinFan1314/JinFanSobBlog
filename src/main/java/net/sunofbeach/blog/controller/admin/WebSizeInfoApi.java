package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

/**
 * @program: SobBlogSystem
 * @description: 网站信息api
 * @author: JinFan
 * @create: 2020-09-15 21:37
 **/
@Slf4j
@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoApi {

    /*
     * description  TODO:获取网站标题
     * date         2020/9/16 14:47
     * @author      jinFan
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return null;
    }

    /**
     * description  TODO:修改网站的标题
     * date         2020/9/16 16:21
     * @author      jinFan
     * @param       title:网站的标题
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @PutMapping("/title")
    public ResponseResult updateWebSizeTitle(@RequestParam("title") String title){
        return null;
    }

    /**
     * description  TODO:获取网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @GetMapping("/seo")
    public ResponseResult getSeoInfo(){
        return  null;
    }

    /**
     * description  TODO:提交网站的seo
     * date         2020/9/16 16:22
     * @author      jinFan
     * @param       keywords：keywords
     * @param       description：description
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords") String keywords,@RequestParam("description") String description){
        return  null;
    }

    /**
     * description  TODO:获取网站统计数
     * date         2020/9/16 16:27
     * @author      jinFan
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @GetMapping("view_count")
    public ResponseResult getWebSizeViewCount(){
        return null;
    }




}
