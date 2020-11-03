package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.ArticleDao;
import net.sunofbeach.blog.dao.ArticleNoContentDao;
import net.sunofbeach.blog.dao.LabelDao;
import net.sunofbeach.blog.pojo.Article;
import net.sunofbeach.blog.pojo.ArticleNoContent;
import net.sunofbeach.blog.pojo.Labels;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.ArticleService;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @program: SobBlogSystem
 * @description: ArticleService的实现类
 * @author: JinFan
 * @create: 2020-09-29 18:02
 **/
@Slf4j
@Service
@Transactional
public class ArticleServiceImpl extends BaseServices implements ArticleService {

    @Resource
    private IdWorker idWorker;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private UserServices userServices;

    @Resource
    private ArticleNoContentDao articleNoContentDao;

    @Resource
    private Random random;

    @Resource
    private LabelDao labelDao;


    /**
     * description  TODO;添加文章
     * date         2020/9/15 22:06
     * 后期可以去做一些定时发布的功能
     * 如果多人的博客系统,得考虑审核的问题 --->问题,通知,审核不通过,也可通知
     * <p>
     * 保存或草稿
     * 1.用户手动提交,会发生页面跳转--->提交即可
     * 2.代码自动提交,每隔一段时间会提交--->不会发生页面跳转--->多次提交---->如果没有唯一标识,会添加到数据库里
     * </p>
     * <p>
     * 不管是哪种草稿--->必须有标题
     * <p>
     * 方案一:每次用户发新文章之前--->先向后台发送一个唯一的文章ID
     * 如果是更新文章,则不需要请求这个唯一ID
     * <p>
     * 方案二:可以直接提交,后台判断有没有ID,如果没有ID,就创建,并且ID作为此次返回的唯一结果
     * 如果有ID就修改已存在的内容.
     * 推荐做法:自动保存草稿,在前段本地完成,也就保存到本地
     * 如果用户手动提交的,就提交到后台
     *
     * </p>
     * 防止重复提交:(网络卡顿的时候,用户点击的频率)
     * 1.可以通过ID的方式,
     * 2.通过token_key的提交评率来计算,如果30秒内又多次提交,只有前一次有效
     * 其他的提交,直接return,提示用户不要太频繁的操作
     * <p>
     * 前端的处理.点击提交以后,禁止提交按钮,等到有相应结果,在恢复按钮状态.
     *
     * @param article :文章
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult postArticle(Article article) {
        //检查用户,获取用户列表
        User user = userServices.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //检查数据
        String title = article.getTitle();
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("文章标题不能为空");
        }
        //状态
        //0表示删除,1.表示已经发布,2.表示草稿,3.表示置顶
        String state = article.getState();
        if (!Constants.Article.STATE_DRAFT.equals(state)
                && !Constants.Article.STATE_PUBLISH.equals(state)) {
            return ResponseResult.OPERATION_IS_NOT_SUPPORTED();
        }
        //type
        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return ResponseResult.FAILED("类型不能为空");
        }
        //判断类型
        if (!"0".equals(type) && !"1".equals(type)) {
            return ResponseResult.FAILED("类型格式不对");
        }
        //以下的检查是发布的检查,草稿不需要检查
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            //判断标题长度
            if (title.length() > Constants.Article.TITLE_MAX_LENGTH) {
                return ResponseResult.FAILED("标题长度不可以超过" + Constants.Article.TITLE_MAX_LENGTH + "个字符");
            }
            String content = article.getContent();
            if (TextUtils.isEmpty(content)) {
                return ResponseResult.FAILED("内容不能为空");
            }
            String label = article.getLabel();
            if (TextUtils.isEmpty(label)) {
                return ResponseResult.FAILED("标签不能为空");
            }
            String summary = article.getSummary();
            //判断summary长度
            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.FAILED("摘要长度不可以超过" + Constants.Article.SUMMARY_MAX_LENGTH + "个字符");
            }
            if (TextUtils.isEmpty(summary)) {
                return ResponseResult.FAILED("摘要不能为空");
            }
        }
        String articleId = article.getId();
        if (TextUtils.isEmpty(articleId)) {
            //补全数据
            article.setId(idWorker.nextId() + "");
            article.setCreateTime(new Date());
        } else {
            //更新内容,对状态进行处理,如果已经发布的,则不能保存为草稿
            Article articleFormDb = articleDao.findOneById(articleId);
            String stateFormDb = articleFormDb.getState();
            if (Constants.Article.STATE_PUBLISH.equals(stateFormDb)
                    && Constants.Article.STATE_DRAFT.equals(state)) {
                //已经发布了,只能更新,不能保存为草稿
                return ResponseResult.FAILED("已经发布的文章不支持保存成草稿");
            }
        }
        article.setUserId(user.getId());
        article.setUpdateTime(new Date());
        //保存到数据库
        //打散标签入库
        this.setupLabels(article.getLabel());
        //返回结果:如果是程序自动提交(比如每30秒保存一次,需要加上这个ID,否则会创建多个Item)
        return ResponseResult.SUCCESS(Constants.Article.STATE_DRAFT.equals(state) ? "草稿发布成功" :
                "文章保存成功!").setDate(article.getId());
    }

    /**
     * description  TODO:打散标签
     * date         2020/10/30 15:57
     * @author      jinFan
     * @param       labels:标签
     */
    private void setupLabels(String labels){
        ArrayList<String> labelList = new ArrayList<>();
        if (labels.contains("-")) {
            labelList.addAll(Arrays.asList(labels.split("-")));
        }else {
            labelList.add(labels);
        }
        //入库,统计
        for (String label : labelList) {
            //找出来
            int result = labelDao.updateCountByName(label);
            if (result==0) {
                Labels targetLabel = new Labels();
                targetLabel.setId(idWorker.nextId()+"");
                targetLabel.setCount(1);
                targetLabel.setName(label);
                targetLabel.setCreateTime(new Date());
                targetLabel.setUpdateTime(new Date());
                labelDao.save(targetLabel);
            }

        }
    }

