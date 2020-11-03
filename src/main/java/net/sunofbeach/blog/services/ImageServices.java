package net.sunofbeach.blog.services;

import net.sunofbeach.blog.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: SobBlogSystem
 * @description: Image的service
 * @author: JinFan
 * @create: 2020-09-27 15:24
 **/
public interface ImageServices {

    /**
     * description  TODO:上传图片
     * date         2020/9/15 21:48
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult uploadImage(MultipartFile file);

    /**
     * description  TODO:根据图片id获取图片
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       imageId:图片ID
     * @param       response:response
     * @return      net.sunofbeach.blog.response.ResponseResult
     */

    void viewsImage(HttpServletResponse response, String imageId) throws IOException;

    /**
     * description  TODO:获取图片列表
     * date         2020/9/15 22:04
     * @author      jinFan
     * @param       page:页码
     * @param       size:页数
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listImages(int page, int size);

    /**
     * description  TODO:根据图片id删除图片
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       imageId:图片ID
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteImage(String imageId);
}
