# 优惠券系统

这是一个基于Spring Boot开发的优惠券管理系统，采用DDD（领域驱动设计）架构。

## 技术栈

- Spring Boot 2.7.18
- Java 8
- Gradle 8.5
- H2内存数据库
- MyBatis
- Lombok
- MapStruct

## 功能特性

- 优惠券模板管理
- 优惠券发放与领取
- 优惠券使用与核销
- 用户优惠券查询
- 完整的DDD分层架构

## API接口

- `POST /api/coupon/templates` - 创建优惠券模板
- `POST /api/coupon/users/{userId}/receive` - 用户领取优惠券
- `GET /api/coupon/users/{userId}/coupons` - 查询用户优惠券
- `POST /api/coupon/users/{userId}/use` - 核销优惠券