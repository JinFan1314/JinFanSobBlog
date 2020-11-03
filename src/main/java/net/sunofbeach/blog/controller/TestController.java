package net.sunofbeach.blog.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.CommentDao;
import net.sunofbeach.blog.pojo.Comment;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.util.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: SobBlogSystem
 * @description: 测试Controller
 * @author: JinFan
 * @create: 2020-09-15 12:35
 **/
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private IdWorker idWorker;

    @Resource
    private CommentDao commentDao;

    /**
     * description  TODO:测试helloWorld
     * date         2020/9/15 12:46
     * @author      jinFan
     * @return      java.lang.String
     */
    @RequestMapping("/hello")
    public ResponseResult helloWorld(){
        log.info("hello world.......");
        String value = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + "123456");
        log.info("value ==>"+value);
        return ResponseResult.SUCCESS().setDate("hello world! and springboot!");
    }

    /**
     * description  TODO:获取图灵验证码
     * date         2020/9/18 0:06
     * @author      jinFan
     * @param       response:接受请求
     */
    //http://localhost:2020/test/captcha
    @RequestMapping("/captcha")
    public void captcha(HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        // specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字、纯字母、字母数字混合
        //specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = specCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        // 验证码存入session
        //request.getSession().setAttribute("captcha", content);
        //保存到redis里面
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT+"123456",content,60*10);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @PostMapping("/comment")
    public ResponseResult testComment(@RequestBody  Comment comment, HttpServletRequest request){
        String content = comment.getContent();
        log.info("content === >"+content);
        //还得知道是谁的评论,对这个评论.身份进行确认
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        if (tokenKey == null) {
            return ResponseResult.FAILED("账号未登录");
        }
        //token
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN+tokenKey);

        if (token == null) {
            //空的话就可能过期了
            //TODO:
            return null;
        }
        //已经登录,解析token
        Claims claims=null;

        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //空的话就可能过期了
            //TODO:
        }
        if (claims == null) {
            return ResponseResult.FAILED("账号未登录");
        }
        User user = ClaimsUtils.claimsUser(claims);
        comment.setUserId(user.getId());
        comment.setUserAvatar(user.getAvatar());
        comment.setUserName(user.getUserName());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setId(idWorker.nextId()+"");
        commentDao.save(comment);
        return ResponseResult.SUCCESS("评论成功!");
    }
}
