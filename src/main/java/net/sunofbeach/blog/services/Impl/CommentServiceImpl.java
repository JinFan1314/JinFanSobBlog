package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.ArticleNoContentDao;
import net.sunofbeach.blog.dao.CommentDao;
import net.sunofbeach.blog.pojo.ArticleNoContent;
import net.sunofbeach.blog.pojo.Comment;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.CommentService;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: SobBlogSystem
 * @description: CommentService的实现类
 * @author: JinFan
 * @create: 2020-11-02 16:33
 **/
@Slf4j
@Service
@Transactional
public class CommentServiceImpl extends BaseServices implements CommentService {

    @Resource
    private UserServices userServices;

    @Resource
    private ArticleNoContentDao articleNoContentDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private CommentDao commentDao;
    /**
     * description  TODO:提交评论
     * date         2020/9/15 22:18
     *
     * @param comment:评论
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult postComment(Comment comment) {
        //检查用户是否登录
        User user = userServices.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查内容
        String articleId = comment.getArticleId();
        if (TextUtils.isEmpty(articleId)) {
            return ResponseResult.FAILED("文章ID不能为空!");
        }
        ArticleNoContent article = articleNoContentDao.findOneById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        String content = comment.getContent();
        if (TextUtils.isEmpty(content)) {
            return ResponseResult.FAILED("评论内容不能为空!");
        }
        //补全内容
        comment.setId(idWorker.nextId()+"");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setUserAvatar(user.getAvatar());
        comment.setUserName(user.getUserName());
        comment.setUserId(user.getId());
        //保存入库
        commentDao.save(comment);
        //返回结果
        return ResponseResult.SUCCESS("文章评论成功!");
    }

    /**
     * description  TODO:获取文章的评论
     * <p>
     *     评论的排序策略
     *     最基本的即使按时间排序-->升序和降序--->先发表的在前面或者后发表的在前面
     *     <p>
     *         置顶的一定在最前面
     *     </p>
     *     后发表的,前单位时间内会排到前面,过了此单位时间,会按点赞量和发表时间进行排序
     * </p>
     * date         2020/9/16 16:45
     *
     * @param articleId ：文章ID
     * @param page:页码
     * @param size:每页个数
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listCommentsByArticleId(String articleId, int page, int size) {
        //检查页码和个数
        page = checkPage(page);
        size = checkSize(size);
        //分页
        Pageable pageable = PageRequest.of(page-1, size);
        //查询全部
        Page<Comment> all = commentDao.findAll(pageable);
        //返回结果
        return ResponseResult.SUCCESS("评论列表查询成功!").setDate(all);
    }

    /**
     * description  TODO:根据评论id删除评论
     * date         2020/9/15 21:51
     *
     * @param commentId :评论ID
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteCommentById(String commentId) {
        //检查用户角色
        User user = userServices.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //评论找出来,对比用户权限
        Comment comment = commentDao.findOneById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在!");
        }
        //用户id不一样,只用管理员才能删除,用户id一致,直接删除,登录了就判断角色
        if (Constants.User.ROLE_ADMIN.equals(user.getRoles())||user.getId().equals(comment.getUserId())) {
            commentDao.deleteById(commentId);
            return ResponseResult.SUCCESS("评论删除成功!");
        }else {
            return ResponseResult.PERMISSION_FORBID();
        }
    }
}
