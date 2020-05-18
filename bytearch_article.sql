CREATE TABLE `kv` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(30) NOT NULL COMMENT '存储字段名',
  `value` varchar(3000) NOT NULL DEFAULT '' COMMENT '存储value',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '字段类型: 1: string , 2: json',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`,`key`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单扩展字段KV表';