package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Looper;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.LooperService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 轮播图Api
 * @author: JinFan
 * @create: 2020-09-15 21:38
 **/
@Slf4j
@RestController
@RequestMapping("/admin/looper")
public class LooperAdminApi {

    @Resource
    private LooperService looperService;

    /**
     * description  TODO:添加轮播图
     * date         2020/9/15 21:48
     * @author      jinFan
     * @param       looper:轮播图
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLooper(@RequestBody Looper looper){
        return looperService.addLooper(looper);
    }

    /**
     * description  TODO:根据轮播id删除轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{looperId}")
    public ResponseResult deleteLooper(@PathVariable("looperId") String looperId){
        return looperService.deleteLooper(looperId);
    }

    /**
     * description  TODO:根据轮播图id修改轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播图id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{looperId}")
    public ResponseResult updateLooper(@PathVariable("looperId") String looperId,@RequestBody Looper looper){
        return looperService.updateLooper(looperId,looper);
    }

    /**
     * description  TODO:根据轮播图id获取修轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播图id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{looperId}")
    public ResponseResult getLooper(@PathVariable("looperId") String looperId){
        return looperService.getLooper(looperId);
    }

   /**
    * description  TODO:获取轮播图列表
    * date         2020/9/15 22:02
    * @author      jinFan
    * @return      net.sunofbeach.blog.response.ResponseResult
    */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLooper(){
        return looperService.listLooper();
    }
}
