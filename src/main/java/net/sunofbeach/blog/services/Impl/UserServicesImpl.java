package net.sunofbeach.blog.services.Impl;

import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.RefreshTokenDao;
import net.sunofbeach.blog.dao.SettingsDao;
import net.sunofbeach.blog.dao.UserDao;
import net.sunofbeach.blog.dao.UserNoPasswordDao;
import net.sunofbeach.blog.pojo.RefreshToken;
import net.sunofbeach.blog.pojo.Settings;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.pojo.UserNoPassword;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.response.ResponseState;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @program: SobBlogSystem
 * @description: UserServices的实现类
 * @author: JinFan
 * @create: 2020-09-16 17:09
 **/
@Slf4j
@Service
@Transactional
public class UserServicesImpl extends BaseServices implements UserServices {

    @Resource
    private UserDao userDao;

    @Resource
    private SettingsDao settingsDao;

    @Resource
    private IdWorker idWorker;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private Random random;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TaskService taskService;

    @Resource
    private RefreshTokenDao refreshTokenDao;

    @Resource
    private Gson gson;

    @Resource
    private UserNoPasswordDao userNoPasswordDao;

    /**
     * description  TODO:初始化管理员账号
     * date         2020/9/16 17:08
     *
     * @param user:user
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult initManagerAccount(User user) {
        //检查是否有初始化
        Settings managerAccountSate = settingsDao.findOneByKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        if (managerAccountSate != null) {
            return ResponseResult.FAILED("管理员账号已经初始化");
        }
        //TODO:
        //检查数据
        if (TextUtils.isEmpty(user.getUserName())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmpty(user.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmpty(user.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空");
        }
        //补全数据
        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(idWorker.nextId() + "");
        user.setRoles(Constants.User.ROLE_ADMIN);
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setState(Constants.User.DEFAULT_STATE);
        String remoteAddr = getRequest().getRemoteAddr();
        String localAddr = getRequest().getLocalAddr();
        log.info("remoteAddr==>" + remoteAddr);
        log.info("localAddr==>" + localAddr);
        user.setLoginIp(remoteAddr);
        user.setRegIp(remoteAddr);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //保存到数据库
        userDao.save(user);
        //更新标记
        Settings settings = new Settings();
        settings.setId(idWorker.nextId() + "");
        settings.setKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        settings.setCreateTime(new Date());
        settings.setUpdateTime(new Date());
        settings.setValue("1");
        settingsDao.save(settings);
        return ResponseResult.SUCCESS("初始化成功");
    }

    /**
     * description  TODO：创建captcha
     * date         2020/9/18 3:12
     *
     * @param captchaKey：captchaKey
     * @author jinFan
     */
    @Override
    public void createCaptcha(String captchaKey) throws Exception {
        if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }
        // 设置请求头为输出图片类型
        getResponse().setContentType("image/gif");
        getResponse().setHeader("Pragma", "No-cache");
        getResponse().setHeader("Cache-Control", "no-cache");
        getResponse().setDateHeader("Expires", 0);
        int captchaType = random.nextInt(3);
        Captcha targetCaptcha;
        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            targetCaptcha = new SpecCaptcha(200, 60, 5);
        } else if (captchaType == 1) {
            //gif使用
            targetCaptcha = new GifCaptcha(200, 60);
        } else {
            // 算术类型
            targetCaptcha = new ArithmeticCaptcha(200, 60);
            targetCaptcha.setLen(2);  // 几位数运算，默认是两位
        }
        // 设置字体
        int index = random.nextInt(captcha_font_types.length);
        log.info("captcha font type ==>" + index);
        targetCaptcha.setFont(captcha_font_types[index]);
        targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        String content = targetCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        //保存到redis里面
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);
        targetCaptcha.out(getResponse().getOutputStream());
    }

    /**
     * description  TODO:发送邮件
     * <p>
     * 1.使用的场景：注册，找回密码，修改邮箱（会输入新的邮箱）
     * 2.注册(register),如果已经注册则提示用户邮箱已注册
     * 3.找回密码(forget),如果没有注册,则提示用户邮箱没有注册
     * 4.修改密码(update),(新的邮箱),如果已经注册,提示已经注册
     * </P>
     * date         2020/9/18 3:37
     *
     * @param emailAddress:邮件地址
     * @return net.sunroof.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult sendEmail( String type, String emailAddress) {
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不能为空");
        }
        //根据类型查询邮箱是否存在
        if ("register".equals(type) || "update".equals(type)) {
            User userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail != null) {
                return ResponseResult.FAILED("该邮箱已注册");
            }
        } else if ("forget".equals(type)) {
            User userByEmail = userDao.findOneByEmail(emailAddress);
            if (userByEmail == null) {
                return ResponseResult.FAILED("该邮箱未注册");
            }
        }
        //1.防止重复发送，同一个ip只能最多只能发送10次，30秒发送一次。
        String remoteAddr = getRequest().getRemoteAddr();
        log.info("sendEmail==> ip==>" + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "-");
        }
        //拿出来，如果没有，那就过了
        Integer ipSendTime = (Integer) redisUtils.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTime != null && ipSendTime > 10) {
            return ResponseResult.FAILED("您发送验证码太频繁了吧");
        }
        Object hasEmailSend = redisUtils.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("您发送验证码太频繁了吧");
        }
        //2.检查邮件地址是否正确
        boolean emailAddressOk = TextUtils.isEmailAddressOk(emailAddress);
        if (!emailAddressOk) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        log.info("code==>" + code);
        //3.发送验证码6位数,10000-99999
        try {
            // EmailSender.sendRegisterVerifyCode(String.valueOf(code), emailAddress);
            taskService.sendEmailVerifyCode(String.valueOf(code), emailAddress);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败,请稍后重试!");
        }
        //4.做记录
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;
        //一个小时
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr, ipSendTime, 60 * 60);
        redisUtils.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
        //保存code,10分钟有效
        redisUtils.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
        return ResponseResult.SUCCESS("验证码发送成功!");
    }

    /**
     * description  TODO:注册
     * date         2020/9/18 17:20
     *
     * @param user：user
     * @param emailCode：emailCode
     * @param captchaCode：captchaCode
     * @param captchaKey：captchaKey
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult register(User user, String emailCode, String captchaCode, String captchaKey) {
        //第一步:检查当前用户是否为空
        String userName = user.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        //第二步:检查当前用户是否注册
        User oneByUserName = userDao.findOneByUserName(userName);
        if (oneByUserName != null) {
            return ResponseResult.FAILED("用户名已经存在!");
        }
        //第三步:检查邮箱是否未为空
        String email = user.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不能为空!");
        }
        //第四步:邮箱格式是否正确
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱格式不正确!");
        }
        //第五步:判断邮箱是否已经注册
        User userFormDbByEmail = userDao.findOneByEmail(email);
        if (userFormDbByEmail != null) {
            return ResponseResult.FAILED("该邮箱已经被注册了!");
        }
        //第六步:检查邮箱验证码是否正确
        String emailVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码无效!");
        }
        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确!");
        } else {
            //正确:删除内容
            redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        }
        //第七步:检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码不正确");
        } else {
            redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //第八步:对密码进行加密
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不能空");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //第八步:补全数据
        user.setId(idWorker.nextId() + "");
        String ipAddress = getRequest().getRemoteAddr();
        user.setRegIp(ipAddress);
        user.setLoginIp(ipAddress);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setState("1");
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setRoles(Constants.User.ROLE_NORMAL);
        //第九步:保存数据到数据库
        userDao.save(user);
        //第十步:返回结果
        return ResponseResult.GET(ResponseState.JOIN_IN_SUCCESS);
    }

    /**
     * description  TODO:登录
     * <p>
     * 需要提交的数据
     * 1.用户账号-可以是昵称,可以是邮箱--->唯一处理
     * 2.密码
     * 3.图灵验证码
     * 4.图灵验证码的key
     * </p>
     * date         2020/9/15 20:47
     *
     * @param captcha：验证码
     * @param captchaKey:图灵验证码的key
     * @param user:用户
     * @return net.SudoFetch.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult doLogin(String captcha, String captchaKey, User user) {
        String captchaValue = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(captchaValue)) {
            return ResponseResult.FAILED("人类验证码不正确");
        }
        //验证成功删除
        redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        String userName = user.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        String password = user.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不能为空");
        }
        User userFromDb = userDao.findOneByUserName(userName);
        if (userFromDb == null) {
            userFromDb = userDao.findOneByEmail(userName);
        }
        if (userFromDb == null) {
            return ResponseResult.FAILED("用户名或密码错误");
        }
        //用户名存在
        //对比密码
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("用户名或密码错误");
        }
        //密码正确
        //判断用户状态,如果是非正常状态,则返回结果
        if (!"1".equals(userFromDb.getState())) {
            return ResponseResult.FAILED("当前账号已被禁止!");
        }
        createToken(userFromDb);
        return ResponseResult.LOGIN_SUCCESS();
    }

    /**
     * description  TODO:创建token
     * date         2020/9/21 2:48
     *
     * @param userFromDb:user
     * @return String
     * @author jinFan
     */
    private String createToken(User userFromDb) {
        //删除refreshToken的记录
        int deleteResult = refreshTokenDao.deleteAllByUserId(userFromDb.getId());
        log.info("deleteResult of refresh token ===>" + deleteResult);
        //TODO:生成token
        Map<String, Object> claims = ClaimsUtils.userClaims(userFromDb);
        //默认为2个小时
        String token = JwtUtil.createToken(claims);
        //返回token的MD5值,token会保存到redis里
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存到redis里,有效期为3个小时,key是tokenKey
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValueInMillions.HOUR_2);
        //把toToken写到cookies里
        //动态获取,可以从request里获取
        CookieUtils.setUpCookie(getResponse(), Constants.User.COOKIE_TOKE_KEY, tokenKey);
        //TODO:生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.TimeValueInMillions.MONTH);
        //TODO:保存到数据库
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(userFromDb.getId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        return tokenKey;
    }

    /**
     * description  TODO:通过token_key检查用户是否登录,如果登录了就返回用户信息
     * date         2020/9/21 2:22
     *
     * @return net.sunbeam.blog.pojo.User
     * @author jinFan
     */
    @Override
    public User checkUser() {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        User user = parseByTokenKey(tokenKey);
        if (user == null) {
            //说明token过期
            //1.去mysql数据库查询refreshToken
            RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            //2.如果不存在,就是当前用户没有登录,提示用户登录
            if (refreshToken == null) {
                return null;
            }
            //3.如果存在,就解析refreshToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //5.如果refreshToken有效,创建新的token,和新的refreshToken
                String userId = refreshToken.getUserId();
                User userFromDb = userDao.findOneById(userId);
                //创建新的refreshToken
                String newTokenKey = createToken(userFromDb);
                return parseByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //4.如果refreshToken过期了,就是当前用户没有登录,提示用户登录
                return null;
            }
        }
        return user;
    }

    /**
     * description  TODO:根据用户ID获取用户信息
     * date         2020/9/21 14:26
     *
     * @param userId:用户id
     * @return net.sunroof.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getUserInfo(String userId) {
        //从数据库里获取
        User user = userDao.findOneById(userId);
        //判断结果
        if (user == null) {
            //如果不存在,就返回不存在
            return ResponseResult.FAILED("用户不存在");
        }
        //如果存在,就复制对象,清空数据
        String userGson = gson.toJson(user);
        User newUser = gson.fromJson(userGson, User.class);
        newUser.setPassword("");
        newUser.setEmail("");
        newUser.setRegIp("");
        newUser.setLoginIp("");
        //返回数据
        return ResponseResult.SUCCESS("获取数据成功").setDate(newUser);
    }

    /**
     * description  TODO:检查邮箱
     * date         2020/9/21 14:45
     *
     * @param email:邮箱地址
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址为空");
        }
        User user = userDao.findOneByEmail(email);
        return user == null ? ResponseResult.FAILED("邮箱未注册") : ResponseResult.SUCCESS("该邮箱已经注册");
    }

    /**
     * description  TODO:检查用户名
     * date         2020/9/21 14:45
     *
     * @param userName:用户名
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名为空");
        }
        User user = userDao.findOneByUserName(userName);
        return user == null ? ResponseResult.FAILED("用户未注册") : ResponseResult.SUCCESS("该用户已经注册");
    }

    /**
     * description  TODO:根据userId修改用户信息
     * date         2020/9/15 20:59
     *
     * @param user:user
     * @param userId:userId
     * @return net.SudoFetch.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateUserInfo(String userId, User user) {
        //检查用户
        User userFromTokenKey = checkUser();
        if (userFromTokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        User userFromDb = userDao.findOneById(userFromTokenKey.getId());
        //判断id是否一致
        if (!userFromDb.getId().equals(userId)) {
            return ResponseResult.PERMISSION_FORBID();
        }
        //可以进行修改
        //用户名
        String userName = user.getUserName();
        if (!TextUtils.isEmpty(userName)) {
            User userByUserName = userDao.findOneByUserName(userName);
            if (userByUserName != null) {
                return ResponseResult.FAILED("用户名已存在!");
            }
            userFromDb.setUserName(userName);
        }
        //头像
        if (!TextUtils.isEmpty(user.getAvatar())) {
            userFromDb.setAvatar(user.getAvatar());
        }
        //签名
        userFromDb.setSign(user.getSign());
        //UpdateTime
        userFromDb.setUpdateTime(new Date());
        userDao.save(userFromDb);
        //干掉redis里的token
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        redisUtils.del(tokenKey);
        return ResponseResult.SUCCESS("用户信息修改成功!");
    }

    /**
     * description  TODO:删除用户,并不是真的删除,管理员权限
     * date         2020/9/24 19:57
     *
     * @param userId:用户id
     * @return net.sunroof.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteById(String userId) {
        //可以删除
        int result = userDao.deleteUserByState(userId);

        if (result > 0) {
            return ResponseResult.SUCCESS("删除成功!");
        }
        return ResponseResult.FAILED("用户不存在!");
    }

    /**
     * description  TODO:获取用户列表
     * date         2020/9/15 21:30
     *
     * @param page:页码
     * @param size:个数
     * @return net.sunbath.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listUsers(int page, int size) {
        //可以获取列表
        //分页
        page = checkPage(page);
        size = checkSize(size);
        //根据注册日期排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<UserNoPassword> all = userNoPasswordDao.findAll(pageable);
        return ResponseResult.SUCCESS("获取用户列表成功!").setDate(all);
    }

    /**
     * description  TODO:根据userId修改密码
     * 普通做法:通过与旧密码对比来更新密码
     * <p>
     * 既可以找回密码,也可以修改密码
     * 发送验证码到邮箱/手机 --> 判断验证码是否正确来判断
     * 判断对应的邮箱和手机号是否属于你.
     * </p>
     * <p>
     * 步骤:
     * 1.用户填写邮箱
     * 2.用户获取验证码type=forget
     * 3.填写验证码
     * 4.填写新的验证码
     * 5.提交数据
     * </p>
     * <p>
     * 数据包括:
     * 1.邮箱和新的密码
     * 2.验证码
     * </p>
     * date         2020/9/15 20:55
     *
     * @param user:user
     * @param verifyCode:verifyCode
     * @return net.SudoFetch.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateUserPassword(String verifyCode, User user) {
        //检查邮箱
        String email = user.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱不能为空!");
        }
        //那redis里的验证
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (redisVerifyCode == null || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误");
        }
        //干掉
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        int result = userDao.updateUserPasswordByEmail(password, email);
        //修改密码
        return result > 0 ? ResponseResult.SUCCESS("密码修改成功!") : ResponseResult.FAILED("密码修改失败!");
    }

    /**
     * description  TODO:更新邮箱
     * <p>
     * 1.用户已经登录
     * 2.新的邮箱没有注册过
     * 用户的步骤:
     * 1.已经登录
     * 2.输入新的邮箱
     * 3.发送验证码
     * 4.输入验证码
     * 5.提交数据
     * <p>
     * 需要提交的数据
     * 1.新的邮箱
     * 2.验证码
     * 3.其他的用户信息可以从token中获取
     * </p>
     * </p>
     * date         2020/9/26 10:34
     *
     * @param email:邮箱地址
     * @param verifyCode:邮箱验证码
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateEmail(String email, String verifyCode) {
        //1.确保登陆了
        User user = this.checkUser();
        //没有登录
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //对比验证码,确保新的邮箱是属于当前用户的
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(redisVerifyCode) || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误!");
        }
        //删除redis里的
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        //可以修改
        int result = userDao.updateEmailById(email, user.getId());
        //返回结果
        return result > 0 ? ResponseResult.SUCCESS("邮箱修改成功!") : ResponseResult.FAILED("邮箱修改失败!");
    }

    /**
     * description  TODO:退出登录
     * <p>
     * 拿到tokenKey
     * 1.s删除redis的token
     * 2.删除mysql里的refreshToken
     * 3.删除cookie里的token_key
     * </p>
     * date         2020/9/26 13:14
     *
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult doLogout() {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        if (TextUtils.isEmpty(tokenKey)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //删除redis里的token
        redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);
        //删除mysql里的refreshToken
        refreshTokenDao.deleteAllByTokenKey(tokenKey);
        //删除cookie
        CookieUtils.deleteCookie(Constants.User.COOKIE_TOKE_KEY, getResponse());
        //返回结果
        return ResponseResult.SUCCESS("退出成功!");
    }

    /**
     * description  TODO:解析token
     * date         2020/9/21 3:18
     *
     * @param tokenKey:tokenKey
     * @return net.sunbeam.blog.pojo.User
     * @author jinFan
     */
    private User parseByTokenKey(String tokenKey) {
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
        //说明有token
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claimsUser(claims);
            } catch (Exception e) {
                log.info("parseByTokenKey ==>" + tokenKey + "过期了!");
                return null;
            }
        }
        return null;
    }


}
