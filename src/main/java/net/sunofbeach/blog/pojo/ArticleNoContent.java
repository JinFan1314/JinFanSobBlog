package net.sunofbeach.blog.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: SobBlogSystem
 * @description: 没有内容的文章
 * @author: JinFan
 * @create: 2020-09-30 02:23
 **/
@Entity
@Table( name ="tb_article" )
public class ArticleNoContent {
    @Id
    private String id;
    @Column(name = "title" )
    private String title;
    @Column(name = "user_id" )
    private String userId;
    @Column(name = "category_id" )
    private String categoryId;
    @Column(name = "cover" )
    private String cover;
    @Column(name = "type" )
    private String type;
    @Column(name = "state" )
    private String state = "1";
    @Column(name = "summary" )
    private String summary;
    @Column(name = "labels" )
    private String labels;
    @Column(name = "view_count" )
    private long viewCount=0L;
    @Column(name = "create_time" )
    private Date createTime;
    @Column(name = "update_time" )
    private Date updateTime;

    @OneToOne(targetEntity = UserNoPassword.class)
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private UserNoPassword userNoPassword;


    public UserNoPassword getUserNoPassword() {
        return userNoPassword;
    }

    public void setUserNoPassword(UserNoPassword userNoPassword) {
        this.userNoPassword = userNoPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
