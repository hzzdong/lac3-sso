SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_sys_setup`;
CREATE TABLE `tab_sys_setup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `code` varchar(64) NOT NULL COMMENT '设置项编号',
	          `name` varchar(64) NOT NULL COMMENT '设置项名称',
	          `value` varchar(255) NOT NULL COMMENT '设置项值',
	          `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统设置';

