/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : security

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 04/03/2024 10:48:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for excel_configure
-- ----------------------------
DROP TABLE IF EXISTS `excel_configure`;
CREATE TABLE `excel_configure`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '导出的时候,只需要根据该id查询模板即可',
  `service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Service名称,Service的路径,反射使用（我这里使用的是Controller）',
  `service_method_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Service方法名称,反射使用（Controller方法名）',
  `entity_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出的对象实体类路径',
  `template_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板地址',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出的文件名称',
  `save_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出的路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of excel_configure
-- ----------------------------
INSERT INTO `excel_configure` VALUES (2, 'com.works.bootworks.service.imp.UserInfoServiceImpl', 'getAll', 'com.works.bootworks.entity.UserInfo', NULL, 'user', NULL);
INSERT INTO `excel_configure` VALUES (3, 'com.works.bootworks.service.imp.UserInfo2ServiceImpl', 'getAll', 'com.works.bootworks.entity.UserInfo', NULL, 'user2', NULL);

SET FOREIGN_KEY_CHECKS = 1;
