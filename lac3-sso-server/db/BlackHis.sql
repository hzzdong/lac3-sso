SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_black_his`;
CREATE TABLE `tab_black_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `hisId` int(11) DEFAULT NULL COMMENT 'ID',
	          `blackTarget` varchar(255) DEFAULT NULL COMMENT '加黑目标',
	          `type` int(11) NOT NULL DEFAULT '0' COMMENT '加黑类型',
	          `blackTime` datetime DEFAULT NULL COMMENT '加黑时间',
	          `reason` int(11) NOT NULL DEFAULT '0' COMMENT '加黑原因',
	          `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
	          `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='黑名单历史';

