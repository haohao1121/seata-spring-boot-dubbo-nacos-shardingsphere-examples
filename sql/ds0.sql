/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50718
Source Host           : 127.0.0.1:3306
Source Database       : ds0

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-05-21 18:32:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_account0
-- ----------------------------
DROP TABLE IF EXISTS `t_account0`;
CREATE TABLE `t_account0` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_account0
-- ----------------------------
INSERT INTO `t_account0` VALUES ('2', '3000.00');

-- ----------------------------
-- Table structure for t_account1
-- ----------------------------
DROP TABLE IF EXISTS `t_account1`;
CREATE TABLE `t_account1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_account1
-- ----------------------------
INSERT INTO `t_account1` VALUES ('1', '3700.00');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);

-- ----------------------------
-- Records of undo_log
-- ----------------------------
