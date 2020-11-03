package net.sunofbeach.blog.controller;

import net.sunofbeach.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SobBlogSystem
 * @description: 权限访问处理
 * @author: JinFan
 * @create: 2020-09-25 20:25
 **/
@RestController
public class ErrorPageController {

    @GetMapping("/403")
    public ResponseResult page403() {
        return ResponseResult.ERROR_403();
    }

    @GetMapping("/404")
    public ResponseResult page404() {
        return ResponseResult.ERROR_404();
    }

    @GetMapping("/405")
    public ResponseResult page405() {
        return ResponseResult.ERROR_405();
    }

    @GetMapping("/504")
    public ResponseResult page504() {
        return ResponseResult.ERROR_504();
    }

    @GetMapping("/505")
    public ResponseResult page505() {
        return ResponseResult.ERROR_505();
    }
}
