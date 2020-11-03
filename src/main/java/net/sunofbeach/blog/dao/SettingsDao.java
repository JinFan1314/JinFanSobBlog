package net.sunofbeach.blog.dao;

import net.sunofbeach.blog.pojo.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: SobBlogSystem
 * @description: settingsDao
 * @author: JinFan
 * @create: 2020-09-17 13:46
 **/
public interface SettingsDao extends JpaRepository<Settings,String>, JpaSpecificationExecutor<Settings> {
    
    /**
     * description  TODO:根据key查找
     * date         2020/9/17 13:50
     * @author      jinFan
     * @param       key：key
     * @return      net.sunbeam.blog.pojo.Settings
     */
    Settings findOneByKey(String key);
}
