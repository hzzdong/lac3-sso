SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_manager`;
CREATE TABLE `tab_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `loginname` varchar(127) DEFAULT NULL COMMENT '登录名',
	          `passwd` varchar(127) DEFAULT NULL COMMENT '密码',
	          `salt` varchar(127) DEFAULT NULL COMMENT '密码盐',
	          `name` varchar(64) DEFAULT NULL COMMENT '名称',
	          `mobile` varchar(20) DEFAULT NULL COMMENT '手机',
	          `email` varchar(127) DEFAULT NULL COMMENT '邮箱',
	          `birthday` datetime DEFAULT NULL COMMENT '生日',
	          `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='管理员';

