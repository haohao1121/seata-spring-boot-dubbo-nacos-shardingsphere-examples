/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50718
Source Host           : 127.0.0.1:3306
Source Database       : ds1

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2020-05-21 18:32:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_order0
-- ----------------------------
DROP TABLE IF EXISTS `t_order0`;
CREATE TABLE `t_order0` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_order0
-- ----------------------------
INSERT INTO `t_order0` VALUES ('1263365189460000769', '20307f1c422849b480e4058caa3bccd8', '2', 'C201901140001', '50', '100.00');
INSERT INTO `t_order0` VALUES ('1263365731955474433', '9d2cb390aa904bf88721e062894317b8', '2', 'C201901140001', '50', '100.00');
INSERT INTO `t_order0` VALUES ('1263365778319310849', '9224618b9038468e9403e62df9f183cd', '2', 'C201901140001', '50', '100.00');
INSERT INTO `t_order0` VALUES ('1263369292944736257', '1629ad7b842a4b9999ee3121d7aec559', '2', 'C201901140001', '50', '100.00');
INSERT INTO `t_order0` VALUES ('1263383073078804482', '84ddf3d6444f4cffbff4d1d1cbdfc1cb', '2', 'C201901140001', '50', '100.00');

-- ----------------------------
-- Table structure for t_order1
-- ----------------------------
DROP TABLE IF EXISTS `t_order1`;
CREATE TABLE `t_order1` (
  `id` varchar(32) NOT NULL,
  `order_no` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT '0',
  `amount` double(14,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_order1
-- ----------------------------
INSERT INTO `t_order1` VALUES ('1263363885937426434', '7f6e1b0c82104c65bc5b600ae0967d2e', '1', 'C201901140001', '50', '100.00');
INSERT INTO `t_order1` VALUES ('1263383132470149122', 'fadd015b354945dcaf06f1337c4a7061', '1', 'C201901140001', '50', '100.00');
INSERT INTO `t_order1` VALUES ('1263383180624953346', 'e4907867763a4fd1b8b358ad146f9f7b', '1', 'C201901140002', '50', '100.00');

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
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);

-- ----------------------------
-- Records of undo_log
-- ----------------------------
