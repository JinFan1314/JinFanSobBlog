package net.sunofbeach.blog.services.Impl;

import lombok.extern.slf4j.Slf4j;
import net.sunofbeach.blog.dao.ImageDao;
import net.sunofbeach.blog.pojo.Images;
import net.sunofbeach.blog.pojo.User;
import net.sunofbeach.blog.response.ResponseResult;
import net.sunofbeach.blog.services.ImageServices;
import net.sunofbeach.blog.services.UserServices;
import net.sunofbeach.blog.util.Constants;
import net.sunofbeach.blog.util.IdWorker;
import net.sunofbeach.blog.util.TextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SobBlogSystem
 * @description: ImageServices的实现类
 * @author: JinFan
 * @create: 2020-09-27 15:24
 **/
@Slf4j
@Service
@Transactional
@PropertySource({"classpath:application-dev.yml"})
public class ImageServicesImpl extends BaseServices implements ImageServices {

    @Resource
    private IdWorker idWorker;

    @Resource
    private UserServices userServices;

    @Resource
    private ImageDao imageDao;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    @Value("${sob.blog.image.save-path}")
    public String imagePath;

    @Value("${sob.blog.image.max-size}")
    public long maxSize;

    /**
     * description  TODO:上传图片
     * date         2020/9/15 21:48
     *
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //判断是否有文件
        if (file == null) {
            return ResponseResult.FAILED("图片文件不能为空!");
        }
        //判断文件类型,.jpg.gif.png
        String contentType = file.getContentType();
        log.info("contentType == >" + contentType);
        if (TextUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("文件格式错误!");
        }
        String originalFilename = file.getOriginalFilename();
        log.info("originalFilename == >" + originalFilename);
        String type = getType(contentType, originalFilename);
        if (type == null) {
            return ResponseResult.FAILED("不支持该类型图片!");
        }
        //获取相关数据
        //限制文件的大小
        long size = file.getSize();
        log.info("maxSize == >" + maxSize + "size == >" + size);
        if (size > maxSize) {
            return ResponseResult.FAILED("图片最大仅支持" + (maxSize / 1024 / 1024) + "MB");
        }
        //根据规则命名
        long currentMillions = System.currentTimeMillis();
        String currentDay = simpleDateFormat.format(currentMillions);
        log.info("current day" + currentDay);
        //判断日期
        String dayPath = imagePath + File.separator + currentDay;
        File dayPathFile = new File(dayPath);
        if (!dayPathFile.exists()) {
            boolean mkdirs = dayPathFile.mkdirs();
            if (!mkdirs) {
                return ResponseResult.FILE_UPLOAD_FAILED();
            }
        }
        String targetName = String.valueOf(idWorker.nextId());
        String targetPath = dayPath +
                File.separator + type + File.separator + targetName + "." + type;
        File targetFile = new File(targetPath);
        //判断类型
        if (!targetFile.getParentFile().exists()) {
            boolean mkdirs = targetFile.mkdirs();
            if (!mkdirs) {
                return ResponseResult.FILE_UPLOAD_FAILED();
            }
        }
        try {
            if (!targetFile.exists()) {
                boolean newFile = targetFile.createNewFile();
                if (!newFile) {
                    return ResponseResult.FILE_UPLOAD_FAILED();
                }
            }
            log.info("targetFile == >" + targetFile);
            //保存文件
            file.transferTo(targetFile);
            //保存到数据库
            //返回结果:图片的访问路径和id
            //TODO:
            //返回访问路径
            Map<String, String> result = new HashMap<>();
            String resultPath = currentMillions + "_" + targetName + "." + type;
            result.put("id", resultPath);
            result.put("name", originalFilename);
            Images images = new Images();
            images.setId(idWorker.nextId() + "");
            images.setContentType(contentType);
            images.setPath(targetPath);
            images.setName(originalFilename);
            images.setState("1");
            images.setUrl(resultPath);
            images.setUpdateTime(new Date());
            images.setCreateTime(new Date());
            //用户id
            User user = userServices.checkUser();
            images.setUserId(user.getId());
            //记录文件
            imageDao.save(images);
            //返回结果
            return ResponseResult.FILE_UPLOAD_SUCCESS().setDate(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.FILE_UPLOAD_FAILED();
    }

    private String getType(String contentType, String originalFilename) {
        String type = null;
        if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType)
                && originalFilename.endsWith(Constants.ImageType.TYPE_PNG)) {
            type = Constants.ImageType.TYPE_PNG;
        } else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType)
                && originalFilename.endsWith(Constants.ImageType.TYPE_GIF)) {
            type = Constants.ImageType.TYPE_GIF;
        } else if (Constants.ImageType.TYPE_JPG_WITH_PREFIX.equals(contentType)
                && originalFilename.endsWith(Constants.ImageType.TYPE_JPG)) {
            type = Constants.ImageType.TYPE_JPG;
        } else if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType)
                && originalFilename.endsWith(Constants.ImageType.TYPE_JPG)) {
            type = Constants.ImageType.TYPE_JPG;
        }
        return type;
    }

    /**
     * description  TODO:根据图片id获取图片
     * date         2020/9/15 21:51
     *
     * @param imageId:图片ID
     * @param response:response
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public void viewsImage(HttpServletResponse response, String imageId) throws IOException {
        //配置的目录已知
        String[] paths = imageId.split("_");
        //需要日期
        String dayValue = paths[0];
        String format = simpleDateFormat.format(Long.parseLong(dayValue));
        //ID
        String name = paths[1];
        //需要类型
        String type = name.substring(name.length() - 3);
        //使用时间戳_ID.类型
        String targetPath = imagePath + File.separator + format + File.separator + type +
                File.separator + name;
        log.info("targetPath == >" + targetPath);
        File file = new File(targetPath);
        FileInputStream fos = null;
        OutputStream writer = null;
        try {
            response.setContentType("image/png");
            writer = response.getOutputStream();
            //读取
            fos = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = fos.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * description  TODO:获取图片列表
     * date         2020/9/15 22:04
     *
     * @param page:页码
     * @param size:页数
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult listImages(int page, int size) {
        //处理page和size
        page = checkPage(page);
        size = checkSize(size);
        User user = userServices.checkUser();
        if (user == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //创建分页条件
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //查询
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        //返回结果
        String userId = user.getId();
        Page<Images> all = imageDao.findAll((Specification<Images>) (root, criteriaQuery, cb) -> {
            //用户id和状态
            return cb.and(cb.equal(root.get("userId").as(String.class), userId),
                    cb.equal(root.get("state").as(String.class), "1"));
        }, pageable);
        return ResponseResult.SUCCESS("获取列表成功!").setDate(all);
    }

    /**
     * description  TODO:根据图片id删除图片
     * <p>
     * 并不是真正的删除,只是修改状态
     * </p>
     * date         2020/9/15 21:51
     *
     * @param imageId :图片ID
     * @return net.sunbeam.blog.response.ResponseResult
     * @author jinFan
     */
    @Override
    public ResponseResult deleteImage(String imageId) {
        //判断图片是否存在
        Images imagesFromDb = imageDao.findOneById(imageId);
        if (imagesFromDb == null) {
            return ResponseResult.FAILED("图片不存在");
        }
        //删除图片
        int result = imageDao.deleteImagesByState(imageId);
        //返回结果
        return result > 0 ? ResponseResult.SUCCESS("图片删除成功!") : ResponseResult.FAILED("图片删除失败!");
    }
}
