/*
Navicat MySQL Data Transfer

Source Server         : 47.100.211.84
Source Server Version : 50721
Source Host           : 47.100.211.84:3306
Source Database       : um

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-11-28 10:12:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tab_web_log
-- ----------------------------
DROP TABLE IF EXISTS `tab_web_log`;
CREATE TABLE `tab_web_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `tid` varchar(64) DEFAULT NULL COMMENT 'TID',
  `module` varchar(64) DEFAULT NULL COMMENT '操作的模块',
  `operateDesc` varchar(1024) DEFAULT NULL COMMENT '操作描述',
  `operateTime` datetime NOT NULL COMMENT '操作时间',
  `costTime` int(11) DEFAULT NULL COMMENT '操作花费时间',
  `operateResult` int(1) DEFAULT NULL COMMENT '操作结果:成功/失败',
  `errorMessage` varchar(1024) DEFAULT NULL COMMENT '失败的error信息',
  `orgId` varchar(32) DEFAULT NULL COMMENT '操作者所属的组织ID',
  `orgType` varchar(32) DEFAULT NULL COMMENT '操作者所属的组织类型',
  `operatorId` varchar(64) DEFAULT NULL COMMENT '操作者的id',
  `operatorAccount` varchar(64) DEFAULT NULL COMMENT '操作者的账号',
  `ip` varchar(64) DEFAULT NULL COMMENT '操作者的登陆ip',
  `url` varchar(512) DEFAULT NULL COMMENT 'Url',
  `mobile` int(1) DEFAULT NULL COMMENT '是否移动设备',
  `mobileBrand` varchar(32) DEFAULT NULL COMMENT '手机品牌信息',
  `os` varchar(32) DEFAULT NULL COMMENT '操作系统',
  `osVersion` varchar(32) DEFAULT NULL COMMENT '操作系统版本',
  `browser` varchar(32) DEFAULT NULL COMMENT '浏览器',
  `browserVersion` varchar(32) DEFAULT NULL COMMENT '浏览器版本',
  `ua` varchar(512) DEFAULT NULL COMMENT 'navigator.userAgent',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
