package net.sunofbeach.blog.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: SobBlogSystem
 * @description: Cookies的工具类
 * @author: JinFan
 * @create: 2020-09-20 18:37
 **/
@Slf4j
public class CookieUtils {
    public static final int default_age = 60 * 60 * 24 * 365;

    public static final String domain = "localhost";

    /**
     * description  TODO:设置cookie
     * date         2020/9/21 0:05
     * @author      jinFan
     * @param       response:请求
     * @param       key:key
     * @param       value:值
     */
    public static void setUpCookie(HttpServletResponse response, String key, String value) {
        setUpCookie(response,key,value,default_age);
    }

    /**
     * description  TODO:设置cookie
     * date         2020/9/21 0:06
     * @author      jinFan
     * @param       response:请求
     * @param       key：key
     * @param       value:值
     * @param       age:时间
     */
    public static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /**
     * description  TODO:删除cookies
     * date         2020/9/21 0:12
     * @author      jinFan
     * @param       key:key
     * @param       response:请求
     */
    public static  void deleteCookie(String key,HttpServletResponse response){
        setUpCookie(response,key,null,0);
    }

    /**
     * description  TODO:获取cookie
     * date         2020/9/21 0:09
     * @author      jinFan
     * @param       request:接受请求
     * @param       key:key
     * @return      java.lang.String
     */
    public static String getCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        //判断cookies
        if (cookies == null) {
            log.info("cookies is null.....");
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
