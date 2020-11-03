package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.ImageServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SobBlogSystem
 * @description: 图片Api
 * @author: JinFan
 * @create: 2020-09-15 21:36
 **/
@Slf4j
@RestController
@RequestMapping("/admin/image")
public class ImageAdminApi {

    @Resource
    private ImageServices imageServices;

    /**
     * description  TODO:上传图片
     * date         2020/9/15 21:48
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult uploadImage(@RequestParam("file")MultipartFile file){
        return imageServices.uploadImage(file);
    }

    /**
     * description  TODO:根据图片id删除图片
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       imageId:图片ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId") String imageId){
        return imageServices.deleteImage(imageId);
    }

    /**
     * description  TODO:根据图片id获取图片
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       imageId:图片ID
     * @param       response:response
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId") String imageId)  {
        try {
            imageServices.viewsImage(response,imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * description  TODO:获取图片列表
     * date         2020/9/15 22:04
     * @author      jinFan
     * @param       page:页码
     * @param       size:页数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listImages(@PathVariable("page") int page,@PathVariable("size") int size){
        return imageServices.listImages(page,size);
    }
}
