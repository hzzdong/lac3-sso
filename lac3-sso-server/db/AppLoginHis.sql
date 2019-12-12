SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_app_login_his`;
CREATE TABLE `tab_app_login_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `appCode` varchar(64) DEFAULT NULL COMMENT '登录站点',
	          `service` varchar(255) DEFAULT NULL COMMENT '站点url',
	          `loginname` varchar(127) DEFAULT NULL COMMENT '登录名',
	          `loginTime` datetime DEFAULT NULL COMMENT '登录时间',
	          `logoutTime` datetime DEFAULT NULL COMMENT '登出时间',
	          `tgt` varchar(127) DEFAULT NULL COMMENT 'MD5(tgt)',
	          `proxy` int(1) DEFAULT NULL COMMENT '是否代理',
	          `proxyChain` varchar(512) DEFAULT NULL COMMENT '代理链',
	          `pgt` varchar(127) DEFAULT NULL COMMENT 'MD5(pgt)',
	          `ppgt` varchar(127) DEFAULT NULL COMMENT 'MD5(parent pgt)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='应用登录日志';

