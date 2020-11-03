/*
 Navicat Premium Data Transfer

 Source Server         : localhostMysql
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : sob_blog_system

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 30/09/2020 02:39:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
  `category_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文章内容',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '封面',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（0表示富文本，1表示markdown）',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示已发布，1表示草稿，2表示删除）',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '摘要',
  `labels` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数量',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_article_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_category_article_on_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_category_article_on_category_id` FOREIGN KEY (`category_id`) REFERENCES `tb_categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_article_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('760676572544892928', 'C语言是如何编写的', '757420538804043776', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '金帆', '759430006865657856', '这是关于C语言的文章', '', '0', '1', 'C语言的文章', 'C语言', 0, '2020-09-29 17:37:08', '2020-09-29 17:37:08');
INSERT INTO `tb_article` VALUES ('760678788601217024', 'JAVA语言是如何编写的', '757420538804043776', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '金帆', '759423364719706112', '这是关于JAVA的文章', '', '0', '2', NULL, 'JAVA的多线程', 0, '2020-09-29 17:45:56', '2020-09-29 17:45:56');

-- ----------------------------
-- Table structure for tb_categories
-- ----------------------------
DROP TABLE IF EXISTS `tb_categories`;
CREATE TABLE `tb_categories`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `order` int(11) NOT NULL COMMENT '顺序',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_categories
-- ----------------------------
INSERT INTO `tb_categories` VALUES ('759423364719706112', 'java', 'java', '编程语言,多线程', 2, '1', '2020-09-26 06:37:20', '2020-09-26 06:37:20');
INSERT INTO `tb_categories` VALUES ('759429516341805056', 'html', 'html', 'html 网页编程语言', 1, '1', '2020-09-26 07:01:46', '2020-09-26 07:01:46');
INSERT INTO `tb_categories` VALUES ('759429656561582080', 'css', 'css', 'html 网页编程语言的样式表', 3, '1', '2020-09-26 07:02:20', '2020-09-26 07:02:20');
INSERT INTO `tb_categories` VALUES ('759430006865657856', 'C语言', 'C', 'C语言', 5, '0', '2020-09-26 07:03:43', '2020-09-26 07:03:43');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `parent_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '父内容',
  `article_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_comment_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_article_comment_on_article_id`(`article_id`) USING BTREE,
  CONSTRAINT `fk_article_comment_on_article_id` FOREIGN KEY (`article_id`) REFERENCES `tb_article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_comment_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_daily_view_count
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_view_count`;
CREATE TABLE `tb_daily_view_count`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '每天浏览量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_friends
-- ----------------------------
DROP TABLE IF EXISTS `tb_friends`;
CREATE TABLE `tb_friends`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '顺序',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接状态:0表示不可用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_friends
-- ----------------------------
INSERT INTO `tb_friends` VALUES ('759477856513294336', '阳光沙滩', 'https://www.sunofbeach.net/sob.png', 'httt://net.sunofbeach.blog.com', 1, '1', '2020-09-26 10:13:51', '2020-09-26 10:13:51');

-- ----------------------------
-- Table structure for tb_images
-- ----------------------------
DROP TABLE IF EXISTS `tb_images`;
CREATE TABLE `tb_images`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路径',
  `content_type` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片路径',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_images_on_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_user_images_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_images
-- ----------------------------
INSERT INTO `tb_images` VALUES ('760161639243710464', '757422924163448832', '1601278257905_760161639201767424.jpg', 'image/jpeg', 'E:\\project\\gitClonePorject\\SobBlogSystem\\images\\2020.09.28\\jpg\\760161639201767424.jpg', '9236d7fc8fe490e9b23acd25cb992200.jpg', '1', '2020-09-28 07:30:58', '2020-09-28 07:30:58');
INSERT INTO `tb_images` VALUES ('760185041333518336', '757420538804043776', '1601283837407_760185041325129728.png', 'image/png', 'E:\\project\\gitClonePorject\\SobBlogSystem\\images\\2020.09.28\\png\\760185041325129728.png', 'cover.png', '1', '2020-09-28 09:03:57', '2020-09-28 09:03:57');
INSERT INTO `tb_images` VALUES ('760185272695521280', '757420538804043776', '1601283892568_760185272687132672.jpg', 'image/jpeg', 'E:\\project\\gitClonePorject\\SobBlogSystem\\images\\2020.09.28\\jpg\\760185272687132672.jpg', '刀剑神域,朝田诗乃3440x1440动漫壁纸_千叶网.jpg', '1', '2020-09-28 09:04:53', '2020-09-28 09:04:53');
INSERT INTO `tb_images` VALUES ('760185323371102208', '757420538804043776', '1601283904642_760185323329159168.jpg', 'image/jpeg', 'E:\\project\\gitClonePorject\\SobBlogSystem\\images\\2020.09.28\\jpg\\760185323329159168.jpg', 'newpaciure.jpg', '3', '2020-09-28 09:05:05', '2020-09-28 09:05:05');
INSERT INTO `tb_images` VALUES ('760205135258320896', '757420538804043776', '1601288628171_760205135249932288.jpg', 'image/jpeg', 'E:\\project\\gitClonePorject\\SobBlogSystem\\images\\2020.09.28\\jpg\\760205135249932288.jpg', '93b4f00e30f595a020b0e7dc09338154.jpg', '0', '2020-09-28 10:23:48', '2020-09-28 10:23:48');

-- ----------------------------
-- Table structure for tb_labels
-- ----------------------------
DROP TABLE IF EXISTS `tb_labels`;
CREATE TABLE `tb_labels`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `count` int(11) NOT NULL DEFAULT 0 COMMENT '数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_looper
-- ----------------------------
DROP TABLE IF EXISTS `tb_looper`;
CREATE TABLE `tb_looper`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图标题',
  `order` int(11) NOT NULL DEFAULT 0 COMMENT '顺序',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不可用，1表示正常',
  `target_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标URL',
  `image_url` varchar(2014) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_refresh_token`;
CREATE TABLE `tb_refresh_token`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `refresh_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'refresh_token',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `token_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'toekn_key值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_refresh_token
-- ----------------------------
INSERT INTO `tb_refresh_token` VALUES ('760675692038848512', 'eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3NTc0MjA1Mzg4MDQwNDM3NzYiLCJpYXQiOjE2MDE0MDA4MTcsImV4cCI6MTYwMzk5MjgxN30.4jGgRWYsI89m3Ur6cUq2Bbmui0D3N_3ZRjXJKj3Ul-o', '757420538804043776', 'c5cc4d951800e4eac9954d67c3d28ef1', '2020-09-29 17:33:38', '2020-09-29 17:33:38');

-- ----------------------------
-- Table structure for tb_settings
-- ----------------------------
DROP TABLE IF EXISTS `tb_settings`;
CREATE TABLE `tb_settings`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '键',
  `value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_settings
-- ----------------------------
INSERT INTO `tb_settings` VALUES ('757420538820820992', 'manager_account_init_state', '1', '2020-09-20 17:58:49', '2020-09-20 17:58:49');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `roles` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像地址',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '签名',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示删除，1表示正常',
  `reg_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录Ip',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('757420538804043776', '金帆', '$2a$10$BL8yS./nMd.SNvpaoEil9.HY9Jx4vBXbI9pl.GEPI2KaxiFDUwhCK', 'role_admin', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '2539906953@qq.com', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-09-20 17:58:49', '2020-09-20 17:58:49');
INSERT INTO `tb_user` VALUES ('757422924163448832', '程序员小金', '$2a$10$GJrIIg/wUYw5BaIAI1n6weTGX8L2uASELbfK/.8jhhj5v9QGrrPe6', 'role_normal', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '2539906953@qq.com', '你好', '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-09-20 18:08:17', '2020-09-20 18:08:17');
INSERT INTO `tb_user` VALUES ('758785640182579200', '小金子', '$2a$10$KhuqDD7/cPhb.VSqboECHevhLwS2SWzdfrlchFs8NaDxZqSFGMk52', 'role_normal', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '1582516092@qq.com', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-09-24 12:23:14', '2020-09-24 12:23:14');
INSERT INTO `tb_user` VALUES ('758786477319520256', '小金崽', '$2a$10$.Lg3TTEKst5ZAIx1/IC82ewkhV4MwL5M3omV8KNfsS8c9oercBIKy', 'role_normal', 'https://cdn.sunofbeaches.com/images/default_avatar.png', '761924224@qq.com', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-09-24 12:26:34', '2020-09-24 12:26:34');

SET FOREIGN_KEY_CHECKS = 1;
