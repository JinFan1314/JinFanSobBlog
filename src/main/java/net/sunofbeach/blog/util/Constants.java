package net.sunofbeach.blog.util;

/**
 * @program: SobBlogSystem
 * @description: 常量
 * @author: JinFan
 * @create: 2020-09-17 13:31
 **/
public interface Constants {

    interface User {
        String ROLE_ADMIN = "role_admin";
        String ROLE_NORMAL = "role_normal";
        String DEFAULT_AVATAR = "https://cdn.sunofbeaches.com/images/default_avatar.png";
        String DEFAULT_STATE = "1";
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_CODE_CONTENT = "key_email_code__content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address";
        String KEY_TOKEN = "key_token_";
        String COOKIE_TOKE_KEY = "sob_blog_token";

    }

    interface Settings {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
        String WEB_SIZE_TITLE = "web_size_title";
        String WEB_SIZE_DESCRIPTION = "web_size_description";
        String WEB_SIZE_KEYWORDS = "web_size_keywords";
        String WEB_SIZE_VIEW_COUNT = "web_size_view_count";
    }

    interface Page {
        int DEFAULT_PAGE = 1;
        int MIN_SIZE = 5;
    }

    interface TimeValueInMillions {
        long MIN = 60 * 1000;
        long HOUR = 60 * MIN;
        long HOUR_2 = 60 * MIN * 2;
        long DAY = 24 * HOUR;
        long WEEK = 7 * DAY;
        long MONTH = 30 * DAY;
    }

    interface TimeValueInSecond {
        int MIN = 60;
        int HOUR = 60 * MIN;
        int HOUR_2 = 60 * MIN * 2;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
    }

    //图片格式
    interface ImageType {
        String PREFIX = "image/";
        String TYPE_JPG = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPEG = "jpeg";
        //
        String TYPE_JPG_WITH_PREFIX = PREFIX + "jpg";
        String TYPE_PNG_WITH_PREFIX = PREFIX + "png";
        String TYPE_GIF_WITH_PREFIX = PREFIX + "gif";
        String TYPE_JPEG_WITH_PREFIX = PREFIX + "jpeg";
    }

    interface Article {
        int TITLE_MAX_LENGTH = 120;
        int SUMMARY_MAX_LENGTH = 256;
        //0表示删除,1.表示已经发布,2.表示草稿,3.表示置顶
        String STATE_DELETE = "0";
        String STATE_PUBLISH = "1";
        String STATE_DRAFT  = "2";
        String STATE_TOP = "3";
    }
}
