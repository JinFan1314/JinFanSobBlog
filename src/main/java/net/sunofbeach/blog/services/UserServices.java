package net.sunofbeach.blog.services;

import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * @program: SobBlogSystem
 * @description: userService
 * @author: JinFan
 * @create: 2020-09-16 17:06
 **/
public interface UserServices {

    /**
     * description  TODO:初始化管理员账号
     * date         2020/9/16 17:08
     * @author      jinFan
     * @param       user:user
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult initManagerAccount(User user);

    /**
     * description  TODO：创建captcha
     * date         2020/9/18 3:12
     * @author      jinFan
     * @param       captchaKey：captchaKey
     * @return      void
     */
    void createCaptcha(String captchaKey) throws Exception;

    /**
     * description  TODO:发送邮件
     * date         2020/9/18 3:37
     * @author      jinFan
     * @param       emailAddress:邮件地址
     * @param       type:类型
     * @return      net.sunroof.blog.response.ResponseResult
     */
    ResponseResult sendEmail(String type, String emailAddress);

    /**
     * description  TODO:注册
     * date         2020/9/15 20:44
     * @author      jinFan
     * @param       user：user
     * @param       emailCode：emailCode
     * @param       captchaCode：captchaCode
     * @param       captchaKey：captchaKey
     * @return      net.SudoFetch.blog.response.ResponseResult
     */
    ResponseResult register(User user,String emailCode,String captchaCode,String captchaKey);

    /**
     * description  TODO:登录
     * <p>
     *     需要提交的数据
     *     1.用户账号-可以是昵称,可以是邮箱--->唯一处理
     *     2.密码
     *     3.图灵验证码
     *     4.图灵验证码的key
     * </p>
     * date         2020/9/15 20:47
     * @author      jinFan
     * @param       captcha：验证码
     * @param       captchaKey:图灵验证码的key
     * @param       user:用户
     * @return      net.SudoFetch.blog.response.ResponseResult
     */
    ResponseResult doLogin(String captcha, String captchaKey, User user);

    /**
     * description  TODO:检查用户
     * date         2020/9/21 2:22
     * @author      jinFan
     * @return      net.sunofbeach.blog.pojo.User
     */
    User checkUser();

    /**
     * description  TODO:根据用户ID获取用户信息
     * date         2020/9/21 14:26
     * @author      jinFan
     * @param       userId:用户id
     * @return      net.sunroof.blog.response.ResponseResult
     */
    ResponseResult getUserInfo(String userId);

    /**
     * description  TODO:检查邮箱
     * date         2020/9/21 14:45
     * @author      jinFan
     * @param       email:邮箱地址
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult checkEmail(String email);

    /**
     * description  TODO:检查用户名
     * date         2020/9/21 14:45
     * @author      jinFan
     * @param       userName:用户名
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult checkUserName(String userName);

    /**
     * description  TODO:根据userId修改用户信息
     * date         2020/9/15 20:59
     * @author      jinFan
     * @param       user:user
     * @param       userId:userId
     * @return      net.SudoFetch.blog.response.ResponseResult
     */
    ResponseResult updateUserInfo(String userId, User user);

    /**
     * description  TODO:删除用户
     * date         2020/9/24 19:57
     * @author      jinFan
     * @param       userId:用户id
     * @return      net.sunroof.blog.response.ResponseResult
     */
    ResponseResult deleteById(String userId);

    /**
     * description  TODO:获取用户列表
     * date         2020/9/15 21:30
     * @author      jinFan
     * @param       page:页码
     * @param       size:个数
     * @return      net.sunbath.blog.response.ResponseResult
     */
    ResponseResult listUsers(int page, int size);

    /**
     * description  TODO:根据userId修改密码
     * 普通做法:通过与旧密码对比来更新密码
     * <p>
     *     既可以找回密码,也可以修改密码
     *     发送验证码到邮箱/手机 --> 判断验证码是否正确来判断
     *     判断对应的邮箱和手机号是否属于你.
     * </p>
     * <p>
     *     步骤:
     *     1.用户填写邮箱
     *     2.用户获取验证码type=forget
     *     3.填写验证码
     *     4.填写新的验证码
     *     5.提交数据
     * </p>
     * <p>
     *     数据包括:
     *     1.邮箱和新的密码
     *     2.验证码
     * </p>
     * date         2020/9/15 20:55
     * @author      jinFan
     * @param       user:user
     * @param       verify_code:verify_code
     * @return      net.SudoFetch.blog.response.ResponseResult
     */
    ResponseResult updateUserPassword(String verify_code, User user);

    /**
     * description  TODO:更新邮箱
     * <p>
     *     1.用户已经登录
     *     2.新的邮箱没有注册过
     *     用户的步骤:
     *     1.已经登录
     *     2.输入新的邮箱
     *     3.发送验证码
     *     4.输入验证码
     *     5.提交数据
     *     <p>
     *         需要提交的数据
     *         1.新的邮箱
     *         2.验证码
     *         3.其他的用户信息可以从token中获取
     *     </p>
     * </p>
     * date         2020/9/26 10:34
     * @author      jinFan
     * @param       email:邮箱地址
     * @param       verifyCode:邮箱验证码
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult updateEmail(String email, String verifyCode);

    /**
     * description  TODO:退出登录
     * <p>
     *     拿到tokenKey
     *     1.s删除redis的token
     *     2.删除mysql里的refreshToken
     *     3.删除cookie里的token_key
     * </p>
     * date         2020/9/26 13:14
     * @author      jinFan
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    ResponseResult doLogout();
}
