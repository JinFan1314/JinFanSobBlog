package net.sunofbeach.blog.controller.admin;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.Article;
import net.sunofbeach.blog.pojo.Friends;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.FriendLinkServices;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.RedisUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: SobBlogSystem
 * @description: 友情链接api
 * @author: JinFan
 * @create: 2020-09-15 21:40
 **/
@Slf4j
@RestController
@RequestMapping("/admin/friend_link")
public class FriendLinkAdminApi {

    @Resource
    private FriendLinkServices friendLinkServices;

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
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addFriendsLink(@RequestBody Friends friends){
        return friendLinkServices.addFriendsLink(friends);
    }

    /**
     * description  TODO:根据友情连接id删除连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{friendId}")
    public ResponseResult deleteFriendsLink(@PathVariable("friendId") String friendId){
        return friendLinkServices.deleteFriendsLink(friendId);
    }

    /**
     * description  TODO:根据友情连接id修改连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{friendId}")
    public ResponseResult updateFriendsLink(@PathVariable("friendId") String friendId,@RequestBody Friends friends){
        return friendLinkServices.updateFriendsLink(friendId,friends);
    }

    /**
     * description  TODO:根据友情连接id获取连接
     * date         2020/9/15 21:51
     * @author      jinFan
     * @param       friendId:友情id
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{friendId}")
    public ResponseResult getFriendsLink(@PathVariable("friendId") String friendId){
        return friendLinkServices.getFriendsLink(friendId);
    }

    /**
     * description  TODO:获取友情连接列表
     * date         2020/9/15 22:04
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listFriendsLinks(){
        return friendLinkServices.listFriendsLinks();
    }
}
