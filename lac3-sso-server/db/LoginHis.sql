SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_login_his`;
CREATE TABLE `tab_login_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `loginname` varchar(64) DEFAULT NULL COMMENT '登录名',
	          `loginTime` datetime DEFAULT NULL COMMENT '登录时间',
	          `appCode` varchar(64) DEFAULT NULL COMMENT '登录站点',
	          `service` varchar(255) DEFAULT NULL COMMENT '站点url',
	          `ip` varchar(64) DEFAULT NULL COMMENT '访问IP',
	          `mobi` int(1) DEFAULT NULL COMMENT '是否移动设备',
	          `mobileBrand` varchar(64) DEFAULT NULL COMMENT '手机品牌',
	          `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
	          `osVersion` varchar(32) DEFAULT NULL COMMENT '操作系统版本',
	          `browser` varchar(64) DEFAULT NULL COMMENT '浏览器',
	          `browserVersion` varchar(32) DEFAULT NULL COMMENT '浏览器版本',
	          `tgt` varchar(127) DEFAULT NULL COMMENT 'MD5(tgt)',
	          `logoutTime` datetime DEFAULT NULL COMMENT '登出时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='登录日志';

