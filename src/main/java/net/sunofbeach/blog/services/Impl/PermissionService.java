package net.sunofbeach.blog.services.Impl;

import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.CookieUtils;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: SobBlogSystem
 * @description: 权限控制
 * @author: JinFan
 * @create: 2020-09-25 19:56
 **/
@Service("permission")
public class PermissionService {

    @Resource
    private UserServices userServices;

    /**
     * description  TODO:判断是否是管理员
     * date         2020/9/25 19:58
     * @author      jinFan
     * @return      boolean
     */
    public boolean admin(){
        //拿到request,response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String cookie = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        //没有令牌
        if (TextUtils.isEmpty(cookie)) {
            return false;
        }
        User user = userServices.checkUser();
        if (user == null) {
            return  false;
        }
        //管理员
        return Constants.User.ROLE_ADMIN.equals(user.getRoles());
    }
}
