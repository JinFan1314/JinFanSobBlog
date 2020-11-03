package net.sunofbeach.blog.controller.portal;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SobBlogSystem
 * @description: 搜索api
 * @author: JinFan
 * @create: 2020-09-16 17:00
 **/
@Slf4j
@RestController
@RequestMapping("/portal/search")
public class SearchPortalApi {

    /**
     * description  TODO:搜索
     * date         2020/9/16 17:02
     * @author      jinFan
     * @param       keyword:关键字
     * @param       page:页码
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping
    public ResponseResult doSearch(@RequestParam("keyword") String keyword,@RequestParam("page") int page){
        return null;
    }
}
