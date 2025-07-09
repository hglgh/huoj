CREATE DATABASE IF NOT EXISTS `huoj`;
USE `huoj`;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userEmail    varchar(512)                           null comment '用户邮箱',
    userPhone    varchar(512)                           null comment '用户手机号',
    userAddress  varchar(512)                           null comment '用户地址',
    userSex      varchar(512)                           null comment '用户性别',
    userAge      varchar(512)                           null comment '用户年龄',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    unique index userAccount_unique (userAccount asc),
    unique index userEmail_unique (userEmail asc),
    unique index userPhone_unique (userPhone asc),
    index idx_userName (userName asc)
) comment '用户' collate = utf8mb4_unicode_ci;