package net.sunofbeach.blog.services.Impl;

import com.wf.captcha.base.Captcha;
import net.sunofbeach.blog.util.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @program: SobBlogSystem
 * @description: 基础Services
 * @author: JinFan
 * @create: 2020-09-28 16:41
 **/
public class BaseServices {

    /**
     * description  TODO:检查页码
     * date         2020/9/28 16:43
     *
     * @param page:页码
     * @return int
     * @author jinFan
     */
    public int checkPage(int page) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = Constants.Page.DEFAULT_PAGE;
        }
        return page;
    }


    /**
     * description  TODO:检查每页个数
     * date         2020/9/28 16:45
     * @author      jinFan
     * @param       size:每页个数
     * @return      int
     */
    public int checkSize(int size) {
        if (size < Constants.Page.MIN_SIZE) {
            size = Constants.Page.MIN_SIZE;
        }
        return size;
    }

    /**
     * description  TODO:获取request
     * date         2020/9/30 22:48
     * @author      jinFan
     * @return      javax.servlet.http.HttpServletRequest
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }

    /**
     * description  TODO:获取response
     * date         2020/9/30 22:48
     * @author      jinFan
     * @return      javax.servlet.http.HttpServletResponse
     */
    public HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getResponse();
    }
    //
    /**
     * description  TODO:验证码肢体
     * date         2020/9/18 2:41
     *
     * @author: jinFan
     */
    public static final int[] captcha_font_types = {Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10};
}
