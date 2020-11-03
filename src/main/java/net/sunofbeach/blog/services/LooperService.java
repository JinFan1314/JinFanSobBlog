package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.Looper;
import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: 轮播图service
 * @author: JinFan
 * @create: 2020-09-29 14:51
 **/
public interface LooperService {

    /**
     * description  TODO:添加轮播图
     * date         2020/9/15 21:48
     * @author      jinFan
     * @param       looper:轮播图
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult addLooper(Looper looper);

    /**
     * description  TODO:获取轮播图列表
     * date         2020/9/15 22:02
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listLooper();

    /**
     * description  TODO:根据轮播id删除轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteLooper(String looperId);

    /**
     * description  TODO:根据轮播图id获取修轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播图id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getLooper(String looperId);


    /**
     * description  TODO:根据轮播图id修改轮播图
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       looperId:轮播图id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateLooper(String looperId, Looper looper);
}
