/*
 Navicat Premium Data Transfer

 Source Server         : 49.232.140.99
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 49.232.140.99
 Source Database       : mall_user

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : utf-8

 Date: 05/28/2020 00:15:38 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `m_permission`
-- ----------------------------
DROP TABLE IF EXISTS `m_permission`;
CREATE TABLE `m_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限',
  `name` varchar(64) NOT NULL COMMENT '权限名称',
  `enname` varchar(64) NOT NULL COMMENT '权限英文名称',
  `url` varchar(255) NOT NULL COMMENT '授权路径',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
--  Records of `m_permission`
-- ----------------------------
BEGIN;
INSERT INTO `m_permission` VALUES ('2', '0', 'mPermission查询', 'mPermission', '/**', '权限接口', '2020-05-09 22:35:10', '2020-05-13 23:59:33');
COMMIT;

-- ----------------------------
--  Table structure for `m_role`
-- ----------------------------
DROP TABLE IF EXISTS `m_role`;
CREATE TABLE `m_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色',
  `name` varchar(64) NOT NULL COMMENT '角色名称',
  `enname` varchar(64) NOT NULL COMMENT '角色英文名称',
  `description` varchar(200) DEFAULT NULL COMMENT '备注',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
--  Records of `m_role`
-- ----------------------------
BEGIN;
INSERT INTO `m_role` VALUES ('1', '0', 'super', 'super', 'super', '2020-05-09 11:54:20', '2020-05-09 11:54:22');
COMMIT;

-- ----------------------------
--  Table structure for `m_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `m_role_permission`;
CREATE TABLE `m_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限 ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
--  Records of `m_role_permission`
-- ----------------------------
BEGIN;
INSERT INTO `m_role_permission` VALUES ('1', '1', '2', '2020-05-09 11:54:31', '2020-05-09 22:35:47');
COMMIT;

-- ----------------------------
--  Table structure for `m_user`
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1256465872748285955 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
--  Records of `m_user`
-- ----------------------------
BEGIN;
INSERT INTO `m_user` VALUES ('1256465872748285953', 'root', '$2a$10$HO088qWjUT.fQhR9CY/Uo.Z836J1tYtP1fz6QrBJZqsgYIEAasdre', null, '2020-05-02 14:09:42', '2020-05-02 14:09:42'), ('1256465872748285954', 'test', '$2a$10$HO088qWjUT.fQhR9CY/Uo.Z836J1tYtP1fz6QrBJZqsgYIEAasdre', null, '2020-05-14 00:01:37', '2020-05-14 00:01:40');
COMMIT;

-- ----------------------------
--  Table structure for `m_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `m_user_role`;
CREATE TABLE `m_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户 ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
--  Records of `m_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `m_user_role` VALUES ('1', '1256465872748285953', '1', '2020-05-09 11:54:45', '2020-05-09 11:54:45');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
