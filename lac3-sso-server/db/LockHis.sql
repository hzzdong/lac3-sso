SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `tab_lock_his`;
CREATE TABLE `tab_lock_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(64) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
	          `hisId` int(11) DEFAULT NULL COMMENT 'ID',
	          `lockedTarget` varchar(255) DEFAULT NULL COMMENT '锁定目标',
	          `type` int(11) NOT NULL DEFAULT '0' COMMENT '锁定类型',
	          `count` int(11) NOT NULL DEFAULT '0' COMMENT '锁定次数',
	          `reason` int(11) NOT NULL DEFAULT '0' COMMENT '锁定原因',
	          `lockedTime` datetime DEFAULT NULL COMMENT '锁定时间',
	          `operator` varchar(255) DEFAULT NULL COMMENT '操作员',
	          `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='锁/解锁历史';

