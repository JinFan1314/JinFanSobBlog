package net.sunofbeach.blog.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @program: SobBlogSystem
 * @description: 权限访问处理
 * @author: JinFan
 * @create: 2020-09-25 20:19
 **/
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
        registry.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/405"));
        registry.addErrorPages(new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, "/504"));
        registry.addErrorPages(new ErrorPage(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "/505"));

    }
}
