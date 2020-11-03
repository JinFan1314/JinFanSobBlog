package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.LooperDao;
import net.sunofbeach.blog.pojo.Category;
import net.sunofbeach.blog.pojo.Looper;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.LooperService;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: LooperService的实现类
 * @author: JinFan
 * @create: 2020-09-29 14:52
 **/
@Slf4j
@Service
@Transactional
public class LooperServiceImpl extends BaseServices implements LooperService {

    @Resource
    private IdWorker idWorker;

    @Resource
    private LooperDao looperDao;

    @Resource
    private UserServices userServices;

    /**
     * description  TODO:添加轮播图
     * date         2020/9/15 21:48
     *
     * @param looper :轮播图
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult addLooper(Looper looper) {
        //检查数据
        Looper oneByTitle = looperDao.findOneByTitle(looper.getTitle());
        if (oneByTitle != null) {
            return ResponseResult.FAILED("轮播图已存在!");
        }
        //title
        String title = looper.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("title不能为空!");
        }
        //imageUrl
        String imageUrl = looper.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            return ResponseResult.FAILED("图片路径不能为空!");
        }
        //targetUrl
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmpty(targetUrl)) {
            return ResponseResult.FAILED("跳转连接不能为空");
        }
        //补全数据
        looper.setId(idWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        //保存数据
        looperDao.save(looper);
        //返回结果
        return ResponseResult.SUCCESS("轮播图保存成功!");
    }

    /**
     * description  TODO:获取轮播图列表
     * date         2020/9/15 22:02
     *
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listLooper() {
        //创建条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //判断用户角色,普通用户,未登录的用户:只能获取正常的category,管理员可以获取所有
        User user = userServices.checkUser();
        List<Looper> looperAll ;
        if (user == null || Constants.User.ROLE_ADMIN.equals(user.getRoles())) {
            //只能获取正常的category
            looperAll = looperDao.listLooperByState("1");
        }else {
            //查询:管理员可以获取所有
            looperAll = looperDao.findAll(sort);
        }
        //返回结果
        return ResponseResult.SUCCESS("查询成功!").setDate(looperAll);
    }

    /**
     * description  TODO:根据轮播id删除轮播图,并不是真正的删除知识修改状态
     * date         2020/9/15 21:51
     *
     * @param looperId :轮播id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteLooper(String looperId) {
        //判断looper是否存在
        Looper looper = looperDao.findOneById(looperId);
        //判断是否为空
        if (looper == null) {
            return ResponseResult.FAILED("轮播图不存在!");
        }
        //存在就删除
        int result = looperDao.deleteLooperByUpdateState(looperId);
        //返回结果
        return result>0?ResponseResult.SUCCESS("删除成功!"):ResponseResult.FAILED("删除失败!");
    }

    /**
     * description  TODO:根据轮播图id获取修轮播图
     * date         2020/9/15 21:51
     *
     * @param looperId :轮播图id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getLooper(String looperId) {
        //查找looper
        Looper looper = looperDao.findOneById(looperId);
        //判断是否为空
        if (looper == null) {
            return ResponseResult.FAILED("轮播图不存在!");
        }
        return ResponseResult.SUCCESS("查询成功!").setDate(looper);
    }

    /**
     * description  TODO:根据轮播图id修改轮播图
     * date         2020/9/15 21:51
     *
     * @param looperId :轮播图id
     * @param looper:looper
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateLooper(String looperId, Looper looper) {
        //找出来
        Looper looperFormDb = looperDao.findOneById(looperId);
        //判空
        if (looperFormDb == null) {
            return ResponseResult.FAILED("轮播图不存在!");
        }
        //title
        String title = looper.getTitle();
        if (!TextUtils.isEmpty(title)) {
            looperFormDb.setTitle(title);
        }
        //imageUrl
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            looperFormDb.setImageUrl(imageUrl);
        }
        //targetUrl
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmpty(targetUrl)) {
            looperFormDb.setTargetUrl(targetUrl);
        }
        //state
        if (!TextUtils.isEmpty(looper.getState())) {
            looperFormDb.setState(looper.getState());
        }
        //order
        looperFormDb.setOrder(looper.getOrder());
        //updateTime
        looperFormDb.setUpdateTime(looper.getUpdateTime());
        //保存
        looperDao.save(looperFormDb);
        //返回结果
        return ResponseResult.SUCCESS("修改成功!");
    }
}
