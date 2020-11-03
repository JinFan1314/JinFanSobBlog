package net.sunofbeach.blog.controller.user;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.response.ResponseState;
import net.sunofbeach.blog.services.UserServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;


/**
 * @program: SobBlogSystem
 * @description: 用户api
 * @author: JinFan
 * @create: 2020-09-15 20:34
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserApi {

    @Resource
    private UserServices userServices;


    /**
     * description  TODO:初始账户
     * date         2020/9/15 20:38
     * @author      jinFan
     * @param       user:user
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PostMapping("/admin_account")
    public ResponseResult initManagerAccount(@RequestBody User user){
        return userServices.initManagerAccount(user);
    }

    /**
     * description  TODO:注册
     * date         2020/9/15 20:44
     * @author      jinFan
     * @param       user：user
     * @param       emailCode：emailCode
     * @param       captchaCode：captchaCode
     * @param       captchaKey：captchaKey
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PostMapping("join_in")
    public ResponseResult register(@RequestBody User user,
                                   @RequestParam("email_code") String emailCode,
                                   @RequestParam("captcha_code") String captchaCode,
                                   @RequestParam("captcha_key") String captchaKey){
        return userServices.register(user,emailCode,captchaCode,captchaKey);
    }

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
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PostMapping("/login/{captcha}/{captcha_key}")
    public ResponseResult login(@PathVariable("captcha_key") String captchaKey,
                                @PathVariable("captcha") String captcha,
                                @RequestBody User user){
        return userServices.doLogin(captcha,captchaKey,user);
    }

    /**
     * description  TODO:获取图灵验证码
     * date         2020/9/15 20:49
     * @author      jinFan
     * @param       captchaKey：验证码的key
     */
    @GetMapping("/captcha")
    public void getCaptcha(@RequestParam("captcha_key") String captchaKey){
        try {
            userServices.createCaptcha(captchaKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /*
     * description  TODO:获取邮箱验证码,
     *               <p>
                      1.使用的场景：注册，找回密码，修改邮箱（会输入新的邮箱）
                      * 注册,如果已经注册则提示用户邮箱已注册
                      * 找回密码,如果没有注册,则提示用户邮箱没有注册
                      * 修改密码,(新的邮箱),如果已经注册,提示已经注册
                      *
     *               </P>
     * date         2020/9/15 20:53
     * @author      jinFan
     * @param       emailAddress
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/verify_code")
    public ResponseResult sendVerifyCode(@RequestParam("type") String type,
                                         @RequestParam("email") String emailAddress){
        return  userServices.sendEmail(type,emailAddress);
    }

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
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PutMapping("/password/{verify_code}")
    public ResponseResult updatePassword(@RequestBody User user,@PathVariable("verify_code") String verify_code){
        return  userServices.updateUserPassword(verify_code,user);
    }

    /**
     * description  TODO:根据userId获取用户信息
     * date         2020/9/15 20:57
     * @author      jinFan
     * @param       userId:userId
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @GetMapping("/user_info/{userId}")
    public ResponseResult getUserInfo(@PathVariable("userId") String userId){
        return userServices.getUserInfo(userId);
    }

    /**
     * description  TODO:根据userId修改用户信息
     * date         2020/9/15 20:59
     * @author      jinFan
     * @param       user:user
     * @param       userId:userId
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/user_info/{userId}")
    public ResponseResult updateUserInfo(@RequestBody User user,@PathVariable("userId") String userId){
        return userServices.updateUserInfo(userId,user);
    }

    /**
     * description  TODO:获取用户列表
     * date         2020/9/15 21:30
     * @author      jinFan
     * @param       page:页码
     * @param       size:个数
     * @return      net.sunbath.blog.response.ResponseResult
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listUsers(@RequestParam("page") int page,
                                    @RequestParam("size") int size){
        return userServices.listUsers(page,size);
    }

    /**
     * description  TODO:根据userId删除user
     * <p>
     *     需要管理员权限
     * </p>
     * date         2020/9/15 21:20
     * @author      jinFan
     * @param       userId:id
     * @return      net.sunbath.blog.response.ResponseResult
     */
    @DeleteMapping("/{userId}")
    public ResponseResult deleteUser(@PathVariable("userId") String userId){
        //判断当前的用户角色
        return userServices.deleteById(userId);
    }

    /**
     * description  TODO:检查邮箱
     * date         2020/9/21 14:45
     * @author      jinFan
     * @param       email:邮箱地址
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @ApiResponses({
            @ApiResponse(code = 20000,message ="表示当前邮箱已经注册"),
            @ApiResponse(code = 40000,message ="表示当前邮箱未注册")
    })
    @GetMapping("/email")
    public ResponseResult checkEmail(@RequestParam("email") String email){
        return userServices.checkEmail(email);
    }

    /**
     * description  TODO:检查用户名
     * date         2020/9/21 14:45
     * @author      jinFan
     * @param       userName:用户名
     * @return      net.sunofbeach.blog.response.ResponseResult
     */
    @ApiResponses({
            @ApiResponse(code = 20000,message ="表示当前用户已经注册"),
            @ApiResponse(code = 40000,message ="表示当前用户未注册")
    })
    @GetMapping("/user_name")
    public ResponseResult checkUserName(@RequestParam("userName") String userName){
        return userServices.checkUserName(userName);
    }

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
    public ResponseResult updateEmail(@RequestParam("email") String email,@RequestParam("verify_code") String verifyCode){
        return userServices.updateEmail(email,verifyCode);
    }

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
    @GetMapping("/logout")
    public ResponseResult logout(){
        return userServices.doLogout();
    }

}