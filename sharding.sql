create database  ba_test default charset = utf8;
CREATE TABLE `kv_000` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '存储name',
  `value` varchar(2000) NOT NULL DEFAULT '' COMMENT '存储value',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '字段类型: 1: string , 2: json',
    PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='kv扩展字段表';

CREATE TABLE `kv_001` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '存储name',
  `value` varchar(2000) NOT NULL DEFAULT '' COMMENT '存储value',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '字段类型: 1: string , 2: json',
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='kv扩展字段表';

CREATE TABLE `kv` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '存储name',
  `value` varchar(2000) NOT NULL DEFAULT '' COMMENT '存储value',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '字段类型: 1: string , 2: json',
    PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='kv扩展字段表';
