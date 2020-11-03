package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.FriendLinkDao;
import net.sunofbeach.blog.pojo.Friends;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.FriendLinkServices;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: FriendLinkServices的实现类
 * @author: JinFan
 * @create: 2020-09-26 17:44
 **/
@Slf4j
@Service
@Transactional
public class FriendLinkServicesImpl extends BaseServices implements FriendLinkServices {

    @Resource
    private IdWorker idWorker;

    @Resource
    private FriendLinkDao friendLinkDao;

    @Resource
    private UserServices userServices;

    /**
     * description  TODO;添加友情连接
     * <p>
     * 需要管理员权限
     * </P>
     * date         2020/9/15 22:06
     *
     * @param friends:友情连接
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult addFriendsLink(Friends friends) {
        //检查连接是否存在
        Friends friendLinkDaoOneByName = friendLinkDao.findOneByName(friends.getName());
        if (friendLinkDaoOneByName != null) {
            return ResponseResult.FAILED("链接已经存在!");
        }
        //检查数据
        //名称
        String name = friends.getName();
        if (TextUtils.isEmpty(name)) {
            return ResponseResult.FAILED("名称不能为空!");
        }
        //logo
        String logo = friends.getLogo();
        if (TextUtils.isEmpty(logo)) {
            return ResponseResult.FAILED("logo不能为空!");
        }
        //url
        String url = friends.getUrl();
        if (TextUtils.isEmpty(url)) {
            return ResponseResult.FAILED("连接不能为空!");
        }

        friends.setId(idWorker.nextId() + "");
        friends.setState("1");
        friends.setCreateTime(new Date());
        friends.setUpdateTime(new Date());
        friendLinkDao.save(friends);
        return ResponseResult.SUCCESS("友情链接添加成功!");
    }

    /**
     * description  TODO:根据友情连接id获取连接
     * date         2020/9/15 21:51
     *
     * @param friendId:友情id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getFriendsLink(String friendId) {
        Friends friends = friendLinkDao.findOneById(friendId);
        if (friends == null) {
            return ResponseResult.FAILED("连接不存在!");
        }
        return ResponseResult.SUCCESS("查询成功!").setDate(friends);
    }

    /**
     * description  TODO:获取友情连接列表
     * date         2020/9/15 22:04
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listFriendsLinks() {
        //根据注册日期排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //判断用户角色,普通用户,未登录的用户:只能获取正常的category,管理员可以获取所有
        User user = userServices.checkUser();
        List<Friends> all;
        if (user == null || Constants.User.ROLE_ADMIN.equals(user.getRoles())) {
            //只能获取正常的category
            all = friendLinkDao.listFriendLinkByState("1");
        }else {
            //查询:管理员可以获取所有
            all = friendLinkDao.findAll(sort);
        }
        //返回结果
        return ResponseResult.SUCCESS("获取友情链接列表成功!").setDate(all);
    }

    /**
     * description  TODO:根据友情连接id删除连接
     * date         2020/9/15 21:51
     *
     * @param friendId:友情id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteFriendsLink(String friendId) {
        int result = friendLinkDao.deleteAllById(friendId);
        return result > 0 ? ResponseResult.SUCCESS("删除成功!") : ResponseResult.FAILED("删除失败!");
    }

    /**
     * description  TODO:根据友情连接id修改连接
     * <p>
     * 名称,url,logo
     * </p>
     * date         2020/9/15 21:51
     *
     * @param friendId:友情id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateFriendsLink(String friendId, Friends friends) {
        Friends friendFromDb = friendLinkDao.findOneById(friendId);
        if (friendFromDb == null) {
            return ResponseResult.FAILED("更新失败");
        }
        //name
        String name = friends.getName();
        if (!TextUtils.isEmpty(name)) {
            friendFromDb.setName(name);
        }
        //url
        String url = friends.getUrl();
        if (!TextUtils.isEmpty(url)) {
            friendFromDb.setName(url);
        }
        //logo
        String logo = friends.getLogo();
        if (!TextUtils.isEmpty(logo)) {
            friendFromDb.setName(logo);
        }
        //order
        friendFromDb.setOrder(friends.getOrder());
        //updateTime
        friendFromDb.setUpdateTime(new Date());
        //保存到数据库
        friendLinkDao.save(friendFromDb);
        //返回结果
        return ResponseResult.SUCCESS("友情链接修改成功");
    }
}
