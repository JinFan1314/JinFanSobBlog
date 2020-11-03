package net.sunofbeach.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: SobBlogSystem
 * @description: 检查数据
 * @author: JinFan
 * @create: 2020-09-17 13:02
 **/
public class TextUtils {

    //校验规则
    public static final  String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * description  TODO:判断是否为空
     * date         2020/9/17 13:04
     * @author      jinFan
     * @param       text:text
     * @return      boolean
     */
    public static boolean isEmpty(String text){
        return text == null || text.length()==0;
    }

    /**
     * description  TODO:邮箱的校验
     * date         2020/9/18 3:57
     * @author      jinFan
     * @param       emailAddress：邮箱地址
     * @return      boolean
     */
    public static boolean isEmailAddressOk(String emailAddress){
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }
}
