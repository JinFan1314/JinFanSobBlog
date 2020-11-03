package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.Friends;
import net.sunofbeach.blog.response.ResponseResult;

/**
 * @program: SobBlogSystem
 * @description: FriendLink的services
 * @author: JinFan
 * @create: 2020-09-26 17:43
 **/
public interface FriendLinkServices {

    /**
     * description  TODO;添加友情连接
     * <P>
     *     需要管理员权限
     * </P>
     * date         2020/9/15 22:06
     * @author      jinFan
     * @param       friends:友情连接
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult addFriendsLink(Friends friends);


    /**
     * description  TODO:根据友情连接id获取连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult getFriendsLink(String friendId);

    /**
     * description  TODO:获取友情连接列表
     * date         2020/9/15 22:04
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult listFriendsLinks();

    /**
     * description  TODO:根据友情连接id删除连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult deleteFriendsLink(String friendId);

    /**
     * description  TODO:根据友情连接id修改连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateFriendsLink(String friendId, Friends friends);
}
