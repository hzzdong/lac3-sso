SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_app_account`;
CREATE TABLE `tab_app_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `appId` int(11) DEFAULT NULL COMMENT '应用ID',
	          `appCode` varchar(64) DEFAULT NULL COMMENT '应用编码',
	          `appName` varchar(64) DEFAULT NULL COMMENT '应用名称',
	          `appLoginName` varchar(64) DEFAULT NULL COMMENT '应用账号',
	          `accountId` int(11) DEFAULT NULL COMMENT '统一账号id',
	          `loginname` varchar(64) DEFAULT NULL COMMENT '统一账号',
	          `name` varchar(64) DEFAULT NULL COMMENT '姓名',
	          `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='应用账号';

