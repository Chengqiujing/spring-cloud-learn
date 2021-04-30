CREATE TABLE `config_properties` (
`id` bigint(20) not null auto_increment,
`key1` varchar(50) collate utf8_bin not null,
`value1` varchar(500) collate utf8_bin default null,
`application` varchar(50) collate utf8_bin default null,
`profile` varchar(50)   collate utf8_bin not null,
`label` varchar(50) collate utf8_bin default null,
primary key(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 collate=utf8_bin;


INSERT INTO `config_properties` ( `id`, `key1`, `value1`, `application`, `profile`, `label` )
VALUES
	( '1', 'server.port','8083', 'config-client', 'dev', 'master' );
INSERT INTO `config_properties` ( `id`, `key1`, `value1`, `application`, `profile`, `label` )
VALUES
	( '2', 'foo','bar-jdbc', 'config-client', 'dev', 'master' );
