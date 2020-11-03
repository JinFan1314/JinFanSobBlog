package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.SettingsDao;
import net.sunofbeach.blog.pojo.Settings;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.WebSizeService;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SobBlogSystem
 * @description: WebSizeService的实现类
 * @author: JinFan
 * @create: 2020-09-29 15:48
 **/
@Slf4j
@Service
@Transactional
public class WebSizeServiceImpl extends BaseServices implements WebSizeService {

    @Resource
    private IdWorker idWorker;

    @Resource
    private SettingsDao settingsDao;

    /*
     * description  TODO:获取网站标题
     * date         2020/9/16 14:47
     * @author      jinFan
     * @return      net.sunbeam.blog.response.ResponseResult
     */
    @Override
    public ResponseResult getWebSizeTitle() {
        Settings settings = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        if (settings == null) {
            return ResponseResult.FAILED("");
        }
        return ResponseResult.SUCCESS("获取网站title成功!").setDate(settings);
    }

    /**
     * description  TODO:修改网站的标题
     * date         2020/9/16 16:21
     *
     * @param title :网站的标题
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateWebSizeTitle(String title) {
        //潘孔
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("title不能为空!");
        }
        //查找
        Settings titleFormDb = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        //判断
        if (titleFormDb==null) {
            titleFormDb = new Settings();
            titleFormDb.setId(idWorker.nextId()+"");
            titleFormDb.setUpdateTime(new Date());
            titleFormDb.setCreateTime(new Date());
            titleFormDb.setKey(Constants.Settings.WEB_SIZE_TITLE);
            //保存到数据库
            settingsDao.save(titleFormDb);
        }
        titleFormDb.setValue(title);
        //返回结果
        return ResponseResult.SUCCESS("网站title更新成功!");
    }

    /**
     * description  TODO:获取网站的seo
     * date         2020/9/16 16:22
     *
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getSeoInfo() {
        Settings description = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        Settings keywords = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        Map<String,String> result = new HashMap<>();
        result.put(description.getKey(),description.getValue());
        result.put(keywords.getKey(),keywords.getValue());
        return ResponseResult.SUCCESS("获取seo信息成功").setDate(result);
    }

    /**
     * description  TODO:提交网站的seo
     * date         2020/9/16 16:22
     *
     * @param keywords    ：keywords
     * @param description ：description
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        //判空
        if (TextUtils.isEmpty(keywords)||TextUtils.isEmpty(description)) {
            return ResponseResult.FAILED("关键字和描述不能为空!");
        }
        Settings descriptionFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        if (descriptionFromDb == null) {
            descriptionFromDb = new Settings();
            descriptionFromDb.setId(idWorker.nextId()+"");
            descriptionFromDb.setCreateTime(new Date());
            descriptionFromDb.setUpdateTime(new Date());
            descriptionFromDb.setKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        }
        descriptionFromDb.setValue(description);
        //保存到数据库
        settingsDao.save(descriptionFromDb);
        Settings keywordsFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        if (keywordsFromDb == null) {
            keywordsFromDb = new Settings();
            keywordsFromDb.setId(idWorker.nextId()+"");
            keywordsFromDb.setCreateTime(new Date());
            keywordsFromDb.setUpdateTime(new Date());
            keywordsFromDb.setKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        }
        keywordsFromDb.setValue(keywords);
        //保存到数据库
        settingsDao.save(keywordsFromDb);
        //返回结果
        return ResponseResult.SUCCESS("更新seo成功!");
    }

    /**
     * description  TODO:获取网站统计数
     * <p>
     *     添加拦截器,这里只统计浏览量(文章的)
     * </p>
     * date         2020/9/16 16:27
     *
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getWebSizeViewCount() {
        Settings viewCountFromDb = settingsDao.findOneByKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        if (viewCountFromDb == null) {
            viewCountFromDb = new Settings();
            viewCountFromDb.setId(idWorker.nextId()+"");
            viewCountFromDb.setKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
            viewCountFromDb.setCreateTime(new Date());
            viewCountFromDb.setUpdateTime(new Date());
            viewCountFromDb.setValue("1");
            settingsDao.save(viewCountFromDb);
        }
        Map<String,Integer> result = new HashMap<>();
        result.put(viewCountFromDb.getKey(),Integer.valueOf(viewCountFromDb.getValue()));
        return ResponseResult.SUCCESS("获取成功!").setDate(result);
    }
}