    /**
     * description  TODO:根据文章id删除文章
     * date         2020/9/15 21:51
     *
     * @param articleId :文章id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteArticle(String articleId) {
        //判断文章是否存在
        Article article = articleDao.findOneById(articleId);
        //如果不存在返回
        if (article == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        //判断文章状态
        if (Constants.Article.STATE_DELETE.equals(article.getState())) {
            return ResponseResult.FAILED("文章已删除!");
        }
        //删除
        int result = articleDao.deleteAllById(articleId);
        //返回结果
        return result > 0 ? ResponseResult.SUCCESS("文章删除成功!") : ResponseResult.FAILED("文章删除失败!");
    }

    /**
     * description  TODO:根据文章id修改文章,标题,内容,标签,分类,摘要
     * date         2020/9/15 21:51
     *
     * @param articleId :文章id
     * @param article   :文章
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateArticle(String articleId, Article article) {
        //判断文章是否存在
        Article articleFormDb = articleDao.findOneById(articleId);
        //如果不存在返回
        if (articleFormDb == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        //判断文章状态
        String state = article.getState();
        if (Constants.Article.STATE_DELETE.equals(state)) {
            return ResponseResult.FAILED("文章已删除!");
        }
        //判断标题,内容,标签,分类,摘要
        //title
        String title = article.getTitle();
        if (!TextUtils.isEmpty(title)) {
            articleFormDb.setTitle(title);
        }
        //内容
        String content = article.getContent();
        if (!TextUtils.isEmpty(content)) {
            articleFormDb.setContent(content);
        }
        //标签
        String label = article.getLabel();
        if (!TextUtils.isEmpty(label)) {
            articleFormDb.setLabel(label);
        }
        //摘要
        String summary = article.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            articleFormDb.setSummary(summary);
        }
        //分类
        String categoryId = article.getCategoryId();
        if (!TextUtils.isEmpty(categoryId)) {
            articleFormDb.setCategoryId(articleId);
        }
        //更新时间
        articleFormDb.setUpdateTime(new Date());
        //保存到数据库
        articleDao.save(articleFormDb);
        //返回结果
        return ResponseResult.SUCCESS("文章更新成功!").setDate(articleFormDb);
    }

    /**
     * description  TODO:根据文章id获取文章
     * date         2020/9/15 21:51
     *
     * @param articleId :文章id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult getArticleById(String articleId) {
        //查询文章
        Article article = articleDao.findOneById(articleId);
        //判断文章是否存在
        if (article == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        //判断状态
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state)
                || Constants.Article.STATE_TOP.equals(state)) {
            //返回结果
            return ResponseResult.SUCCESS("获取文章成功").setDate(article);
        }
        //如果是删除的或草稿,需要权限
        User user = userServices.checkUser();
        if (user == null || !Constants.User.ROLE_ADMIN.equals(user.getRoles())) {
            return ResponseResult.PERMISSION_FORBID();
        }
        //返回结果
        return ResponseResult.SUCCESS("获取文章成功").setDate(article);
    }

    /**
     * description  TODO:获取文章列表
     * date         2020/9/15 22:04
     *
     * @param page                      :页码
     * @param size                      :页数
     * @param keywords:管关键字
     * @param categoryId:分类id
     * @param state:状态:已发布的,已删除的,草稿,置顶的
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listArticles(int page, int size, String state, String keywords, String categoryId) {
        //检查数据
        page = checkPage(page);
        size = checkSize(size);
        //分页条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //分页
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        //查询数据
        Page<ArticleNoContent> all = articleNoContentDao.findAll((Specification<ArticleNoContent>) (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //判断是否传入参数
            if (!TextUtils.isEmpty(state)) {
                Predicate statePre = cb.equal(root.get("state").as(String.class), state);
                predicates.add(statePre);
            }
            if (!TextUtils.isEmpty(categoryId)) {
                Predicate categoryIdPre = cb.equal(root.get("categoryId").as(String.class), categoryId);
                predicates.add(categoryIdPre);
            }
            if (!TextUtils.isEmpty(keywords)) {
                Predicate titlePre = cb.like(root.get("title").as(String.class), "%" + keywords + "%");
                predicates.add(titlePre);
            }
            //传换
            Predicate[] preArray = new Predicate[predicates.size()];
            predicates.toArray(preArray);
            return cb.and(preArray);
        }, pageable);
        //返回结果
        return ResponseResult.SUCCESS("获取文章成功!").setDate(all);
    }

    /**
     * description  TODO:修改状态
     * date         2020/9/15 22:13
     *
     * @param articleId :文章id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteArticleByUpdateState(String articleId) {
        //判断文章是否存在
        Article article = articleDao.findOneById(articleId);
        //如果不存在返回
        if (article == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        //判断文章状态
        if (Constants.Article.STATE_DELETE.equals(article.getState())) {
            return ResponseResult.FAILED("文章已删除!");
        }
        //删除
        int result = articleDao.deleteArticleByState(articleId);
        //返回结果
        return result > 0 ? ResponseResult.SUCCESS("文章删除成功!") : ResponseResult.FAILED("文章删除失败!");
    }

    /**
     * description  TODO:修改状态(置顶)
     * date         2020/9/15 22:13
     *
     * @param articleId :文章id
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult updateTopArticleStateTop(String articleId) {
        //判断文章是否存在
        Article article = articleDao.findOneById(articleId);
        //如果不存在返回
        if (article == null) {
            return ResponseResult.FAILED("文章不存在!");
        }
        //判断文章状态
        if (Constants.Article.STATE_DELETE.equals(article.getState())) {
            return ResponseResult.FAILED("文章已删除!");
        }
        //判断是否置顶
        if (Constants.Article.STATE_TOP.equals(article.getState())) {
            return ResponseResult.FAILED("文章已经置顶");
        }
        //置顶
        int result = articleDao.topArticle(articleId);
        //返回结果
        return result > 0 ? ResponseResult.SUCCESS("文章置顶成功!") : ResponseResult.FAILED("文章置顶失败!");
    }

    /**
     * description  TODO:获取置顶文章
     * date         2020/10/1 13:04
     *
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult ListTopArticles() {
        //查询全部
        List<Article> all = articleDao.findAll((Specification<Article>) (root, criteriaQuery, cb) -> {
            //置顶的文章
            return cb.equal(root.get("state").as(String.class), Constants.Article.STATE_TOP);
        });
        //返回结果
        return ResponseResult.SUCCESS("置顶文章查询成功").setDate(all);
    }

    /**
     * description  TODO；获取文章recommend
     * <p>
     * 通过文章标签计算匹配度
     * 从里面随机哪一个标签 --- >每次获取的文章不那么雷同
     * 通过标签去查询类似的文章,所包含的次标签的文章
     * 如果没有相关文章,就获取最新的文章
     *
     * </p>
     * date         2020/9/16 16:41
     *
     * @param articleId ：文章的id
     * @param size:个数
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listRecommendArticle(String articleId, int size) {
        //查询文章,不需要文章内容
        String labels = articleDao.listArticleLabelsById(articleId);
        //打散
        List<String> labelList = new ArrayList<>();
        boolean contains = labels.contains("-");
        if (!contains) {
            labelList.add(labels);
        }else {
            labelList.addAll(Arrays.asList(labels.split("-")));
        }
        //从列表中随机获取一个标签,查询与此标签相似的文章
        String targetLabel = labelList.get(random.nextInt(labelList.size()));
        //获取文章列表
        List<ArticleNoContent> likeResultList=articleNoContentDao.listArticleByLikeLabel(targetLabel,"%"+articleId+"%",size);
        //判断他的长度
        if (likeResultList.size()<size) {
            //不够数,就获取最新的文章作为补充
            int dxSize = size-likeResultList.size();
            List<ArticleNoContent> dxList = articleNoContentDao.listLastedArticleBySize(articleId,dxSize);
            //有一定的弊端,会把前面找到的也加进来
            likeResultList.addAll(dxList);
        }
        //返回结果
        return ResponseResult.SUCCESS("获取推荐文章成功!").setDate(likeResultList);
    }

    /**
     * description  TODO:通过标签获取文章列表
     * date         2020/10/1 16:50
     *
     * @param page  :页码
     * @param size  :个数,每页的个数
     * @param label :标签
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listArticlesByLabel(int page, int size, String label) {
        //数据检查
        page = checkPage(page);
        size = checkSize(size);
        //查询条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //分页
        Pageable  pageable=PageRequest.of(page-1,size,sort);
        //查询
        Page<ArticleNoContent> all = articleNoContentDao.findAll(
            (Specification<ArticleNoContent>) (root, cq, cb) -> {
            //标签
            Predicate labelsPre = cb.like(root.get("labels").as(String.class),"%"+label+"%");
            //文章状态
            Predicate or = cb.or(
                    cb.equal(root.get("state").as(String.class),Constants.Article.STATE_PUBLISH),
                    cb.equal(root.get("state").as(String.class), Constants.Article.STATE_TOP)
            );
            return cb.and(labelsPre,or);
        }, pageable);
        //返回结果
        return ResponseResult.SUCCESS("查询成功!").setDate(all);
    }

    /**
     * description  TODO:获取标签云,用户点击标签,就会通过标签获取相关的文章
     * <p>
     * 任意用户
     * </p>
     * date         2020/10/1 13:21
     *
     * @param size :个数
     * @return net.sunofbeach.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listLabels(int size) {
        //检查数据
        size = this.checkSize(size);
        //查询条件
        Sort sort =  Sort.by(Sort.Direction.DESC, "count");
        //分页
        Pageable pageable = PageRequest.of(0, size, sort);
        //查询全部
        Page<Labels> all = labelDao.findAll(pageable);
        //返回结果
        return ResponseResult.SUCCESS("数据查询成功!").setDate(all);
    }
}
