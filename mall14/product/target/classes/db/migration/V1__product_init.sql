/*
 Navicat Premium Data Transfer

 Source Server         : 49.232.140.99
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 49.232.140.99
 Source Database       : mall_product

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : utf-8

 Date: 05/26/2020 23:32:14 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '商品名',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `pic_urls` varchar(250) DEFAULT NULL COMMENT '商品主图集合',
  `keywords` varchar(50) DEFAULT NULL COMMENT '商品搜索关键字，最多20个汉字',
  `description` text COMMENT '商品的描述文',
  `listing` tinyint(2) DEFAULT '1' COMMENT '是否上架，默认1位上架',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已被删除，默认0没有被删除',
  `product_class_id` bigint(20) DEFAULT NULL COMMENT '所属类别的id',
  `product_class_name` varchar(20) DEFAULT NULL COMMENT '所属类别的名称',
  `price` double(20,0) DEFAULT NULL COMMENT '展示在商品列表的商品价格，不是用户实际要支付的价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ----------------------------
--  Table structure for `product_class`
-- ----------------------------
DROP TABLE IF EXISTS `product_class`;
CREATE TABLE `product_class` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类别';

-- ----------------------------
--  Table structure for `product_class_attr`
-- ----------------------------
DROP TABLE IF EXISTS `product_class_attr`;
CREATE TABLE `product_class_attr` (
  `id` bigint(20) NOT NULL,
  `attr_key` varchar(50) DEFAULT NULL COMMENT '类别属性的key',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `product_class_id` bigint(20) DEFAULT NULL COMMENT '分类id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_attr_key` (`attr_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类别属性表，属于商品类别的一个属性，没有值只有属性key';

-- ----------------------------
--  Table structure for `product_class_attr_value`
-- ----------------------------
DROP TABLE IF EXISTS `product_class_attr_value`;
CREATE TABLE `product_class_attr_value` (
  `id` bigint(20) NOT NULL,
  `attr_value` varchar(50) DEFAULT NULL COMMENT '类别属性的key',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `product_class_attr_id` bigint(20) DEFAULT NULL COMMENT '类别属性表的id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_attr_value` (`attr_value`,`product_class_attr_id`) USING BTREE COMMENT '唯一标识一个有效的商品属性值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类别属性值表，类别属性表的value';

-- ----------------------------
--  Table structure for `product_specs`
-- ----------------------------
DROP TABLE IF EXISTS `product_specs`;
CREATE TABLE `product_specs` (
  `id` bigint(20) NOT NULL,
  `product_specs` varchar(250) DEFAULT NULL COMMENT '规格，json存储，key和value来自类别属性表、类别属性值表',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price` double(20,0) DEFAULT NULL COMMENT '这种规格下的商品价格',
  `stock` bigint(20) DEFAULT NULL COMMENT '库存数量',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表，记录sku数据和对应价格、库存数量';

SET FOREIGN_KEY_CHECKS = 1;
