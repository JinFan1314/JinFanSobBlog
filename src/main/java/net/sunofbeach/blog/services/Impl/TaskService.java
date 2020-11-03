package net.sunofbeach.blog.services.Impl;

import net.sunofbeach.blog.util.EmailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @program: SobBlogSystem
 * @description: 邮件异步发送
 * @author: JinFan
 * @create: 2020-09-18 14:47
 **/
@Service
public class TaskService {
    @Async
    public void sendEmailVerifyCode(String verifyCode, String emailAddress) throws Exception {
        EmailSender.sendRegisterVerifyCode(verifyCode, emailAddress);
    }
}
