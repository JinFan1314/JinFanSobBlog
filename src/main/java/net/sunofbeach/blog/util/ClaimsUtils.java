package net.sunofbeach.blog.util;

import io.jsonwebtoken.Claims;
import net.sunofbeach.blog.pojo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: SobBlogSystem
 * @description: 解析和生成token
 * @author: JinFan
 * @create: 2020-09-21 00:20
 **/
public class ClaimsUtils {

    public static final String ID = "ID";
    public static final String USER_NAME="user_name";
    public static final String ROLES="roles";
    public static final String AVATAR="avatar";
    public static final String EMAIL="email";
    public static final String SIGN="sign";


    /**
     * description  TODO:设置token
     * date         2020/9/21 0:24
     * @author      jinFan
     * @param       user:user
     * @return      java.util.Map<java.lang.String,java.lang.Object>
     */
    public static Map<String,Object> userClaims(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put(ID, user.getId());
        claims.put(USER_NAME,user.getUserName());
        claims.put(ROLES, user.getRoles());
        claims.put(AVATAR, user.getAvatar());
        claims.put(EMAIL, user.getEmail());
        claims.put(SIGN, user.getSign());
        return claims;
    }

    /**
     * description  TODO:解析token
     * date         2020/9/21 0:41
     * @author      jinFan
     * @param       claims:claims
     * @return      net.sunroof.blog.pojo.User
     */
    public static User claimsUser(Claims claims){
        User user = new User();
        String userId = (String) claims.get(ID);
        user.setId(userId);
        String user_name = (String) claims.get(USER_NAME);
        user.setUserName(user_name);
        String roles = (String) claims.get(ROLES);
        user.setRoles(roles);
        String avatar = (String) claims.get(AVATAR);
        user.setAvatar(avatar);
        String email = (String) claims.get(EMAIL);
        user.setEmail(email);
        String sign = (String) claims.get(SIGN);
        user.setSign(sign);
        return user;
    }
}
