SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_account`;
CREATE TABLE `tab_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `loginname` varchar(255) DEFAULT NULL COMMENT '登录名',
	          `name` varchar(255) DEFAULT NULL COMMENT '名称',
	          `mobile` varchar(255) DEFAULT NULL COMMENT '手机',
	          `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
	          `sex` int(1) DEFAULT NULL COMMENT '性别',
	          `birthday` datetime DEFAULT NULL COMMENT '生日',
	          `ico` varchar(255) DEFAULT NULL COMMENT '头像',
	          `passwd` varchar(255) DEFAULT NULL COMMENT '密码',
	          `salt` varchar(127) DEFAULT NULL COMMENT '密码盐',
	          `lastLoginDate` datetime DEFAULT NULL COMMENT '最后登录时间',
	          `oldPasswds` varchar(255) DEFAULT NULL COMMENT '老密码',
	          `lastPasswdDate` datetime DEFAULT NULL COMMENT '改密时间',
	          `wechatOpenId` varchar(127) DEFAULT NULL COMMENT '微信openid',
	          `alipayOpenId` varchar(127) DEFAULT NULL COMMENT '支付宝openid',
	          `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='账号';

